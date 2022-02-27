package com.sabri.naat.amjadsabri;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.stats.StatsEvent;

import java.util.Random;

/**
 * Created by Tanin on 9/22/2016.
 */
public class AppRater {
    private final static String APP_TITLE = "ছোট সূরাসমূহ";// App Name
    private final static String APP_PNAME = "com.dhakastall.tanin.smallsurahquran";// Package Name
    public static boolean noWay=true;
    private static Context context2;
    private static int count_sa_b;
    public static StartPage mainActivity;


    static SharedPreferences pref_back;
    static SharedPreferences.Editor editor;

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        context2=mContext;
        mainActivity = (StartPage) mContext;
        pref_back= mainActivity.getSharedPreferences("SurahSaveData", Context.MODE_PRIVATE);
        editor = pref_back.edit();

            showRateDialog(mainActivity,mContext);


}

    public static void showRateDialog(StartPage mainActivity, final Context context) {
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(context);
//        mBuilder.setCancelable(false);
        View mView = mainActivity.getLayoutInflater().inflate(R.layout.alert_dialog,null);
        final TextView tvDetail= (TextView) mView.findViewById(R.id.tvDetail);
        final Button p_again= (Button) mView.findViewById(R.id.p_again);
        final Button no_p= (Button) mView.findViewById(R.id.no_p);
        final Button moreA= (Button) mView.findViewById(R.id.more_a);
        p_again.setTransformationMethod(null);
        no_p.setTransformationMethod(null);
        moreA.setTransformationMethod(null);
        p_again.setText("Rate");
        no_p.setText("Exit");
        moreA.setText("More Apps");
        tvDetail.setText("If you like this App"+". Please help us with 5*. Thanks");

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        p_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UpdateData("don't Show",1,0);
//                editor.putInt("dontshowagain", 1);
//                editor.commit();

//                noWay=true;
//
//                SharedPreferences pref_3 = context2.getSharedPreferences("myPrefsKeyAR", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor2 = pref_3.edit();
//
//                editor2.putBoolean("keyAR", noWay);
//                Log.e("okkkkkkk",""+noWay);
                final String myappPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myappPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + myappPackageName)));
                }
                dialog.dismiss();
            }
        });
        moreA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:AppsArena")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=AppsArena")));
                }
                dialog.dismiss();
            }
        });
        no_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (editor != null) {
//                    editor.putInt("dontshowagain", 3);
//                    editor.commit();
//
//                }
//                UpdateData("Later",1,0);
//                UpdateData("Later",2,0);

                editor.putString("running", "notRunning");
                editor.commit();
                editor.clear();
                Intent intent2 = new Intent(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_HOME);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                Process.killProcess(Process.myPid());

                dialog.dismiss();
            }
        });
        if(!((Activity) context).isFinishing())
        {
            dialog.show();
            //show dialog
        }
        else if (((Activity) context).isFinishing()){
            dialog.dismiss();
        }


    }
}
