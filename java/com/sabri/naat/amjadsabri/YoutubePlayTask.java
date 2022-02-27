package com.sabri.naat.amjadsabri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YoutubePlayTask extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    public static final String API_KEY = "**************";

    //https://www.youtube.com/watch?v=<VIDEO_ID>

    String[] VIDEO_ID = new String[]{"0084pFyb_7g","AZYw4weyRJI","cUzUpMFDLlY","sywoviaZMb8","tCamSwld93Q","wEuPiJPLNfs",
            "Iq6mpGbGDxc","OJY9CDDLgDU","lpivOxMkRn8","vbjeN_ppn6c","RUs_VTAaqAw","54oR6yxyHVw"};



    public static int nowPos=0;
    MyBroadCastReceiver receiver;
    LocalBroadcastManager manager;
    CompleteThisTask backgroundWorker;

    SharedPreferences pref_back;
    SharedPreferences.Editor editor;
    private long timeSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_play_task);

        pref_back = this.getSharedPreferences("SabriSaveData", Context.MODE_PRIVATE);
        editor = pref_back.edit();


        timeSpend= System.currentTimeMillis();

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            Log.e("ooooooooo","ok: "+bundle.getString("Position"));
            nowPos= bundle.getInt("Position");
        }

        initBroadCastReceiver();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (null == youTubePlayer) return;

        // Start buffering
        if (!b) {
            youTubePlayer.loadVideo(VIDEO_ID[nowPos]);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


        Toast.makeText(YoutubePlayTask.this,"Failed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (backgroundWorker!=null)
            backgroundWorker.cancel(true);

        manager.unregisterReceiver(receiver);
    }


    @Override
    public void onResume() {
        super.onResume();

        checkNetwork();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.putBoolean("ebtyTi",false);
        editor.commit();
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void checkNetwork(){
        if (isNetworkAvailable()) {

            String type = "data_receive";
//            if(backgroundWorker.getStatus() == AsyncTask.Status.RUNNING ){
//                // My AsyncTask is currently doing work in doInBackground()
//            }else {

            backgroundWorker = new CompleteThisTask(YoutubePlayTask.this);
            backgroundWorker.execute(type, "listdatagive");
//            }
        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(YoutubePlayTask.this);
            builder.setMessage("No Wifi/Data Connection!!!")
                    .setCancelable(false)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            dialog.cancel();
                            checkNetwork();
                        }
                    });
            final AlertDialog alert = builder.create();


            alert.show();


        }
    }

    private void initBroadCastReceiver() {
        manager = LocalBroadcastManager.getInstance(YoutubePlayTask.this);
        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        //whatever
        filter.addAction("com.action.test");
        manager.registerReceiver(receiver,filter);
    }
    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //e.g
            String data = intent.getStringExtra("key");

            Log.e("dddddddddd","ddddddeee"+data);
            if (data!= null){
                data.trim();
                editor.putString("savelinkdata",""+data);
                editor.commit();


                String divide_2 = "divideuswithbbbn";
                String[] titletake;
                titletake =data.split(""+divide_2);
                if (data == "nothing"){

                }
                else if (titletake.length == VIDEO_ID.length )
                {
                    VIDEO_ID[nowPos]= titletake[nowPos];

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                        youTubePlayerView.initialize(API_KEY, YoutubePlayTask.this);

//
                    }
                });
//                list_Data= titletake[0].split("rshyhydttyhtjdr");
//                title_Data= titletake[1].split("rshyhydttyhtjdr");
//                numbers= new int[list_Data.length];
//                for (int c= 0;c<list_Data.length;c++){
//                    numbers[c]= c+1;
//                }
//                Rhymes_List ryhmesList = new Rhymes_List(getContext(),list_Data,title_Data);
//                lvList.setAdapter(ryhmesList);
            }
        }
    }

    public class CompleteThisTask extends AsyncTask<String,String,String> {
        Context context;
        String data,l_u_name;
        ProgressDialog mProgressDialog;
        YoutubePlayTask mainActivity;

        CompleteThisTask (Context ctx) {
            context = ctx;
            mainActivity = (YoutubePlayTask) context;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String massage_list_url = "http://ilabsbd.com/amjadsabri/data_path.php";


            if(type.equals("data_receive")) {
                try {
                    String from_user = params[1];
                    URL url = new URL(massage_list_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context){

                @Override
                public void onBackPressed() {
                    mProgressDialog.cancel();
                    mProgressDialog.dismiss();
                    if (backgroundWorker!=null)
                        backgroundWorker.cancel(true);
                }
            };
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setProgressStyle(android.R.attr.progressBarStyleSmall);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {


            mProgressDialog.dismiss();
            mProgressDialog = null;
            if (result!=null){

                String search1 = "divideuswithbbbn";


                if ((result.equals("Noone has found")) || (result.equals("Nothing")) || (result.equals("nothingNoone has found"))){

//                    Toast.makeText(context,"Something going wrong",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("com.action.test");
                    intent.putExtra("key","nothing");
                    manager.sendBroadcast(intent);
                }
                else if (result.contains(search1)){

                    Intent intent = new Intent("com.action.test");
                    intent.putExtra("key",""+result);
                    manager.sendBroadcast(intent);
                }
                else {

                    Intent intent = new Intent("com.action.test");
                    intent.putExtra("key",""+result);
                    manager.sendBroadcast(intent);
//                Toast.makeText(context,"wrong query",Toast.LENGTH_LONG).show();
                }

            }
            else {
//                Toast.makeText(context,"Something going wrong",Toast.LENGTH_LONG).show();

                Intent intent = new Intent("com.action.test");
                intent.putExtra("key","nothing");
                manager.sendBroadcast(intent);
            }
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }


    }


    public void makeYoutubePlay(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(YoutubePlayTask.this,VideoStartPage.class);
        if (System.currentTimeMillis()-timeSpend >(1000*60)) {
            intent.putExtra("showadsbackhere", "seethis");
//            s_inter=false;
        }
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

    }
}
