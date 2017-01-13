/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souverain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mignotju
 */
public class Fonctions_calcul {

    // l'entier cds vaut 1 si on veut trier les notations par CDS_1Y croissant, 5 pour CDS_5Y, 10 pour CDS_10Y
    public static int triBulleCroissant(int cds, Notation tableau[]) {
        int longueur = tableau.length;
        int compteur_CDS1y_null = 0,compteur_CDS5y_null = 0,compteur_CDS10y_null = 0;
        Notation tampon;
        boolean permut;
        boolean echange = false;

        do {
            // hypothèse : le tableau est trié
            permut = false;
            for (int i = 0; i < longueur - 1; i++) {
                // Teste si 2 éléments successifs sont dans le bon ordre ou non
                if (cds == 1) {
                    echange = tableau[i].getCDS_1Y() > tableau[i + 1].getCDS_1Y();
                } else if (cds == 5) {
                    echange = tableau[i].getCDS_5Y() > tableau[i + 1].getCDS_5Y();
                } else if (cds == 10) {
                    echange = tableau[i].getCDS_10Y() > tableau[i + 1].getCDS_10Y();
                } else {
                    System.err.println("Sur quelle critere voulez-vous trier votre tableau ? \n");
                }

                if (echange) {
                    // s'ils ne le sont pas, on échange leurs positions
                    tampon = tableau[i];
                    tableau[i] = tableau[i + 1];
                    tableau[i + 1] = tampon;
                    permut = true;
                }
            }
        } while (permut);

        for (int i = 0; i < longueur - 1; i++) {
            if (cds == 1) {
                if (tableau[i].getCDS_1Y() == -1) {
                    compteur_CDS1y_null++;
                } else {
                    return compteur_CDS1y_null;
                }
            } else if (cds==5) {
                if (tableau[i].getCDS_5Y() == -1) {
                    compteur_CDS5y_null++;
                } else {
                    return compteur_CDS5y_null;
                }
            } else if (cds== 10) {
                if (tableau[i].getCDS_10Y() == -1) {
                    compteur_CDS10y_null++;
                } else {
                    return compteur_CDS10y_null;
                }
            } else {
                System.err.println("Probleme de tri de tableau !");
            }
        }
        return 0;
    }
}
