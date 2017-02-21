package com.tejas.paras.musicapp;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {


    private MediaPlayer mMediaPlayer;
    private ArrayList<DataModel> dataModels;
    private AudioManager audioManager = null;
    private SeekBar volumeSeekbar = null,songProgress=null;
    private ListView listView;
    private static CustomAdapter adapter;
    private ImageButton ib3;
    private int i=0,mCurrentPosition,resume;
    private DataModel dataModel;
    private String current,time,time2,s_name;
    private Handler mHandler;
    private TextView tv1,tv5,tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        songProgress=(SeekBar)findViewById(R.id.seekBar);
        volumeSeekbar = (SeekBar)findViewById(R.id.seekBar2);
        audioManager = (AudioManager) getSystemService(MainActivity.this.AUDIO_SERVICE);
        volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mMediaPlayer = new MediaPlayer();
        listView=(ListView)findViewById(R.id.listview1);
        ib3=(ImageButton)findViewById(R.id.imageButton3);
        tv1=(TextView)findViewById(R.id.textView1);
        tv5=(TextView)findViewById(R.id.textView5);
        tv6=(TextView)findViewById(R.id.textView6);
        tv5.setText("00:00");
        songProgress.setProgress(0);
        volumeSeekbar.setProgress(40);

        dataModels= new ArrayList<>();

        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA ,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.DURATION}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");


        if (mCursor.moveToFirst()) {
            do {

                dataModels.add(new DataModel(mCursor.getString(0),mCursor.getString(1), mCursor.getString(2),mCursor.getString(3)));

            } while (mCursor.moveToNext());
        }
        mCursor.close();
        adapter= new CustomAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataModel= dataModels.get(position);

                try {
                    ib3.setImageResource(android.R.color.transparent);
                    ib3.setBackgroundResource(R.drawable.pause);
                    Toast.makeText(MainActivity.this,dataModel.getType(),Toast.LENGTH_LONG).show();
                   current=dataModel.getType();
                    s_name=dataModel.getName();
                    i=1;
                    time=dataModel.getDuration();
                    getTime(time);
                    resume=0;
                    playSong(current,s_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==1) {
                    mMediaPlayer.pause();
                    resume=mMediaPlayer.getCurrentPosition();
                    ib3.setImageResource(android.R.color.transparent);
                    ib3.setBackgroundResource(R.drawable.play);
                    i=0;
                }
                else{
                    dataModel= dataModels.get(1);
                    current=dataModel.getType();
                    time=dataModel.getDuration();
                    ib3.setImageResource(android.R.color.transparent);
                    ib3.setBackgroundResource(R.drawable.pause);
                    getTime(time);
                    try {
                        playSong(current,s_name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i=1;
                }
            }
        });



        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }
        });

        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser){
                    int lenght= Integer.parseInt(time);
                    mMediaPlayer.seekTo(progress * (lenght/100));
                }
            }
        });
//Make sure you update Seekbar on UI thread
//        MainActivity.this.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                if(mMediaPlayer != null){
//                    mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
//
//                    songProgress.setProgress(mCurrentPosition);
//                }
//                //tv5.setText(mCurrentPosition);
//                songProgress.postDelayed(this,1000);
//            }
//        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int index = volumeSeekbar.getProgress();
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {

            volumeSeekbar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {

            volumeSeekbar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playSong(String path1,String s_name) throws IllegalArgumentException,
            IllegalStateException, IOException {

        tv1.setText(s_name);
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path1);
        mMediaPlayer.prepare();
        if(resume==0)
        mMediaPlayer.start();
        else
        {
            mMediaPlayer.seekTo(resume);
            mMediaPlayer.start();
        }
    }


    public void getTime(String time)
    {
        long dur = Long.parseLong(time);
        String seconds = String.valueOf((dur % 60000) / 1000);

        String minutes = String.valueOf(dur / 60000);
        time2 = minutes + ":" + seconds;
        if (seconds.length() == 1) {
            tv6.setText("0" + minutes + ":0" + seconds);
        }else {
            tv6.setText("0" + minutes + ":" + seconds);
        }

    }
}
