package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class Favorite {
    @NonNull
    private long mId;
    public void setId( long id ) { mId = id; }
    public long getId() { return mId; }

    @NonNull
    private long[] mItemIds;
    public long[] getItemIds(){ return mItemIds; }
    public void setItemIds(long[] itemIds){ mItemIds = itemIds; }

    private float mFavorite;
    public float getFavorite(){ return mFavorite; }
    public void setFavorite( float fav ){ mFavorite = fav; }

    public Favorite()
    {
        mId = -1;
        mItemIds = new long[]{};
        mFavorite = 1f;
    }

    public Favorite(@NonNull long id, @NonNull long[] itemIds, float favorite)
    {
        mId = id;
        mItemIds = itemIds;
        mFavorite = favorite;
    }

    public boolean IsSameIds(@NonNull long[] itemIds)
    {
        if( itemIds.length < mItemIds.length ) return false;
        boolean bSame;
        for ( long favItemId : mItemIds ) {
            bSame = false;
            for( long itemId : itemIds ) {
                if( favItemId == itemId ) {
                    bSame = true;
                    break;
                }
            }
            if( false == bSame ) {
                return false;
            }
        }
        return true;
    }

    public boolean IsHaveIds(@NonNull long[] itemIds)
    {
        for ( long favItemId : mItemIds ) {
            for( long itemId : itemIds ) {
                if( favItemId == itemId ) {
                    return true;
                }
            }
        }
        return false;
    }

}
