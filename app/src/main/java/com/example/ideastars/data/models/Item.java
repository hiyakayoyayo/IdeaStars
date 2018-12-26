package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

import com.example.ideastars.presentation.fragments.SetNamePresenter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Item {
    private long mId;
    public long getId(){ return mId; }
    public void setId(long id) { mId = id; }

    @NonNull
    private String mName;

    private int mPriority;
    public int getPriority() { return mPriority; }
    public void setPriority(int priority) {
        mPriority = priority;
    }

    private long mWordId;
    public long getWordId(){ return mWordId; }
    public void setWordId( long wordId ){ mWordId = wordId; }

    public Item(){
        mId = -1;
        mWordId = -1;
        mName = "";
        mPriority = -1;
    }

    public Item(long id, long word_id, @NonNull String name, int priority)
    {
        mId = id;
        mWordId = word_id;
        mName = name;
        mPriority = priority;
    }

    public void setName( @NonNull String name ) { mName = name; }
    @NonNull
    public String getName() {
        return mName;
    }

}
