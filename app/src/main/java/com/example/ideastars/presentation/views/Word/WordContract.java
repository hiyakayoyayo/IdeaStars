package com.example.ideastars.views.Word;

import android.support.annotation.NonNull;

import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.data.models.Word;

/**
 * Created by hiyakayoyayo on 2017/06/17.
 */

public class WordContract {
    public interface IWordView extends ItemListContract.IItemListView {
        void setPresenter( WordContract.IWordPresenter presenter );

        void onPageSetWordName();
        void onPageItem(@NonNull long ideaId,@NonNull long wordId,@NonNull long itemId);

        void showItems(Word word);
        void showItemsError();

        void removeAt(int position);

        void setLoadingIndicator(boolean enable);
    }

    public interface IWordPresenter extends ItemListContract.IItemListListener
    {
        void start();
        void onPageSetWordName();
        void changeTitle(String title);
    }

    public interface IWordActivity
    {
        void changeTitle(String title);
    }
}
