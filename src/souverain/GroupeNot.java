/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souverain;

import static java.lang.Math.*;
import java.util.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author kamowskn
 */
public class GroupeNot {

    private Set<Notation> notations;
    private String nom;

    private Caracteristic mean = new Caracteristic(0, 0, 0);
    private Caracteristic median = new Caracteristic(0, 0, 0);
    private Caracteristic min = new Caracteristic(0, 0, 0);
    private Caracteristic max = new Caracteristic(0, 0, 0);
    private Caracteristic volatility = new Caracteristic(0, 0, 0);
    private Caracteristic skewness = new Caracteristic(0, 0, 0);
    private Caracteristic kurtosis = new Caracteristic(0, 0, 0);
    private Caracteristic AC1 = new Caracteristic(0, 0, 0);

    public static GroupeNot AAA = new GroupeNot("AAA");
    public static GroupeNot AA = new GroupeNot("AA");
    public static GroupeNot A = new GroupeNot("A");
    public static GroupeNot B = new GroupeNot("B");
    public static GroupeNot BB = new GroupeNot("BB");
    public static GroupeNot BBB = new GroupeNot("BBB");
    public static GroupeNot C = new GroupeNot("C");
    public static GroupeNot CC = new GroupeNot("CC");
    public static GroupeNot CCC = new GroupeNot("CCC");
    public static GroupeNot D = new GroupeNot("D");

    private GroupeNot(String n) {
        notations = new HashSet<Notation>();
        nom = n;
    }

    public void add(Notation n) {
        notations.add(n);
    }

    public Set<Notation> getNotations() {
        return notations;
    }

    public String getNom() {
        return nom;
    }

    public Caracteristic getMean() {
        return mean;
    }

    public Caracteristic getMedian() {
        return median;
    }

    public Caracteristic getMin() {
        return min;
    }

    public Caracteristic getMax() {
        return max;
    }

    public Caracteristic getVolatility() {
        return volatility;
    }

    public Caracteristic getSkewness() {
        return skewness;
    }

    public Caracteristic getKurtosis() {
        return kurtosis;
    }

    public Caracteristic getAC1() {
        return AC1;
    }

    public void calcul_moyenne(Caracteristic c) {
        Iterator it = this.getNotations().iterator();
        int compteur1y = 0, compteur5y = 0, compteur10y = 0;
        while (it.hasNext()) {
            Notation n = (Notation) it.next();
            if (n.getCDS_1Y() != -1) {
                c.set1y(c.get1y() + n.getCDS_1Y());
                compteur1y++;
            }
            if (n.getCDS_5Y() != -1) {
                c.set5y(c.get5y() + n.getCDS_5Y());
                compteur5y++;
            }
            if (n.getCDS_10Y() != -1) {
                c.set10y(c.get10y() + n.getCDS_10Y());
                compteur10y++;
            }
        }
        if (compteur1y != 0) {
            c.set1y(c.get1y() / compteur1y);
        }
        if (compteur5y != 0) {
            c.set5y(c.get5y() / compteur5y);
        }
        if (compteur10y != 0) {
            c.set10y(c.get10y() / compteur10y);
        }
    }

    public void calcul_median_min_max(Caracteristic med, Caracteristic min, Caracteristic max) {
        Notation[] arrayNot = this.getNotations().toArray(new Notation[this.getNotations().size()]);
        int taille = arrayNot.length;
        if (taille != 0) {
            int compt = Fonctions_calcul.triBulleCroissant(1, arrayNot);
            if (taille == compt) {
                med.set1y(0);
            } else if ((taille - compt) % 2 == 0) {
                med.set1y((arrayNot[compt + (taille - compt) / 2 - 1].getCDS_1Y() + arrayNot[compt + (taille - compt) / 2].getCDS_1Y()) / 2);
            } else {
                med.set1y(arrayNot[compt + (taille - compt) / 2].getCDS_1Y());
            }
            min.set1y(arrayNot[compt].getCDS_1Y());
            max.set1y(arrayNot[taille - 1].getCDS_1Y());

            compt = Fonctions_calcul.triBulleCroissant(5, arrayNot);
            if (taille == compt) {
                med.set5y(0);
            } else if ((taille - compt) % 2 == 0) {
                med.set5y((arrayNot[compt + (taille - compt) / 2 - 1].getCDS_5Y() + arrayNot[compt + (taille - compt) / 2].getCDS_5Y()) / 2);
            } else {
                med.set5y(arrayNot[compt + (taille - compt) / 2].getCDS_5Y());
            }
            min.set5y(arrayNot[compt].getCDS_5Y());
            max.set5y(arrayNot[taille - 1].getCDS_5Y());

            compt = Fonctions_calcul.triBulleCroissant(10, arrayNot);
            if (taille == compt) {
                med.set10y(0);
            } else if ((taille - compt) % 2 == 0) {
                med.set10y((arrayNot[compt + (taille - compt) / 2 - 1].getCDS_10Y() + arrayNot[compt + (taille - compt) / 2].getCDS_10Y()) / 2);
            } else {
                med.set10y(arrayNot[compt + (taille - compt) / 2].getCDS_10Y());
            }
            min.set10y(arrayNot[compt].getCDS_10Y());
            max.set10y(arrayNot[taille - 1].getCDS_10Y());
        }
    }

    public void calcul_vol_skew_kurt(Caracteristic vol, Caracteristic sk, Caracteristic kur, Caracteristic moy) {
        Iterator it = this.getNotations().iterator();
        int compteur1y = 0, compteur5y = 0, compteur10y = 0;
        while (it.hasNext()) {
            Notation n = (Notation) it.next();
            if (n.getCDS_1Y() != -1) {
                vol.set1y(vol.get1y() + pow((n.getCDS_1Y() - mean.get1y()), 2));
                sk.set1y(sk.get1y() + pow((n.getCDS_1Y() - mean.get1y()), 3));
                kur.set1y(kur.get1y() + pow((n.getCDS_1Y() - mean.get1y()), 4));
                compteur1y++;
            }
            if (n.getCDS_5Y() != -1) {
                vol.set5y(vol.get5y() + pow((n.getCDS_5Y() - mean.get5y()), 2));
                sk.set5y(sk.get5y() + pow((n.getCDS_5Y() - mean.get5y()), 3));
                kur.set5y(kur.get5y() + pow((n.getCDS_5Y() - mean.get5y()), 4));
                compteur5y++;
            }
            if (n.getCDS_10Y() != -1) {
                vol.set10y(vol.get10y() + pow((n.getCDS_10Y() - mean.get10y()), 2));
                sk.set10y(sk.get10y() + pow((n.getCDS_10Y() - mean.get10y()), 3));
                kur.set10y(kur.get10y() + pow((n.getCDS_10Y() - mean.get10y()), 4));
                compteur10y++;
            }
        }
        if (compteur1y != 0) {
            vol.set1y(sqrt(vol.get1y() / compteur1y));
            sk.set1y((sk.get1y() / compteur1y) / pow(vol.get1y(), 3));
            kur.set1y((kur.get1y() / compteur1y) / pow(vol.get1y(), 4));
        }
        if (compteur5y != 0) {
            vol.set5y(sqrt(vol.get5y() / compteur5y));
            sk.set5y((sk.get5y() / compteur5y) / pow(vol.get5y(), 3));
            kur.set5y((kur.get5y() / compteur5y) / pow(vol.get5y(), 4));
        }
        if (compteur10y != 0) {
            vol.set10y(sqrt(vol.get10y() / compteur10y));
            sk.set10y((sk.get10y() / compteur10y) / pow(vol.get10y(), 3));
            kur.set10y((kur.get10y() / compteur10y) / pow(vol.get10y(), 4));

        }
    }

    public void calcul_Table8() {
        if (notations.isEmpty()) {
            return;
        }
        this.calcul_moyenne(mean);
        this.calcul_median_min_max(median, min, max);
        calcul_vol_skew_kurt(volatility, skewness, kurtosis, mean);
        setAC1();
    }

    public static void calculAllTable8() {
        A.calcul_Table8();
        AA.calcul_Table8();
        AAA.calcul_Table8();
        B.calcul_Table8();
        BB.calcul_Table8();
        BBB.calcul_Table8();
        C.calcul_Table8();
        CC.calcul_Table8();
        CCC.calcul_Table8();
        D.calcul_Table8();
    }

    public double[] getAverageVector(int years) {
        TreeSet<Date> liste_date = new TreeSet<Date>();
        for (Notation n : notations) {
            if (!liste_date.contains(n.getDate()) && n.getCDS(years) != -1) {
                liste_date.add(n.getDate());
            }
        }
        int size = liste_date.size();
        double res[] = new double[size];
        Iterator<Date> itDate = liste_date.iterator();
        Date cour;
        for (int i = 0; i < size; i++) {
            int compteur = 0;
            cour = itDate.next();
            for (Notation n : notations) {
                if (cour.equals(n.getDate())) {
                    if (n.getCDS(years) != -1) {
                        res[i] += n.getCDS(years);
                        compteur++;
                    }

                }
            }
            res[i] = res[i] / (double) compteur;
        }
        return res;
    }

    public void setAC1() {
        for (int i = 1; i < 4; i++) {
            double VecteurTmp[] = getAverageVector(i);
            double matrix[][] = new double[VecteurTmp.length - 1][2];
            for (int j = 0; j < VecteurTmp.length - 1; j++) {
                matrix[j][0] = VecteurTmp[j + 1];
                matrix[j][1] = VecteurTmp[j];
            }
            SimpleRegression reg = new SimpleRegression();
            reg.addData(matrix);
            if (i == 1) {
                AC1.set1y(reg.getR());
            }
            if (i == 2) {
                AC1.set5y(reg.getR());
            }
            if (i == 3) {
                AC1.set10y(reg.getR());
            }
        }
    }
    
}
