package com.example.pn748_000.micinput;

import android.util.Log;

/**
 * Created by pn748_000 on 3/20/2016.
 */
public class FourierTransform {
    public static ComplexNumber[] calculateFT(double[] fx){ //simple sum algorithm
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
    public static ComplexNumber[] calculateFFT(double[] fx){ //uses fft recursion
        int n=fx.length;
        if(n==1){
            return new ComplexNumber[]{new ComplexNumber(0,fx[0])};
        }
        double[] evenPart=new double[n/2],oddPart=new double[n/2];
            for(int i=0; i<n;i+=2){
                evenPart[i/2]=fx[i];
                oddPart[i/2]=fx[i+1];
            }

        ComplexNumber[] ans=new ComplexNumber[n],evenFT=calculateFFT(evenPart),oddFT=calculateFFT(oddPart);
        for(int i=0;i<n/2;i++){
            ComplexNumber twiddle=new ComplexNumber(-2*Math.PI*i/(double)n,1);
            oddFT[i].multiply(twiddle);
            ans[i]=new ComplexNumber(evenFT[i]);
            ans[i+n/2]=new ComplexNumber(evenFT[i]);
            ans[i].add(oddFT[i]);
            ans[i+n/2].subtract(oddFT[i]);
        }
        return ans;
        }
    private static int reverseBits(int x, int bits){
        int shiftedX=x,count=bits-1;
        for(x>>=1; x>0;x>>=1){
            count--;
            shiftedX<<=1;
            shiftedX|=(x&1);

        }
        shiftedX<<=count;
        return shiftedX&((1<<bits)-1);
    }
    private static void swap(ComplexNumber[] array,int i, int j ){
        ComplexNumber temp=array[i];
        array[i]=array[j];
        array[j]=temp;
    }
    public static void calcFFT(ComplexNumber[] signal){// Cooley-Tukey fft
        int n=signal.length,bits=(int)(Math.log(n)/Math.log(2)),iReversed,iNot,iReversedNot,iMax=n-1;
      /*  ComplexNumber[] twiddleFactors=new ComplexNumber[n];
        for(int i=0; i<n;i++){
            twiddleFactors[i]=new ComplexNumber(-2*Math.PI/n*i,1);
        }*/
        if(n!=(1<<bits)) throw new RuntimeException("signal's length has to be a power of 2");
        for(int i=0;i<n/2;i++){
            iReversed=reverseBits(i,bits);
            if(i<iReversed){
               swap(signal,i,iReversed);
                iNot=i^iMax;
                iReversedNot=iReversed^iMax;
                swap(signal,iReversedNot,iNot);
            }
            i++;
            iReversed|=n>>1;
            swap(signal,i,iReversed);
        }

        for (int numberOfFTs=n; numberOfFTs>=1; numberOfFTs/=2){
            for(int i=0;i<numberOfFTs;i++){
                int period=n/numberOfFTs;
                for(int k=0;k<period/2;k++){
                    ComplexNumber even=signal[i*period+k],odd=signal[i*period+k+period/2];
                    ComplexNumber twiddle=new ComplexNumber(-2*Math.PI/period*k,1);
                    twiddle.multiply(odd);
                    signal[i*period+k]=ComplexNumber.sum(even,twiddle);
                    signal[i*period+k+period/2]=ComplexNumber.difference(even,twiddle);

                }
            }
        }
    }
}
