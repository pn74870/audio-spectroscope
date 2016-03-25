package com.example.pn748_000.micinput;

/**
 * Created by pn748_000 on 3/20/2016.
 */
public class ComplexNumber {
    public double realPart,imaginaryPart;
  /*  public ComplexNumber(double x, double y){
        realPart=x;
        imaginaryPart=y;
    }*/
    public ComplexNumber( double angle,double radius){
        realPart=radius*Math.cos(angle);
        imaginaryPart=radius*Math.sin(angle);
    }
    private ComplexNumber(double real, double imaginary,boolean b){
        realPart=real;
        imaginaryPart=imaginary;
    }
    public ComplexNumber(ComplexNumber c){
        realPart=c.realPart;
        imaginaryPart=c.imaginaryPart;
    }
    public void add(ComplexNumber c){
        realPart+=c.realPart;
        imaginaryPart+=c.imaginaryPart;
    }
    public void subtract(ComplexNumber c){
        realPart-=c.realPart;
        imaginaryPart-=c.imaginaryPart;
    }
    public double modulusSq(){
        return realPart*realPart+imaginaryPart*imaginaryPart;
    }
    public void multiply(ComplexNumber c){
        double temp=realPart;
        realPart=realPart*c.realPart-imaginaryPart*c.imaginaryPart;
        imaginaryPart=imaginaryPart*c.realPart+temp*c.imaginaryPart;
    }
    public static ComplexNumber[] createRealArray(double[] real){
        ComplexNumber[] complexNumbers=new ComplexNumber[real.length];
        for (int i=0;i<real.length;i++) {
            complexNumbers[i]=new ComplexNumber(0,real[i]);
        }
        return complexNumbers;
    }
    public static ComplexNumber sum(ComplexNumber a,ComplexNumber b){
        return new ComplexNumber(a.realPart+b.realPart,a.imaginaryPart+b.imaginaryPart,false);
    }
    public static ComplexNumber difference(ComplexNumber a, ComplexNumber b){
        return new ComplexNumber(a.realPart-b.realPart,a.imaginaryPart-b.imaginaryPart,false);
    }
}
