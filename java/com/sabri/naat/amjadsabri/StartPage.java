package com.sabri.naat.amjadsabri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class StartPage extends AppCompatActivity {

    LinearLayout audioBut, videoBut;
    boolean s_inter=true;


    TextView more_app;
    LinearLayout shareit;
    ImageView fb_share;

    public int getTaskRunningNow() {
        return taskRunningNow;
    }

    public void setTaskRunningNow(int taskRunningNow) {
        this.taskRunningNow = taskRunningNow;
    }

    int taskRunningNow;
    SharedPreferences pref_back;
    SharedPreferences.Editor editor;


    private AdView dtAdViewmain;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_start_page);

        audioBut= findViewById(R.id.audioBut);
        videoBut= findViewById(R.id.videoBut);

        MobileAds.initialize(this, getResources().getString(R.string.ad_id_app));

        pref_back = this.getSharedPreferences("SabriSaveData", Context.MODE_PRIVATE);
        editor = pref_back.edit();


//        Adapt_all adapter2 = new Adapt_all(StartPage.this, getResources().getStringArray(R.array.allName));
//        listData.setAdapter(adapter2);

//        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                editor.putInt("posData", i);
//                editor.commit();
//                taskRunningNow = i;
//                s_inter=false;
//                Intent intent= new Intent(StartPage.this,MainActivity.class);
//                intent.putExtra("Position",i);
//                startActivity(intent);
//
//            }
//        });

        audioBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(StartPage.this,AudioStartPage.class);
                startActivity(intent);
            }
        });

        videoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(StartPage.this,VideoStartPage.class);
                startActivity(intent);
            }
        });


        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstiial_ad_unit));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                requestNewInterstitial();
            }
        });


        requestNewInterstitial();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String showads = bundle.getString("showadsbackhere");
            if (showads != null && showads.equals("seethis")) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {

                        if (s_inter) {

//                                        Log.e("eeeeeeeeee","see"+s_inter);
                            try {

                                mInterstitialAd.show();
                            } catch (Exception ec) {
                                ec.printStackTrace();
                            }
                        }

                    }
                });
            }
//            String this_app = bundle.getString("this_app");

            //bundle must contain all info sent in "data" field of the notification
        }


        rateTask();
        try {

            String token= FirebaseInstanceId.getInstance().getToken();
            if (token!=null){

//                Log.e("loooooooooooooooo","tok: "+token);
                String tokenold= pref_back.getString("ttkn", null);
                if (tokenold!=null){
                    if (!tokenold.equals(token)) {
                        editor.putString("ttkn",""+token);
                        updatedb(token);
                        editor.apply();
                    }
//            Log.e("loooooooooooooooo","ll:: "+token);
                }else{
//                    Log.e("loooooooooooooooo","kkkkk2 ");
                    updatedb(token);
                    editor.putString("ttkn",""+token);
                    editor.commit();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        dtAdViewmain = (AdView) findViewById(R.id.dtadViewmain);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        dtAdViewmain.loadAd(adRequest);

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
    protected void onResume() {
        s_inter=true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        s_inter=false;


        super.onPause();

    }

    @Override
    public void onBackPressed() {
        AppRater.app_launched(StartPage.this);
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


    public void rateTask(){


        LinearLayout rateus = (LinearLayout) findViewById(R.id.rateushome);
        more_app = (TextView) findViewById(R.id.more_apps);
        shareit = (LinearLayout) findViewById(R.id.share);
        fb_share = (ImageView) findViewById(R.id.fbI);


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
    public void updatedb(String token){
        new CompleteThisTask(StartPage.this).execute("taketoken",""+token);
    }

    public class CompleteThisTask extends AsyncTask<String,String,String> {
        Context context;
        StartPage mainActivity;

        CompleteThisTask (Context ctx) {
            context = ctx;
            mainActivity = (StartPage) context;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            if(type.equals("taketoken")) {
                String user_name = "first_global_sabri-com.sabri.naat.amjadsabri";

                String ann_list_url = "http://ilabsbd.com/firebase_tasker/jonayed_jamshed.php";
                try {



                    String from_user = params[1];
                    URL url = new URL(ann_list_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("to_user", "UTF-8") + "=" + URLEncoder.encode(from_user, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }catch (MalformedURLException ml){
                    ml.printStackTrace();
                }catch (IOException io){
                    io.printStackTrace();
                }
            }




            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {




        }

        protected void onProgressUpdate(String... progress) {
        }


    }
}
