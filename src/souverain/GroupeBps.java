/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souverain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** 
 *
 * @author kamowskn
 */
public class GroupeBps {

    private String name;
    private Set<Notation> notations;
    private Caracteristic averageCDS = new Caracteristic(0, 0, 0);
    private Caracteristic averageSlope = new Caracteristic(0, 0, 0);
    //_1Y = 5_1;
    //_5Y = 10_5;
    //_10Y = 10_1;
    
    private int curves;
    private int entities;

    public static GroupeBps _1_100 = new GroupeBps("1-100");
    public static GroupeBps _100_200 = new GroupeBps("100-200");
    public static GroupeBps _200_300 = new GroupeBps("200-300");
    public static GroupeBps _300_400 = new GroupeBps("300-400");
    public static GroupeBps _400_500 = new GroupeBps("400-500");
    public static GroupeBps _500_600 = new GroupeBps("500-600");
    public static GroupeBps _600_700 = new GroupeBps("600-700");
    public static GroupeBps _700_800 = new GroupeBps("700-800");
    public static GroupeBps _800_900 = new GroupeBps("800-900");
    public static GroupeBps _900_1000 = new GroupeBps("900-1000");
    public static GroupeBps _1000_1100 = new GroupeBps("1000-1100");
    public static GroupeBps _1100_1200 = new GroupeBps("1100-1200");
    public static GroupeBps _1200_1300 = new GroupeBps("1200-1300");
    public static GroupeBps _1300_1400 = new GroupeBps("1300-1400");
    public static GroupeBps _1400_1500 = new GroupeBps("1400-1500");
    public static GroupeBps _1500_1700 = new GroupeBps("1500-1700");
    public static GroupeBps _1700_1900 = new GroupeBps("1700-1900");
    public static GroupeBps _1900_2100 = new GroupeBps("1900-2100");
    public static GroupeBps _2100_2300 = new GroupeBps("2100-2300");
    public static GroupeBps _2300_2500 = new GroupeBps("2300-2500");
    public static GroupeBps _2500_2700 = new GroupeBps("2500-2700");
    public static GroupeBps _2700_2900 = new GroupeBps("2700-2900");
    public static GroupeBps _2900_3100 = new GroupeBps("2900-3100");
    public static GroupeBps _3100_3300 = new GroupeBps("3100-3300");
    public static GroupeBps _3300_3500 = new GroupeBps("3300-3500");
    public static GroupeBps _3500_3700 = new GroupeBps("3500-3700");
    public static GroupeBps _3700_3900 = new GroupeBps("3700-3900");
    public static GroupeBps _3900_4100 = new GroupeBps("3900-4100");
    public static GroupeBps _4100_Plus = new GroupeBps("4100-Plus");

    public GroupeBps(String s) {
        name = s;
        notations = new HashSet<Notation>();
    }

    public void add(Notation p) {
        notations.add(p);
    }

    public String getName() {
        return name;
    }

    public int getCurves() {
        return curves;
    }
    
    public int getEntities(){
        return entities;
    }
    
    public Caracteristic getAverageCDS() {
        return averageCDS;
    }

    public Caracteristic getAverageSlope() {
        return averageSlope;
    }

    public void calculMoyenneCDS() {
        Iterator it = this.notations.iterator();
        int compteur = 0, compteur1y = 0, compteur5y =0, compteur10y =0;
        while (it.hasNext()) {
            compteur++;
            Notation n = (Notation) it.next();
            if (n.getCDS_1Y() != -1) {
                averageCDS.set1y(averageCDS.get1y() + n.getCDS_1Y());
                compteur1y++;
            }
            if (n.getCDS_5Y() != -1) {
                averageCDS.set5y(averageCDS.get5y() + n.getCDS_5Y());
                compteur5y++;
            }
            if (n.getCDS_10Y() != -1) {
                averageCDS.set10y(averageCDS.get10y() + n.getCDS_10Y());
                compteur10y++;
            }
        }
        averageCDS.set1y(averageCDS.get1y() / compteur1y);
        averageCDS.set5y(averageCDS.get5y() / compteur5y);
        averageCDS.set10y(averageCDS.get10y() / compteur10y);
        curves = compteur;
    }

    public void calculEntities() {
        HashSet<String> listPays = new HashSet<String>();
        Iterator<Notation> it = notations.iterator();
        while (it.hasNext()) {
            listPays.add(it.next().getPays());
        }
        entities = listPays.size();
    }
    
    public void calculSlope() {
        double res5_1 = (averageCDS.get5y()-averageCDS.get1y());
        averageSlope.set1y(res5_1);
        double res10_1 = (averageCDS.get10y()-averageCDS.get1y());
        averageSlope.set10y(res10_1);
        double res10_5 = (averageCDS.get10y()-averageCDS.get5y());
        averageSlope.set5y(res10_5);
    }
    

    public void calcul_Table9() {
        //cette fonction calcule également la curve
        calculMoyenneCDS();
        calculEntities();
        calculSlope();
    }
    
    
    public static void calculAllTable9() {
        _1_100.calcul_Table9();
        _100_200.calcul_Table9();
        _200_300.calcul_Table9();
        _300_400.calcul_Table9();
        _400_500.calcul_Table9();
        _500_600.calcul_Table9();
        _600_700.calcul_Table9();
        _700_800.calcul_Table9();
        _800_900.calcul_Table9();
        _900_1000.calcul_Table9();
        _1000_1100.calcul_Table9();
        _1100_1200.calcul_Table9();
        _1200_1300.calcul_Table9();
        _1300_1400.calcul_Table9();
        _1400_1500.calcul_Table9();
        _1500_1700.calcul_Table9();
        _1700_1900.calcul_Table9();
        _1900_2100.calcul_Table9();
        _2100_2300.calcul_Table9();
        _2300_2500.calcul_Table9();
        _2500_2700.calcul_Table9();
        _2700_2900.calcul_Table9();
        _2900_3100.calcul_Table9();
        _3100_3300.calcul_Table9();
        _3300_3500.calcul_Table9();
        _3500_3700.calcul_Table9();
        _3900_4100.calcul_Table9();
        _3700_3900.calcul_Table9();
        _4100_Plus.calcul_Table9();
    }

}
