package com.example.ideastars.views.Top;

import android.support.annotation.NonNull;

import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.data.models.Ideas;

/**
 * Created by hiyakayoyayo on 2017/05/12.
 */

public class TopContract {
    public interface ITopView extends ItemListContract.IItemListView {
        void setPresenter( ITopPresenter presenter );

        void onPageIdea(@NonNull long ideaId);

        void showIdeas(Ideas ideas);
        void showIdeasError();

        void removeAt(int position);

        void setLoadingIndicator(boolean enable);
    }

    public interface ITopPresenter extends ItemListContract.IItemListListener
    {
        void start();
    }

    public interface ITopActivity
    {
    }
}
