/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsx_manager;

import java.util.List;
import java.util.ArrayList;
import souverain.Notation;

/**
 *
 * @author maubertj
 */
public class Pays {

    private int numero;
    private String nomPays;
    private List<Notation> notations;
    private Double Sensi30;
    private Double Sensi60;
    private Double NotMoy;
    private Double CDS5Y;
    private String date;

    public Pays(int num, String nom, String date) {
        numero = num;
        nomPays = nom;
        notations = new ArrayList<Notation>();
        this.date = date;
    }

    public void addDataNot(Notation myNotation) {
        notations.add(myNotation);
    }

    public void calcData() {
        double s30 = 0;
        double compteurS30 = 0;
        double s60 = 0;
        double compteurS60 = 0;
        double notMoy = 0;
        double compteurNotMoy = 0;
        double CDS5 = 0;
        double compteurCDS5 = 0;
        double cour = -1;
        for (Notation n : notations) {
            cour = n.getSensiMarche30();
            if (cour != -1) {
                s30 += cour;
                compteurS30++;
            }
            cour = n.getSensiMarche60();
            if (cour != -1) {
                s60 += cour;
                compteurS60++;
            }
            cour = n.getNotMoyenne();
            if (cour != -1) {
                notMoy += cour;
                compteurNotMoy++;
            }
            cour = n.getCDS_5Y();
            if (cour != -1) {
                CDS5 += cour;
                compteurCDS5++;
            }

        }
        if (compteurS30 != 0) {
            Sensi30 = s30 / compteurS30;
        } else {
            Sensi30 = (double) 0;
        }
        if (compteurS60 != 0) {
            Sensi60 = s60 / compteurS60;
        } else {
            Sensi60 = (double) 0;
        }
        if (compteurNotMoy != 0) {
            NotMoy = notMoy / compteurNotMoy;
        } else {
            NotMoy = (double) -1;
        }
        if (compteurCDS5 != 0) {
            CDS5Y = CDS5 / compteurCDS5;
        } else {
            CDS5Y = (double) -1;
        }
    }

    public String getNom() {
        return nomPays;
    }

    public int getNum() {
        return numero;
    }

    public double getCDS5YMois() {
        return CDS5Y;
    }

    public double getSensi60Mois() {
        return Sensi60;
    }

    public double getSensi30Mois() {
        return Sensi30;
    }

    public double getNotMoyMois() {
        return NotMoy;
    }
}
