package com.example.ideastars.views.SetIdea;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.presentation.fragments.SetNamePresenter;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.Ideas;

public class SetIdeaPresenter implements SetNamePresenter
{
    private long mIdeaId;

    private  IdeaStarsRepository mIdeaStarsRepository;
    private SetNameFragment mView;

    public SetIdeaPresenter(long ideaId,
                            IdeaStarsRepository ideaStarsRepository,
                            SetNameFragment view)
    {
        mIdeaId = ideaId;
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
                mView.show( ideas.getIdea(mIdeaId) );
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

        if( isNewIdea() ) {
            createIdea(name);
        } else {
            updateIdea(name);
        }
    }

    private void createIdea( String name )
    {
        Idea idea = new Idea();
        idea.setName(name);
        mIdeaStarsRepository.saveIdea(idea);
        mView.back();
    }

    private void updateIdea( String name )
    {
        Idea idea = new Idea();
        idea.setName(name);
        idea.setId(mIdeaId);
        mIdeaStarsRepository.saveIdea(idea);
        mView.back();
    }

    private boolean isNewIdea()
    {
        return (mIdeaId == -1);
    }

}
