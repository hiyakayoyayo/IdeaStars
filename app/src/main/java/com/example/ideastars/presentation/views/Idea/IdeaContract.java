package com.example.ideastars.views.Idea;

import android.support.annotation.NonNull;

import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.data.models.Idea;

public class IdeaContract {
    public interface IIdeaView extends ItemListContract.IItemListView {
        void setPresenter( IIdeaPresenter presenter );

        void onPageWord(@NonNull long ideaId,@NonNull long wordId);

        void showWords(Idea ideas);
        void showWordsError();

        void onPageSetIdeaName();

        void removeAt(int position);

        void setLoadingIndicator(boolean enable);
    }

    public interface IIdeaWordView extends ItemListContract.IItemListView {
        void setPresenter( IIdeaPresenter presenter );

        void onPageWord(@NonNull long ideaId,@NonNull long wordId);

        void showWords(Idea ideas);
        void showWordsError();

        void removeAt(int position);

        void setLoadingIndicator(boolean enable);
    }

    public interface IIdeaPresenter extends ItemListContract.IItemListListener
    {
        void start();
        void changeTitle(String title);
        void onPageSetIdeaName();
    }

    public interface IIdeaActivity
    {
        void changeTitle(String title);
        IdeaContract.IIdeaPresenter getPresenter();
    }
}
