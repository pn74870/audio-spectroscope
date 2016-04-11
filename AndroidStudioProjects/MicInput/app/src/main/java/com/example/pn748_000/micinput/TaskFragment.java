package com.example.pn748_000.micinput;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by pn748_000 on 3/20/2016.
 */
public class TaskFragment extends Fragment {
    private static final String ARG_FREQ = "freq", ARG_BLOCK_SIZE = "size";
    public TaskCallbacks callbacks;
    private int frequency;
    private int blockSize;
private RecordingTask task;
    public interface TaskCallbacks {
        void progressUpdate(double[] values);

    }

    public boolean isRecording = false;

    public static TaskFragment newInstance(int frequency, int blockSize) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FREQ, frequency);
        args.putInt(ARG_BLOCK_SIZE, blockSize);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            frequency = args.getInt(ARG_FREQ);
            blockSize = args.getInt(ARG_BLOCK_SIZE);
        }
        setRetainInstance(true);

    }

    public void switchTask( boolean on) {
        if(on){
         task = new RecordingTask();
        task.execute();}
        else {
            isRecording=false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (TaskCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TaskCallback interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    class RecordingTask extends AsyncTask<Void, double[], Void> {


        private AudioRecord audioRecord;

        @Override
        protected void onPreExecute() {

            isRecording = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO
                    , AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            short[] buffer = new short[blockSize];
            audioRecord.startRecording();

            while (isRecording) {
                audioRecord.read(buffer, 0, blockSize);
                double[] doubleBuffer = new double[buffer.length];
                for (int i = 0; i < buffer.length; i++){
                    doubleBuffer[i] = (double) buffer[i] / 32768.0;
                }
                //ComplexNumber[] ft = FourierTransform.calculateFFT(doubleBuffer);
                ComplexNumber[] ft=ComplexNumber.createRealArray(doubleBuffer);
                FourierTransform.calcFFT(ft);
                double ans[] = new double[ft.length];
                for (int i = 0; i < ft.length; i++)
                    ans[i] = ft[i].modulusSq();
                publishProgress(ans);

            }
            audioRecord.stop();
            audioRecord.release();
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... values) {
           if(callbacks!=null) callbacks.progressUpdate(values[0]);
        }

    }

    @Override
    public void onStop() {
      /*  if(task!=null ){
            isRecording=false;
            task.cancel(true);
        }*/
        super.onStop();
    }
}
