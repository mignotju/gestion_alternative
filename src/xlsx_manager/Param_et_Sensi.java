/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsx_manager;

/**
 *
 * @author mignotju
 */
public class Param_et_Sensi {
    private double param1=-1;
    private double param2=-1;
    private double sensi30=-1;
    private double sensi60=-1;
    
    public Param_et_Sensi(double d1, double d2, double d3, double d4) {
        param1=d1;
        param2=d2;
        sensi30=d3;
        sensi60=d4;
    }
    
    public Param_et_Sensi(double d1, double d2) {
        param1=d1;
        param2=d2;
    }
    
    public double getParam1() {
        return param1;
    }
    
    public double getParam2() {
        return param2;
    }
    
    public void setParam1(double one) {
        param1=one;
    }
    
    public void setParam2(double two) {
        param2=two;
    }
    
    public void setSensi30 (double d) {
        sensi30=d;
    }
    
    public double getSensi30() {
        return sensi30;
    }
    
    public double getSensi60() {
        return sensi60;
    }
    
    public void setSensi60(double d) {
        sensi60=d;
    }
}
