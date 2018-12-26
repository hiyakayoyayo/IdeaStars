package com.example.ideastars.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by hiyakayoyayo on 2017/04/21.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Ideas {

    public Ideas(){}

    private Idea[] ideas;
    public void setIdeas( Idea[] _ideas )
    {
        ideas = _ideas;
    }
    public Idea[] getIdeas()
    {
        return ideas;
    }
    public Idea getIdea(long ideaId) {
        for( Idea idx : ideas ) {
            if( idx.getId() == ideaId ) {
                return idx;
            }
        }
        return null;
    }
    public Idea getIdea(String ideaName)
    {
        for( Idea idx : ideas ) {
            if( idx.getName() == ideaName ) {
                return idx;
            }
        }
        return null;
    }

    public void deleteIdea( long _idea_id ) {
    }
    public void deleteIdeas( long[] _idea_ids ) {
        for( long idx : _idea_ids ) {
            deleteIdea(idx);
        }
    }

}

