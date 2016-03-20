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
    public void add(ComplexNumber c){
        realPart+=c.realPart;
        imaginaryPart+=c.imaginaryPart;
    }
    public double modulusSq(){
        return realPart*realPart+imaginaryPart*imaginaryPart;
    }

}
