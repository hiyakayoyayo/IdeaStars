package com.example.ideastars.views.Idea;

import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.Ideas;

import static com.google.common.base.Preconditions.checkNotNull;

public class IdeaPresenter implements IdeaContract.IIdeaPresenter {

    private long mIdeaId;
    private IdeaStarsRepository mRepository;
    private IdeaContract.IIdeaView mView;
    private IdeaContract.IIdeaActivity mActivity;

    public IdeaPresenter (long ideaId, IdeaStarsRepository repository, IdeaContract.IIdeaActivity activity, IdeaContract.IIdeaView ideaView )
    {
        mIdeaId = ideaId;
        mRepository = repository;
        mActivity = activity;
        mView = ideaView;
        mView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loading();
    }

    public void loading()
    {
        mRepository.getIdeas(new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                Idea idea = ideas.getIdea(mIdeaId);
                mView.showWords(idea);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showWordsError();
            }
        });
    }

    @Override
    public void changeTitle(String title)
    {
        mActivity.changeTitle(title);
    }

    @Override
    public void onPageSetIdeaName()
    {
        mView.onPageSetIdeaName();
    }

    @Override
    public void OnColorClick(ViewListItem viewListItem) {}

    @Override
    public void OnClick(ViewListItem viewListItem)
    {
        mView.onPageWord( mIdeaId, viewListItem.getId() );
    }

    @Override
    public void onItemDismiss(int position)
    {
        mView.removeAt(position);
    }

}
