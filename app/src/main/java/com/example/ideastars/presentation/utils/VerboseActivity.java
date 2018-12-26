package com.example.ideastars.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class VerboseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onDestroy");
        super.onDestroy();
    }

}
