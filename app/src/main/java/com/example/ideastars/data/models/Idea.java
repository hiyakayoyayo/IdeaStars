package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Idea {

    public Idea()
    {
        mId = -1;
        mName = "";
        mPriority = -1;
        mWords.clear();
    }

    public Idea(long id, @NonNull String name, int priority)
    {
        mId = id;
        mName = name;
        mPriority = priority;
        mWords.clear();
    }

    private long mId;
    public long getId() { return mId; }
    public void setId(long id) {
        mId = id;
        for (Word word:mWords ) {
            word.setIdeaId(mId);
        }
    }

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

    private int mPriority;
    public int getPriority() { return mPriority; }
    public void setPriority(int priority) {
        mPriority = priority;
    }

    private ArrayList<Word> mWords = new ArrayList<Word>();
    public void setWords( Word[] words )
    {
        mWords.addAll( Arrays.asList(words) );
    }
    public Word[] getWords()
    {
        return mWords.toArray(new Word[mWords.size()]);
    }
    public Word getWord(long wordId) {
        for( Word idx : mWords ) {
            if( idx.getId() == wordId ) {
                return idx;
            }
        }
        return null;
    }

    private ArrayList<Favorite> mFavorites = new ArrayList<Favorite>();
    public void setFavorites( Favorite[] favs ) { mFavorites.addAll( Arrays.asList(favs) ); }
    public Favorite[] getFavorites(){ return mFavorites.toArray(new Favorite[mFavorites.size()]); }

    private void RemoveFavotiteArray(@NonNull long[] itemIds )
    {
        Iterator itr = mFavorites.iterator();
        Favorite fav;
        while(itr.hasNext()){
            fav = (Favorite)itr.next();
            if(null==fav) continue;
            if(fav.IsSameIds(itemIds)) {
                itr.remove();
            }
        }
        mWords.removeAll(Collections.singleton(null));
    }

    public boolean ContainsWord(@NonNull String wordName)
    {
        Iterator itr = mWords.iterator();
        Word word;
        while(itr.hasNext()){
            word = (Word)itr.next();
            if(null==word) continue;
            if(word.getName().equals(wordName)) {
                return true;
            }
        }
        return false;
    }

    public Word AddWord(@NonNull String wordName, int color, int priority)
    {
        return AddWordFromRepository(-1,wordName,color,priority);
    }

    public Word AddWordFromRepository(long id, @NonNull String wordName, int color, int priority)
    {
        Word word = null;
        if( false == ContainsWord(wordName) ) {
            word = new Word(id, mId,wordName,color,priority);
            mWords.add(word);
            return word;
        }
        return word;
    }

}
