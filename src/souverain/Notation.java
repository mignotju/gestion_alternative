/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souverain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import xlsx_manager.Parser;

/**
 *
 * @author kamowskn
 */
public class Notation {

    private static Set<String> listPaysAAA = new HashSet<String>();
    private double CDS_1Y, CDS_5Y, CDS_10Y;
    private String Fitch, SP, Moodys;
    private String pays;
    private Date date;
    private double notMoyenne = -1;
    private double sensi_marche30 = -1;
    private double sensi_marche60 = -1;
    private String group ="";

    public Notation(double y1, double y5, double y10, String f, String sp, String m, String pays, Date d) {
        CDS_1Y = y1;
        CDS_5Y = y5;
        CDS_10Y = y10;
        Fitch = f;
        SP = sp;
        Moodys = m;
        this.pays = pays;
        date = d;
    }
    

    public String getGroup() {
        return group;
    }
    
    public double getSensiMarche30() {
        return sensi_marche30;
    }
    
    public void setSensiMarche30(double sensi) {
        sensi_marche30=sensi;
    }
    
   public double getSensiMarche60() {
        return sensi_marche60;
    }
    
    public void setSensiMarche60(double sensi) {
        sensi_marche60=sensi;
    }
    
    public double getNotMoyenne(){
        return notMoyenne;
    }
    
    public String getPays() {
        return pays;
    }

    public double getCDS_1Y() {
        return CDS_1Y;
    }

    public double getCDS_5Y() {
        return CDS_5Y;
    }

    public double getCDS_10Y() {
        return CDS_10Y;
    }

    public double getCDS(int i) {
        if (i == 1) {
            return CDS_1Y;
        }
        if (i == 2) {
            return CDS_5Y;
        }
        if (i == 3) {
            return CDS_10Y;
        }
        return 0;
    }

    public Date getDate() {
        return date;
    }

    public void dispNotation() {
        System.out.println(date);
        System.out.println(pays);
        System.out.println(CDS_5Y);
        System.out.println(CDS_1Y);
        System.out.println(CDS_10Y);
        System.out.println(Fitch);
        System.out.println(SP);
        System.out.println(Moodys);
    }

    private double getFitchDouble() {
        double res = 0;
        switch (Fitch) {
            case "AAA":
                res += 23;
                break;
            case "AA+":
                res += 22;
                break;
            case "AA":
                res += 21;
                break;
            case "AA-":
                res += 20;
                break;
            case "A+":
                res += 19;
                break;
            case "A":
                res += 18;
                break;
            case "A-":
                res += 17;
                break;
            case "BBB+":
                res += 16;
                break;
            case "BBB":
                res += 15;
                break;
            case "BBB-":
                res += 14;
                break;
            case "BB+":
                res += 13;
                break;
            case "BB":
                res += 12;
                break;
            case "BB-":
                res += 11;
                break;
            case "B+":
                res += 10;
                break;
            case "B":
                res += 9;
                break;
            case "B-":
                res += 8;
                break;
            case "CCC":
                res += 6;
                break;
            case "CC":
                res += 4;
                break;
            case "C":
                res += 3;
                break;
            case "RD":
                res += 2;
                break;
            case "D":
                res += 1;
                break;
            default:
                res = -1;
                break;
        }

        return res;
    }

    private double getSPDouble() {
        double res = 0;
        switch (SP) {
            case "AAA":
                res += 23;
                break;
            case "AA+":
                res += 22;
                break;
            case "AA":
                res += 21;
                break;
            case "AA-":
                res += 20;
                break;
            case "A+":
                res += 19;
                break;
            case "A":
                res += 18;
                break;
            case "A-":
                res += 17;
                break;
            case "BBB+":
                res += 16;
                break;
            case "BBB":
                res += 15;
                break;
            case "BBB-":
                res += 14;
                break;
            case "BB+":
                res += 13;
                break;
            case "BB":
                res += 12;
                break;
            case "BB-":
                res += 11;
                break;
            case "B+":
                res += 10;
                break;
            case "B":
                res += 9;
                break;
            case "B-":
                res += 8;
                break;
            case "CCC+":
                res += 7;
                break;
            case "CCC":
                res += 6;
                break;
            case "CCC-":
                res += 5;
                break;
            case "CC":
                res += 4;
                break;
            case "C":
            case "CI":
            case "R":
                res += 3;
                break;
            case "SD":
                res += 2;
                break;
            case "D":
                res += 1;
                break;
            default:
                res = -1;
                break;
        }

        return res;
    }

    private double getMoodysDouble() {
        double res = 0;
        switch (Moodys) {
            case "Aaa":
                res += 23;
                break;
            case "Aa1":
                res += 22;
                break;
            case "Aa2":
                res += 21;
                break;
            case "Aa3":
                res += 20;
                break;
            case "A1":
                res += 19;
                break;
            case "A2":
                res += 18;
                break;
            case "A3":
                res += 17;
                break;
            case "Baa1":
                res += 16;
                break;
            case "Baa2":
                res += 15;
                break;
            case "Baa3":
                res += 14;
                break;
            case "Ba1":
                res += 13;
                break;
            case "Ba2":
                res += 12;
                break;
            case "Ba3":
                res += 11;
                break;
            case "B1":
                res += 10;
                break;
            case "B2":
                res += 9;
                break;
            case "B3":
                res += 8;
                break;
            case "Caa1":
                res += 7;
                break;
            case "Caa2":
                res += 6;
                break;
            case "Caa3":
                res += 5;
                break;
            case "Ca":
                res += 4;
                break;
            case "C":
                res += 2;
                break;
            default:
                res = -1;
                break;
        }

        return res;
    }

    private static boolean isBetween(double x, double upper, double lower) {
        return lower <= x && x < upper;
    }

//    public void assignGroupeNot() {
    public String assignGroupeNot(boolean movingFromAAA) {
        String res = "";
        double FitchDouble = getFitchDouble();
        double SPDouble = getSPDouble();
        double moodysDouble = getMoodysDouble();
        int compteurAAA = 0;
        //Calcul de la moyenne        
        double moyenne = 0;
        int compteurMoyenne = 0;
        if (FitchDouble != -1) {
            moyenne += FitchDouble;
            compteurMoyenne++;
        }
        if (SPDouble != -1) {
            moyenne += SPDouble;
            compteurMoyenne++;
        }
        if (moodysDouble != -1) {
            moyenne += moodysDouble;
            compteurMoyenne++;
        }
        if (compteurMoyenne != 0) {
            moyenne = moyenne / compteurMoyenne;
        } else {
            System.out.println("les 3 notations sont nulles");
        }
        notMoyenne = moyenne;
        if (movingFromAAA) {
            if (!isBetween(moyenne, 24, 22.5)) {
                if (listPaysAAA.contains(pays)) {
                    res += pays + " sort du groupe AAA le : " + date + "\n";
                    listPaysAAA.remove(pays);
                }
            }
        }
        if (isBetween(moyenne, 24, 22.5)) {
            GroupeNot.AAA.add(this);
            group = "AAA";
            if (movingFromAAA) {
                compteurAAA = listPaysAAA.size();
                listPaysAAA.add(this.pays);
                if ((compteurAAA != listPaysAAA.size()) && !(date.equals(Parser.premiereDate))) {
                    res += pays + " rentre dans le groupe AAA le : " + date + "\n";

                }
            }
        } else if (isBetween(moyenne, 22.5, 19.5)) {
            GroupeNot.AA.add(this);
            group = "AA";
        } else if (isBetween(moyenne, 19.5, 16.5)) {
            GroupeNot.A.add(this);
            group= "A";
        } else if (isBetween(moyenne, 16.5, 13.5)) {
            GroupeNot.BBB.add(this);
            group= "BBB";
        } else if (isBetween(moyenne, 13.5, 10.5)) {
            GroupeNot.BB.add(this);
            group= "BB";
        } else if (isBetween(moyenne, 10.5, 7.5)) {
            GroupeNot.B.add(this);
            group= "B";
        } else if (isBetween(moyenne, 7.5, 4.5)) {
            GroupeNot.CCC.add(this);
            group= "CCC";
        } else if (isBetween(moyenne, 4.5, 3.5)) {
            GroupeNot.CC.add(this);
            group= "CC";
        } else if (isBetween(moyenne, 3.5, 2.5)) {
            GroupeNot.C.add(this);
            group= "C";
        } else if (isBetween(moyenne, 2.5, 1)) {
            GroupeNot.D.add(this);
            group= "D";
        } else {
            System.out.println("il y a une erreur dans le parsing de la moyenne qui vaut : " + moyenne);
        }
        return res;
    }

    public void assignGroupeBps() {

        if (isBetween(this.CDS_5Y, 100, 1)) {
            GroupeBps._1_100.add(this);
        } else if (isBetween(this.CDS_5Y, 200, 100)) {
            GroupeBps._100_200.add(this);
        } else if (isBetween(this.CDS_5Y, 300, 200)) {
            GroupeBps._200_300.add(this);
        } else if (isBetween(this.CDS_5Y, 400, 300)) {
            GroupeBps._300_400.add(this);
        } else if (isBetween(this.CDS_5Y, 500, 400)) {
            GroupeBps._400_500.add(this);
        } else if (isBetween(this.CDS_5Y, 600, 500)) {
            GroupeBps._500_600.add(this);
        } else if (isBetween(this.CDS_5Y, 700, 600)) {
            GroupeBps._600_700.add(this);
        } else if (isBetween(this.CDS_5Y, 800, 700)) {
            GroupeBps._700_800.add(this);
        } else if (isBetween(this.CDS_5Y, 900, 800)) {
            GroupeBps._800_900.add(this);
        } else if (isBetween(this.CDS_5Y, 1000, 900)) {
            GroupeBps._900_1000.add(this);
        } else if (isBetween(this.CDS_5Y, 1100, 1000)) {
            GroupeBps._1000_1100.add(this);
        } else if (isBetween(this.CDS_5Y, 1200, 1100)) {
            GroupeBps._1100_1200.add(this);
        } else if (isBetween(this.CDS_5Y, 1300, 1200)) {
            GroupeBps._1200_1300.add(this);
        } else if (isBetween(this.CDS_5Y, 1400, 1300)) {
            GroupeBps._1300_1400.add(this);
        } else if (isBetween(this.CDS_5Y, 1500, 1400)) {
            GroupeBps._1400_1500.add(this);
        } else if (isBetween(this.CDS_5Y, 1700, 1500)) {
            GroupeBps._1500_1700.add(this);
        } else if (isBetween(this.CDS_5Y, 1900, 1700)) {
            GroupeBps._1700_1900.add(this);
        } else if (isBetween(this.CDS_5Y, 2100, 1900)) {
            GroupeBps._1900_2100.add(this);
        } else if (isBetween(this.CDS_5Y, 2300, 2100)) {
            GroupeBps._2100_2300.add(this);
        } else if (isBetween(this.CDS_5Y, 2500, 2300)) {
            GroupeBps._2300_2500.add(this);
        } else if (isBetween(this.CDS_5Y, 2700, 2500)) {
            GroupeBps._2500_2700.add(this);
        } else if (isBetween(this.CDS_5Y, 2900, 2700)) {
            GroupeBps._2700_2900.add(this);
        } else if (isBetween(this.CDS_5Y, 3100, 2900)) {
            GroupeBps._2900_3100.add(this);
        } else if (isBetween(this.CDS_5Y, 3300, 3100)) {
            GroupeBps._3100_3300.add(this);
        } else if (isBetween(this.CDS_5Y, 3500, 3300)) {
            GroupeBps._3300_3500.add(this);
        } else if (isBetween(this.CDS_5Y, 3700, 3500)) {
            GroupeBps._3500_3700.add(this);
        } else if (isBetween(this.CDS_5Y, 3900, 3700)) {
            GroupeBps._3700_3900.add(this);
        } else if (isBetween(this.CDS_5Y, 4100, 3900)) {
            GroupeBps._3900_4100.add(this);
        } else if (isBetween(this.CDS_5Y, Double.MAX_VALUE, 4100)) {
            GroupeBps._4100_Plus.add(this);
        } else {
            //CDS5y = -1 (pas de valeur) on ne le range pas.
        }
    }
    
    
}
