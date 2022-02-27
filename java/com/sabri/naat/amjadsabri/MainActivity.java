package com.sabri.naat.amjadsabri;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener {



    TextView more_app;
    LinearLayout shareit;
    ImageView fb_share;


    private boolean adshown = false;
    ImageView prevP, nextP;


    MediaPlayer mediaPlayer;
    ImageView start, repeat;
    SeekBar seekBar;
    Handler seekHandler = new Handler();
    boolean playit = true;
    int reply = 10;

    int MusixName=0;
    LinearLayout lvvvvcarry;
    ListView listData;
    private boolean s_inter = true;
    RelativeLayout mediaViewMake;

    SharedPreferences pref_back;
    SharedPreferences.Editor editor;
    InterstitialAd mInterstitialAd;
    int[] gojol = new int[]{R.raw.dua_manghta_hon, R.raw.milta_hai_kia_namaz, R.raw.mera_koi_nehi,R.raw.bhar_do_jholi,
            R.raw.jab_waqt_naza_aye,R.raw.ya_sahib_al_jamal,R.raw.ya_mustafa,R.raw.tajdar_e_haram,R.raw.jis_nay_madinay_jana,
            R.raw.sar_e_lamakan,R.raw.nazar_karoon_jaan_o_jigar,R.raw.utho_rindo_piyo_jam_qalandar};
    String[] gojol_title;

    private long timeSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);
        pref_back = this.getSharedPreferences("SabriSaveData", Context.MODE_PRIVATE);
        editor = pref_back.edit();

        gojol_title= getResources().getStringArray(R.array.allNameBang);
        timeSpend=System.currentTimeMillis();
        listData =  findViewById(R.id.listData);
        mediaViewMake =  findViewById(R.id.mediaViewMake);
        lvvvvcarry =  findViewById(R.id.lvvvcarry);
        seekBar =  findViewById(R.id.seekbar);
        start =  findViewById(R.id.btnstart);
        repeat =  findViewById(R.id.btnreply);
        prevP =  findViewById(R.id.prevPl);
        nextP =  findViewById(R.id.nextPl);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (mAudioManager != null) {
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
        }


        start.setImageResource(R.drawable.ic_pause);
        repeat.setImageResource(R.drawable.ic_reply);
        if (pref_back.getInt("replymode", 0)==10){
            repeat.setImageResource(R.drawable.ic_reply);
            reply=10;
        }else if (pref_back.getInt("replymode", 0)==20){
            repeat.setImageResource(R.drawable.ic_reply);
            reply=20;
        }else {
            repeat.setImageResource(R.drawable.ic_repeat_one);
            reply=30;
        }

        mediaPlayer = new MediaPlayer();
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            MusixName = extras.getInt("Position");
            Music(MusixName);
            playit=true;
            checkMusic();
        } else {Music(0);
        }

        setTitle(gojol_title[MusixName]);


        s_inter = true;




        rateTask();


        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstiial_ad_unit));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                timeSpend=System.currentTimeMillis();
                adshown = false;
            }

        });


        requestNewInterstitial();


        AdView dtAdViewmain =  findViewById(R.id.dtadViewmain);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        dtAdViewmain.loadAd(adRequest);

        readyTask();
        seek_pos();








    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnstart:
                if (playit) {
                    playit = false;
                    try {
                        if (mediaPlayer!=null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.pause();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    start.setImageResource(R.drawable.ic_play);



                } else {
                    playit = true;
                    try {
                        if (mediaPlayer!=null){
                            if (!mediaPlayer.isPlaying()){
                                mediaPlayer.start();
                                setOnCompListener();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    start.setImageResource(R.drawable.ic_pause);

                }
                break;
            case R.id.btnreply:

                if (reply==10) {
                    reply = 20;
                    repeat.setImageResource(R.drawable.ic_repeat_one);

                    editor.putInt("replymode", reply);
                    editor.apply();

                } else if (reply==20) {
                    reply = 30;
                    repeat.setImageResource(R.drawable.ic_no_reply);

                    editor.putInt("replymode", reply);
                    editor.apply();

                } else {
                    reply = 10;
                    repeat.setImageResource(R.drawable.ic_reply);
                    editor.putInt("replymode", reply);
                    editor.apply();

                }


                break;


            case R.id.seekbar:
                seekBar();

        }


    }





    public void seekBar() {


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    if (mediaPlayer!=null){
                        mediaPlayer.seekTo(i);
                    }

                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            seek_pos();
        }
    };

    public void seek_pos() {
        if (mediaPlayer != null) {
            int mCurrentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
        }
        seekHandler.postDelayed(runnable, 500);
    }



    private void requestNewInterstitial() {
        try {
            AdRequest adRequest = new AdRequest.Builder().build();

            mInterstitialAd.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.homeButton: {
                onBackPressed();
                return true;
            }
            case R.id.rate_us: {
                final String myappPackageName = this.getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myappPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + myappPackageName)));
                }

                return true;
            }

            case R.id.newGoj: {
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1782714495291272")));
                } catch (Exception e) {
                    startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/1782714495291272")));
                }

                return true;

            }




            default: {


                return super.onOptionsItemSelected(item);
            }
        }
    }



    @Override
    public void onBackPressed() {


//        s_inter = false;
        super.onBackPressed();
        Intent intent= new Intent(MainActivity.this,AudioStartPage.class);
        if (System.currentTimeMillis()-timeSpend >(1000*60) && s_inter) {
            intent.putExtra("showadsbackhere", "seethis");
//            s_inter=false;
        }
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

//        finish();

    }

    public void updateNextTask(int daaata) {

        if (daaata == 10) {
            if (MusixName < gojol.length - 1) {

                MusixName+= 1;
            } else {
                MusixName = 0;
            }
            seekBar.setProgress(0);
            Music(MusixName);

            try {
                setTitle(gojol_title[MusixName]);


            } catch (Exception e) {
                e.printStackTrace();
            }
            seekBar();

        }
        if (daaata == 20) {
            if (MusixName > 0) {

                MusixName -= 1;
            } else {
                MusixName = gojol.length - 1;
            }
            seekBar.setProgress(0);
            Music(MusixName);
            try {
                setTitle(gojol_title[MusixName]);


            } catch (Exception e) {
                e.printStackTrace();
            }
            seekBar();
        }


    }


    public void readyTask() {

        if (pref_back.getInt("replymode",0)==10){
            repeat.setImageResource(R.drawable.ic_reply);
        }else if (pref_back.getInt("replymode",0)==20){
            repeat.setImageResource(R.drawable.ic_repeat_one);
        }else {
            repeat.setImageResource(R.drawable.ic_no_reply);
        }

        nextP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                nextTask();
//
            }
        });

        prevP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prevTask();

            }

        });
        start.setOnClickListener(this);
        repeat.setOnClickListener(this);
        seekBar();

    }



    @Override
    protected void onResume() {
        if (goneSleep==10){

//            playit=true;
            checkMusic();
        }else {
//            if (tester==10){
//                Music(MusixName);
//                playit=false;
//                checkMusic();
//            }
//            else {
            checkMusic();
//            }
//            try {
//                if(mediaPlayer!=null){
//                    if (!mediaPlayer.isPlaying()){
//                        mediaPlayer.start();
//                        setOnCompListener();
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            Music(MusixName);
//            playit=true;
//            checkMusic();
        }
//        s_inter=true;
        super.onResume();
    }

    int goneSleep=0;

    @Override
    protected void onPause() {
        s_inter = false;

        KeyguardManager myKM = (KeyguardManager)
                getSystemService(Context.KEYGUARD_SERVICE);

        if (myKM != null) {
            if( myKM.inKeyguardRestrictedInputMode()) {
                // it is locked
                goneSleep=10;
            }
            else {
                try {
                    if(mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            if (!adshown){

                                playit = false;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        s_inter=false;
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onDestroy() {

        stopMediaPl();

        super.onDestroy();
    }


    public void nextTask() {

        updateNextTask(10);
        checkMusic();
    }

    public void prevTask() {
        updateNextTask(20);
        checkMusic();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Do your calcluations
        return super.dispatchTouchEvent(ev);
    }


    public void Music(final int fileDescriptor){

        try{
            if(mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), gojol[fileDescriptor]);
            seekBar.setMax(mediaPlayer.getDuration());
        }catch (Exception ew){
            onBackPressed();
        }


    }

    public void stopMediaPl(){
        try {
            if (mediaPlayer!=null){
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }else {

                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        playit=false;
        checkMusic();
    }

    public void checkMusic(){
        if (!playit) {
            if (mediaPlayer!=null){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
            start.setImageResource(R.drawable.ic_play);



        } else {
            try {
                if (mediaPlayer!=null){
                    if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        setOnCompListener();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            start.setImageResource(R.drawable.ic_pause);

        }
    }
    public void setOnCompListener(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

//                Log.e("ssssssssssss","2: "+reply);
                synchronized (this) {

                    if (reply==10) {
                        if (mediaPlayer != null) {
                            nextTask();

                        }
                        else {
                            Music(0);
                        }
                    }else if (reply==20) {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            }else {

                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            }

                        }
                        else {
                            Music(MusixName);
                        }
                    }else {
                        if (mediaPlayer!=null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.seekTo(0);
                                mediaPlayer.stop();
                            }else {
                                mediaPlayer.seekTo(0);
                            }
                        }
                        playit=false;
                        start.setImageResource(R.drawable.ic_play);
                        if (System.currentTimeMillis()-timeSpend >(1000*100) && s_inter) {

                            if (mInterstitialAd.isLoaded()) {
                                adshown = true;
//                            nextTaskBool = true;
                                try {
                                    mInterstitialAd.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestNewInterstitial();
//                            Log.e("ppppuupp","loade2");
//                                s_inter = true;

                                }
                            } else {
                                requestNewInterstitial();
//                        Log.e("pppjjnppppp","loade2");
                                s_inter = true;
                            }
                        }
                    }
                }
            }
        });
    }

    int foCheck=0;

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback

//                Log.e("ddddddddd","1");
                try {
                    if(mediaPlayer!=null){
                        if (!mediaPlayer.isPlaying()){
                            if (foCheck==5) {
                                mediaPlayer.start();
                                playit = true;
                                mediaPlayer.setVolume(1.0f, 1.0f);
                                foCheck=0;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player

                foCheck=10;

                playit = false;
                try {
                    if(mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            if (!adshown){

                                playit = false;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume

                try {
                    if(mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            foCheck=5;
                            if (!adshown){
                                playit = false;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level


                try {
                    if(mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.setVolume(0.1f, 0.1f);
                            if (!adshown){

                                playit = false;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }

    public void rateTask(){


        LinearLayout rateus =  findViewById(R.id.rateushome);
        more_app =  findViewById(R.id.more_apps);
        shareit =  findViewById(R.id.share);
        fb_share =  findViewById(R.id.fbI);


        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String myappPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myappPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + myappPackageName)));
                }
            }
        });

        more_app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:AppsArena")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=AppsArena")));
                }


            }
        });
        shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String myappPackageName = getPackageName();

                try {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = "https://play.google.com/store/apps/details?id=" + myappPackageName;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share via"));
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });

        fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1782714495291272")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/1782714495291272")));
                }
            }
        });
    }
}
