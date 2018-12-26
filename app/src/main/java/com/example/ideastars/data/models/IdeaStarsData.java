package com.example.ideastars.data.models;

import android.support.annotation.NonNull;

/**
 * Created by hiyakayoyayo on 2017/04/26.
 */
public interface IdeaStarsData {

    interface LoadIdeasCallback {
        void onIdeasLoaded(Ideas ideas);
        void onDataNotAvailable();
    }

    interface SaveIdeasCallback {
        void onSaved();
        void onFailed();
    }

    void getIdeas(@NonNull LoadIdeasCallback callback);

    void deleteAll();

    void saveIdeas(@NonNull Ideas ideas);

    void mergeIdeas(@NonNull Ideas ideas);

    void saveIdea(Idea idea);

    void saveWord(Word word);

    void saveItem(Item item);

    void deleteIdeas(long[] ideaIds);

    void deleteWords(long[] wordIds);

    void deleteItems(long[] itemIds);
}
