/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souverain;

import java.util.Iterator;

/**
 *
 * @author mignotju
 */
public class Caracteristic {

    private double _1Y, _5Y, _10Y;

    public Caracteristic(double y1, double y5, double y10) {
        _1Y = y1;
        _5Y = y5;
        _10Y = y10;
    }

    public void set1y(double m) {
        _1Y = m;
    }

    public void set5y(double m) {
        _5Y = m;
    }

    public void set10y(double m) {
        _10Y = m;
    }

    public void set(double y1, double y5, double y10) {
        _1Y = y1;
        _5Y = y5;
        _10Y = y10;
    }

    public double get1y() {
        return _1Y;
    }

    public double get5y() {
        return _5Y;
    }

    public double get10y() {
        return _10Y;
    }

    public double get(int cds) {
        if (cds == 1) {
            return _1Y;
        } else if (cds == 2) {
            return _5Y;
        } else if (cds == 3) {
            return _10Y;
        } else {
            System.err.println("Annee invalide");
            return -1;
        }
    }
    
    public void dispCaracteristic(){
        System.out.println("1Y : "+ _1Y);
        System.out.println("5Y : "+ _5Y);
        System.out.println("10Y : "+ _10Y);
    }

    
}
