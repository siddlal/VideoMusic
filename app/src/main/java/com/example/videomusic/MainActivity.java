package com.example.videomusic;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //components
    public VideoView videoView;
    public Button btnPlayVideo;
    public MediaController mediaController;
    public Button btnPlayMusic,btnPauseMusic;
    public MediaPlayer mediaPlayer;
    public SeekBar seekBarVolume;
    public AudioManager audioManager;
    public SeekBar moveBackAndForthSeekBar;
    public Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);


        btnPlayVideo = findViewById(R.id.btnPlayVideo);

        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        moveBackAndForthSeekBar = findViewById(R.id.seekBarMove);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);

        mediaController = new MediaController(MainActivity.this);
        mediaPlayer = MediaPlayer.create(this,R.raw.music1);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maximumVolumeOfUser = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeOfUser = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    //    int minimumVolumeOfUser = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);

        seekBarVolume.setMax(maximumVolumeOfUser);
        seekBarVolume.setProgress(currentVolumeOfUser);

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_LONG).show();

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        moveBackAndForthSeekBar.setOnSeekBarChangeListener(MainActivity.this) ;
        moveBackAndForthSeekBar.setMax(mediaPlayer.getDuration());
    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()){

            case R.id.btnPlayVideo:

                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();

                break;

            case R.id.btnPlayMusic:

                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveBackAndForthSeekBar.setProgress(mediaPlayer.getCurrentPosition()    );
                    }
                },0,1000);

                break;

            case R.id. btnPauseMusic:

                mediaPlayer.pause();
                if(timer != null){
                    timer.cancel();
                }

                break;
        }



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
//            Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_LONG).show();
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.stop();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }
}
