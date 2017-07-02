package com.project.gaurs.tadqa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.gaurs.tadqa.Activities.MainScreen;
import com.project.gaurs.tadqa.Activities.OpenScreen;

import java.lang.reflect.Method;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout splash;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        splash=(RelativeLayout)findViewById(R.id.splash);
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {


                if(isMobileDataEnabled()){
                    //Mobile data is enabled and do whatever you want here
                    if(user==null) {
                        Intent i = new Intent(SplashActivity.this, OpenScreen.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(SplashActivity.this, MainScreen.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    //Mobile data is disabled here
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setMessage("No mobile data connection detected.\nPlease check network connection and visit again.");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }, 4000);
    }
    private boolean isMobileDataEnabled(){
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);

            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileDataEnabled;
    }
}
