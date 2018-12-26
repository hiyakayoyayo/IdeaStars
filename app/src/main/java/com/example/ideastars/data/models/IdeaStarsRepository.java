package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hiyakayoyayo on 2017/06/08.
 */

public class IdeaStarsRepository implements IdeaStarsData {

    private static IdeaStarsRepository INSTANCE;

    private IdeaStarsData ideaStarsData;

    private IdeaStarsRepository( @NonNull IdeaStarsData localData )
    {
        ideaStarsData = localData;
    }

    public static IdeaStarsRepository getInstance( @NonNull IdeaStarsData localData )
    {
        if( null == INSTANCE ) {
            INSTANCE = new IdeaStarsRepository(localData);
        }
        return INSTANCE;
    }

    public void getIdeas(@NonNull final LoadIdeasCallback callback)
    {
        checkNotNull(callback);

        ideaStarsData.getIdeas(callback);
    }

    public void deleteAll()
    {
        ideaStarsData.deleteAll();
    }

    public void saveIdeas(@NonNull Ideas ideas)
    {
        ideaStarsData.saveIdeas(ideas);
    }

    public void mergeIdeas(@NonNull Ideas ideas)
    {
        ideaStarsData.mergeIdeas(ideas);
    }

    public void saveIdea(@NonNull Idea idea) { ideaStarsData.saveIdea(idea); }

    public void saveWord(@NonNull Word word) { ideaStarsData.saveWord(word); }

    public void saveItem(@NonNull Item item) { ideaStarsData.saveItem(item); }

    public void deleteIdeas(long[] ideaIds) { ideaStarsData.deleteIdeas(ideaIds); }

    public void deleteWords(long[] wordIds)
    {
        ideaStarsData.deleteWords(wordIds);
    }

    public void deleteItems(long[] itemIds)
    {
        ideaStarsData.deleteItems(itemIds);
    }
}
