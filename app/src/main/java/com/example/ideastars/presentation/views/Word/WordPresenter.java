package com.example.ideastars.views.Word;

import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Word;

public class WordPresenter implements WordContract.IWordPresenter {
    private long mIdeaId;
    private long mWordId;
    private IdeaStarsRepository mRepository;
    private WordContract.IWordView mView;
    private WordContract.IWordActivity mActivity;

    public WordPresenter (long ideaId, long wordId, IdeaStarsRepository repository, WordContract.IWordActivity activity, WordContract.IWordView ideaView )
    {
        mIdeaId = ideaId;
        mWordId = wordId;
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

    @Override
    public void changeTitle(String title)
    {
        mActivity.changeTitle(title);
    }

    public void loading()
    {
        mRepository.getIdeas(new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                Word word = ideas.getIdea(mIdeaId).getWord(mWordId);
                mView.showItems(word);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showItemsError();
            }
        });
    }

    @Override
    public void onPageSetWordName()
    {
        mView.onPageSetWordName();
    }

    @Override
    public void OnColorClick(ViewListItem viewListItem)
    {
    }

    @Override
    public void OnClick(ViewListItem viewListItem)
    {
        mView.onPageItem( mIdeaId, mWordId, viewListItem.getId() );
    }

    @Override
    public void onItemDismiss(int position) {
        mView.removeAt(position);
    }
}
