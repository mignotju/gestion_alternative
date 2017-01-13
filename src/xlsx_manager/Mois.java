/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsx_manager;

import java.util.*;

/**
 *
 * @author maubertj
 */
public class Mois {
    
    
    public static Set<String> listeMois = new LinkedHashSet<String>();
    public static Map<String,Mois> dateMois = new TreeMap<String, Mois>();
    public Map<String,Pays> nomPaysPays; 
    private String moisAnnee;
    private Set<Pays> lesPays;
    
    public Mois(String date) {
        moisAnnee = date;
        lesPays = new LinkedHashSet<Pays>();
        nomPaysPays = new TreeMap<String, Pays>();
    }
    
    public void addPays (Pays pays,String nomDePays) {
        lesPays.add(pays);
        nomPaysPays.put(nomDePays, pays);
    }
    
    public Pays getPays(String pays){
        return nomPaysPays.get(pays);
    }
    
    public Set<Pays> getLesPays () {
        return lesPays;
    }
    
    public String getMois() {
        return moisAnnee;
    }
}
