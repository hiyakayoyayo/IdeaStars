package com.example.ideastars.views.Top;

import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;

import static com.google.common.base.Preconditions.checkNotNull;

public class TopPresenter implements TopContract.ITopPresenter {

    private IdeaStarsData mSaveData;
    private TopContract.ITopView mView;
    private TopContract.ITopActivity mActivity;

    public TopPresenter(IdeaStarsData saveData, TopContract.ITopActivity activity, TopContract.ITopView topView )
    {
        mSaveData = checkNotNull(saveData);
        mActivity = checkNotNull( activity );
        mView = checkNotNull( topView );
        mView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loading();
    }

    public void loading()
    {
        mSaveData.getIdeas(new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                mView.showIdeas(ideas);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showIdeasError();
            }
        });
    }

/// event trigger

    @Override
    public void OnColorClick(ViewListItem viewListItem) {}

    @Override
    public void OnClick(ViewListItem viewListItem)
    {
        mView.onPageIdea( viewListItem.getId() );
    }

    @Override
    public void onItemDismiss(int position) {
        mView.removeAt(position);
    }
}
