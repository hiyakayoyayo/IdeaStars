package com.example.ideastars.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hiyakayoyayo on 2017/07/01.
 */

public class VerboseFragment extends Fragment
{
    @Override
    public void onAttach(Context context) {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onStart");
        super.onStart();
    }

    @Override
    public void onResume()
    {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onResume");
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("lifecycle",this.getClass().getSimpleName()+"::onDetach");
        super.onDetach();
    }
}

