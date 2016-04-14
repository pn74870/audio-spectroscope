package com.example.pn748_000.micinput;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

/**
 * Created by pn748_000 on 3/20/2016.
 */

public class MainActivity extends AppCompatActivity implements TaskFragment.TaskCallbacks {
    private static final double sensitivity=1.0;
    private static final String FRAGMENT_TAG = "tag1";
    private static final String ARG_IS_RECORDING ="is recording";
    private boolean isRecording = false;
    private static final int frequency = 8000;
    private static final int blockSize = 256;
    private ProgressBar[] bars;
    private TextView[] freqTexts;
    private TaskFragment fragment;
    private TextView text;
    private boolean messageSent=false,event1=false,event2=false;
    private int numberOfReceived1=0,numberOFReceived2=0;
    private double []  dialFrequencies={697,770,852, 941,1209,1336,1477,1633};
    private int [] dialIndeces;
    private EditText messageText,numberText;
    public static boolean wasStopped=false;
    private GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.spectrum);
        if(savedInstanceState!=null) isRecording =savedInstanceState.getBoolean(ARG_IS_RECORDING);
       /* configureProgressBars();
        text = (TextView) findViewById(R.id.text);
        messageText= (EditText) findViewById(R.id.messageTxt);
        numberText= (EditText) findViewById(R.id.numberTxt);*/
        FragmentManager fm = getSupportFragmentManager();
        fragment = (TaskFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = TaskFragment.newInstance(frequency, blockSize);
            fm.beginTransaction().add(fragment, FRAGMENT_TAG).commit();
        }
        else fragment.callbacks=this;
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    button.setText("START");
                    wasStopped=true;
                    isRecording = false;
                } else {
                    isRecording = true;
                    button.setText("STOP");
                }
                fragment.switchTask(isRecording);
            }
        });
       // dialIndeces=indexOfDialTones(); //finds index of dial frequencies
        graphView= (GraphView) findViewById(R.id.graph);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_RECORDING, isRecording);
    }

    private void configureProgressBars() {
        View[] views = new View[]{
                findViewById(R.id.bar1),
                findViewById(R.id.bar2),
                findViewById(R.id.bar3),
                findViewById(R.id.bar4),
                findViewById(R.id.bar5),
                findViewById(R.id.bar6),
                findViewById(R.id.bar7),
                findViewById(R.id.bar8),
               // findViewById(R.id.bar9),
                //findViewById(R.id.bar10)
        };
        bars = new ProgressBar[views.length];
        freqTexts = new TextView[views.length];
        for (int i = 0; i < views.length; i++) {
            bars[i] = (ProgressBar) views[i].findViewById(R.id.bar);
            freqTexts[i] = (TextView) views[i].findViewById(R.id.freq_text);
        }
        for (ProgressBar bar : bars)
            bar.setMax(1000);
    }


    private void onFrequencyReceived(int numb) {
       // messageSent=true;
        Log.e("asd","freqReceived");
        String number = numberText.getText().toString();
        String msg=messageText.getText().toString();

        numberOfReceived1+=numb==1?1:0;
        numberOFReceived2+=numb==2?1:0;
       if(numb==2){
           if(number.equals("")) Toast.makeText(this,"Enter a number",Toast.LENGTH_SHORT).show();
           else {
               SmsManager.getDefault().sendTextMessage(number, null, msg, null, null);
               Toast.makeText(this, "Message was sent", Toast.LENGTH_SHORT).show();
           }
       }
       text.setText(String.format("number of 1 events: %d\n number of 2 events: %d", numberOfReceived1,numberOFReceived2));

    }

    @Override
    public void progressUpdate(double[] values) {
      /*  int j;
        for (int i = 0; i < bars.length; i++) {
            j = i * values.length /8/ bars.length;
           bars[i].setProgress((int) (values[j] * 1000));
            freqTexts[i].setText(frequency * j / blockSize + " Hz");
           if (i == 5) {
                //text.setText(values[j]+"");
                if (values[j] > sensitivity && !messageSent) onFrequencyReceived();
                if(messageSent && values[j]<sensitivity/5) messageSent=false;
            }


        }*/

        graphView.setData(values,1,frequency);
//---------------------------------------------------------------- this block is for dial tones
    /*    for(int i=0;i<dialIndeces.length;i++){
            bars[i].setProgress((int) values[dialIndeces[i]] * 1000);
            freqTexts[i].setText(String.format("%d Hz", (int) ((double) frequency * dialIndeces[i] / blockSize)));
        }
        if(!event1&&checkDial(values,dialIndeces[0],dialIndeces[6])){
            onFrequencyReceived(1);
            event1=true;}
        if(!event2&&checkDial(values,dialIndeces[1],dialIndeces[6])) {
            onFrequencyReceived(2);
            event2=true;
        }
        if(values[dialIndeces[0]]<sensitivity && values[dialIndeces[6]]<sensitivity && event1) event1=false;
        if(values[dialIndeces[1]]<sensitivity && values[dialIndeces[6]]<sensitivity && event2) event2=false;*/
//---------------------------------------------------------------

    /*    for(int i=0;i<dial.length;i++){
            j = dial[i];
            bars[i].setProgress((int) (values[j] * 1000));
            freqTexts[i].setText(frequency * j / blockSize + " Hz");
            if(values[j]>sensitivity)
                text.setText(frequency * j / blockSize + " Hz");
        }
        //text.setText("Max: "+frequency * indexOfMax(values)/ blockSize+" Hz");
*/
    }

    private boolean checkDial(double[] values, int index1, int index2){
        boolean noOtherFreq=true;
        for(int i=0;i<values.length/2;i++){
            if(Math.abs(i-index1)>1 && Math.abs(i-index2)>1 &&values[i]>sensitivity)
                noOtherFreq=false;
           // if(values[index1]>sensitivity && values[index2]>sensitivity)
            //    Log.e("asd",String.format("%d Hz %.5f",(int)((double)frequency/blockSize*i),values[i]));
        }
        return noOtherFreq && values[index1]>sensitivity && values[index2]>sensitivity;

    }

    private int indexOfMax(double [] array){
        int max=0;
        for(int i=1;i<array.length;i++){
            if(array[i]>array[max]) max=i;
        }
        return max;
    }
    private int[] indexOfDialTones(){
        int [] indices=new int[dialFrequencies.length];
        int indexOfClosest;
        double freq,diff,diffBest;
        for(int j=0;j<dialFrequencies.length;j++){
            indexOfClosest=0;
            for(int i=1;i<blockSize;i++){
               freq=(double)frequency/blockSize*i;
               diff=difference(freq,dialFrequencies[j]);
               diffBest=difference((double)frequency/blockSize*indexOfClosest,dialFrequencies[j]);

                if(diff<diffBest){
                    indexOfClosest=i;

                }
        }

            indices[j]=indexOfClosest;
    }
    return indices;
    }
    private static double difference(double a , double b){
        return Math.abs(a-b);
    }
}
