package com.example.pn748_000.micinput;

import android.util.Log;

/**
 * Created by pn748_000 on 3/20/2016.
 */
public class FourierTransform {
    public static ComplexNumber[] calculateFT(double[] fx){
        int n=fx.length;
        ComplexNumber[] fk=new ComplexNumber[n];
        for(int i=0; i<n;i++){
            fk[i]=new ComplexNumber(0,0);
        }
        for(int k=0; k<n;k++){

            for(int t=0; t<n;t++){
                double angle=-2*Math.PI*t*k/n;
                fk[k].add(new ComplexNumber(angle, fx[t]));

            }


        }
        return fk;
    }
}
