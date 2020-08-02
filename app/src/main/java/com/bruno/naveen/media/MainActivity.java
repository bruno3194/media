package com.bruno.naveen.media;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener {

    private VideoView v1;
    private Button b1,bplay,bpause;
    private MediaController mc;
    private MediaPlayer mp;
    private SeekBar sk,seek;
    private AudioManager am;
    private int max1;
    private Timer timer;

    //FOR CLOSING TIMER(WHICH IS HEAVY)
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
       timer.cancel();
        Toast.makeText(this,"MUSIC STOPPED",Toast.LENGTH_SHORT).show();

    }

    //SEEKING AUDIO
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        max1=mp.getDuration();
        if(b)
        {
            mp.seekTo(i);
            //Toast.makeText(this,Integer.toString(i), Toast.LENGTH_LONG).show();
        }
//        if(i==max1)
//        {
//
//        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
            mp.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mp.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //INTIALIZES..
        v1=(VideoView)findViewById(R.id.videoView);
        b1=(Button)findViewById(R.id.button);
        bplay=findViewById(R.id.btnplay);
        bpause=findViewById(R.id.btnpause);
        sk=findViewById(R.id.seek);
        seek=findViewById(R.id.seek2);

        //BUTTONS ON CLICKS..
        b1.setOnClickListener(MainActivity.this);
        bplay.setOnClickListener(MainActivity.this);
        bpause.setOnClickListener(MainActivity.this);
        //MEDIA INTERFFACES
        mc=new MediaController(MainActivity.this);
        //mp=MediaPlayer.create(this,R.raw.birds);
        //ANOTHER METHOD FOR ABOVE LINE (IN CASE WE WANT TO ACCESS FILE USING NAME)*********************************************IMP***********************
        mp=MediaPlayer.create(this,getResources().getIdentifier("birds","raw",getPackageName()));

        am=(AudioManager) getSystemService(AUDIO_SERVICE);//FOR CONTROLLING AUDIO

        //SETTING SEEK VALUES
        int max=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cur=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sk.setMax(max);
        sk.setProgress(cur);
        seek.setMax(mp.getDuration());
        //SEEK CURRENT IS SET USING TIMER

//SEEK LISTENERS
        seek.setOnSeekBarChangeListener(this);
        //SEEKING VOLUME
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    //Toast.makeText(MainActivity.this,Integer.toString(i),Toast.LENGTH_LONG).show();
                    am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mp.setOnCompletionListener(this);
    }


    //BUTTONS ACTIONS
    @Override
    public void onClick(View bview) {

        switch (bview.getId())
        {
            case R.id.button:

                Uri v1uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sea);
                v1.setVideoURI(v1uri);

                v1.setMediaController(mc);
                mc.setAnchorView(v1);

                v1.start();
                break;

            case R.id.btnplay:
                //mp.setLooping(true);
                timer=new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                     seek.setProgress(mp.getCurrentPosition());
                    }
                },0,1000);
                mp.start();
                break;

            case R.id.btnpause:
                mp.pause();
                timer.cancel();
                break;

        }

    }
}
