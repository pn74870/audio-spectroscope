package com.example.pn748_000.micinput;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by pn748_000 on 3/20/2016.
 */
public class MainActivity extends AppCompatActivity implements TaskFragment.TaskCallbacks {
    private static final double sensitivity=.7;
    private static final String FRAGMENT_TAG = "tag1";
    private boolean isRecordering = false;
    private static final int frequency = 8000;
    private static final int blockSize = 256;
    private ProgressBar[] bars;
    private TextView[] freqTexts;
    private TaskFragment fragment;
    private TextView text;
    private boolean messageSent=false;
    private int numberOfReceived=0;
    private int [] dial={22,25,27};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        configureProgressBars();
        FragmentManager fm = getSupportFragmentManager();
        fragment = (TaskFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = TaskFragment.newInstance(frequency, blockSize);
            fm.beginTransaction().add(fragment, FRAGMENT_TAG).commit();
        }
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecordering) {
                    button.setText("START");
                    isRecordering = false;
                } else {
                    isRecordering = true;
                    button.setText("STOP");
                }
                fragment.switchTask(isRecordering);
            }
        });


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
                findViewById(R.id.bar9),
                findViewById(R.id.bar10)
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


    private void onFrequencyReceived() {
        messageSent=true;
        Log.e("asd","freqReceived");
        String number = "863754345";
        String msg="!?";
        SmsManager sm = SmsManager.getDefault();
       // sm.sendTextMessage(number, null, msg, null, null);
       text.setText("number of events: "+ ++numberOfReceived);
    }

    @Override
    public void progressUpdate(double[] values) {
        int j;
       /* for (int i = 0; i < bars.length; i++) {
            j = i * values.length /8/ bars.length;
           bars[i].setProgress((int) (values[j] * 1000));
            freqTexts[i].setText(frequency * j / blockSize + " Hz");
           if (i == 5) {
                //text.setText(values[j]+"");
                if (values[j] > sensitivity && !messageSent) onFrequencyReceived();
                if(messageSent && values[j]<sensitivity/5) messageSent=false;
            }


        }*/
        for(int i=0;i<dial.length;i++){
            j = dial[i];
            bars[i].setProgress((int) (values[j] * 1000));
            freqTexts[i].setText(frequency * j / blockSize + " Hz");
            if(values[j]>sensitivity)
                text.setText(frequency * j / blockSize + " Hz");
        }
        //text.setText("Max: "+frequency * indexOfMax(values)/ blockSize+" Hz");

    }
    private int indexOfMax(double [] array){
        int max=0;
        for(int i=1;i<array.length;i++){
            if(array[i]>array[max]) max=i;
        }
        return max;
    }
}
