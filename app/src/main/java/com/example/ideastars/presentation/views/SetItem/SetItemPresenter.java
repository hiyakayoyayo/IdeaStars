package com.example.ideastars.views.SetItem;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.presentation.fragments.SetNamePresenter;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Item;

public class SetItemPresenter implements SetNamePresenter {
    private long mIdeaId;
    private long mWordId;
    private long mItemId;

    private IdeaStarsRepository mIdeaStarsRepository;
    private SetNameFragment mView;

    public SetItemPresenter(long ideaId, long wordId, long itemId,
                            IdeaStarsRepository ideaStarsRepository,
                            SetNameFragment view)
    {
        mIdeaId = ideaId;
        mWordId = wordId;
        mItemId = itemId;
        mIdeaStarsRepository = ideaStarsRepository;
        mView = view;
        mView.setPresenter( this );
    }

    @Override
    public void start()
    {
        open();
    }
    @Override
    public void edit()
    {

    }
    @Override
    public void delete()
    {

    }

    private void open()
    {
        mView.setLoadingIndicator(true);
        mIdeaStarsRepository.getIdeas(new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                mView.setLoadingIndicator(false);
                mView.show( ideas.getIdea(mIdeaId).getWord(mWordId).getItem(mItemId) );
            }

            @Override
            public void onDataNotAvailable()
            {
                mView.setLoadingIndicator(false);
                mView.showMissing( R.string.empty_ideas_message );
            }
        });
    }

    @Override
    public void save( String name, int color )
    {
        if( name.isEmpty() ) {
            mView.showMissing( R.string.empty_idea_message );
            return;
        }

        if( isNewItem() ) {
            createItem(name);
        } else {
            updateItem(name);
        }
    }

    private void createItem( String name )
    {
        Item item = new Item();
        item.setName(name);
        item.setId(mItemId);
        item.setWordId(mWordId);
        mIdeaStarsRepository.saveItem(item);
        mView.back();
    }

    private void updateItem( String name )
    {
        Item item = new Item();
        item.setName(name);
        item.setId(mItemId);
        item.setWordId(mWordId);
        mIdeaStarsRepository.saveItem(item);
        mView.back();
    }

    private boolean isNewItem()
    {
        return (mItemId == -1);
    }

}
