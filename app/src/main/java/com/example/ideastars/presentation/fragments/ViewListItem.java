package com.example.ideastars.presentation.fragments;

import android.support.annotation.NonNull;

public class ViewListItem {
    @NonNull
    private final long mId;

    @NonNull
    private final String mName;

    private boolean mSelected;

    private int mColor;

    @NonNull
    public long getId() { return mId; }

    @NonNull
    public String getName() {
        return mName;
    }

    public int getColor() { return mColor; }

    public ViewListItem(boolean selected, long id, String name )
    {
        mId = id;
        mName = name;
        mSelected = selected;
        mColor = 0xFFFFFFFF;
    }

    public ViewListItem(boolean selected, long id, String name, int color )
    {
        mId = id;
        mName = name;
        mSelected = selected;
        mColor = color;
    }

    public void setSelected(boolean selected) { mSelected = selected; }
    public boolean isSelected() {
        return mSelected;
    }

}
