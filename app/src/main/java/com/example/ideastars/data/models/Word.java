package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Word {
    private long mId;
    public long getId(){ return mId; }
    public void setId(long id) {
        mId = id;
        for (Item item:mItems ) {
            item.setWordId(mId);
        }
    }

    private long mIdeaId;
    public long getIdeaId(){ return mIdeaId; }
    public void setIdeaId( long wordId ){ mIdeaId = wordId; }

    @NonNull
    private String mName;
    public void setName( String name )
    {
        mName = name;
    }
    public String getName()
    {
        return mName;
    }

    private int mColor;
    public void setColor( int color ) { mColor = color; }
    public int getColor() { return mColor; }

    private int mPriority;
    public int getPriority() { return mPriority; }
    public void setPriority(int priority) {
        mPriority = priority;
    }

    private ArrayList<Item> mItems = new ArrayList<Item>();
    public void setItems( Item[] items )
    {
        mItems.addAll( Arrays.asList(items) );
    }
    public Item[] getItems()
    {
        return mItems.toArray(new Item[mItems.size()]);
    }
    public Item getItem(long itemId)
    {
        for( Item idx : mItems ) {
            if( idx.getId() == itemId ) {
                return idx;
            }
        }
        return null;
    }
    public Item getItem(String itemName)
    {
        for( Item idx : mItems ) {
            if( idx.getName() == itemName ) {
                return idx;
            }
        }
        return null;
    }

    public Word(){
        mId = -1;
        mIdeaId = -1;
        mName = "";
        mPriority = -1;
        mItems.clear();
    }

    public Word(long id, long idea_id, @NonNull String name, int color, int priority)
    {
        mId = id;
        mIdeaId = idea_id;
        mName = name;
        mColor = color;
        mPriority = priority;
        mItems.clear();
    }

    public boolean RemoveItem(@NonNull String itemName)
    {
        boolean ret = false;
        Iterator itr = mItems.iterator();
        Item item;
        while(itr.hasNext()){
            item = (Item)itr.next();
            if(null==item) continue;
            if(item.getName().equals(itemName)) {
                itr.remove();
                ret = true;
            }
        }
        mItems.removeAll(Collections.singleton(null));
        return ret;
    }

    public boolean ContainsItem(@NonNull String itemName)
    {
        Iterator itr = mItems.iterator();
        Item item;
        while(itr.hasNext()){
            item = (Item)itr.next();
            if(null==item) continue;
            if(item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public Item AddItem(@NonNull String itemName, int priority) {
        return AddItemFromRepository(-1,itemName,priority);
    }

    public Item AddItemFromRepository(int id, @NonNull String itemName, int priority)
    {
        Item item = null;
        if( false == ContainsItem(itemName) ) {
            item = new Item(id, mId, itemName, priority);
            mItems.add(item);
        }
        return item;
    }

}
