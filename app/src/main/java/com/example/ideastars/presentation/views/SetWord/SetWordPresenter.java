package com.example.ideastars.views.SetWord;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.presentation.fragments.SetNamePresenter;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Word;

public class SetWordPresenter implements SetNamePresenter
{

    private long mIdeaId;
    private long mWordId;

    private  IdeaStarsRepository mIdeaStarsRepository;
    private SetNameFragment mView;

    public SetWordPresenter(long ideaId, long wordId,
                            IdeaStarsRepository ideaStarsRepository,
                            SetNameFragment view)
    {
        mIdeaId = ideaId;
        mWordId = wordId;
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
                mView.show( ideas.getIdea(mIdeaId).getWord(mWordId) );
            }

            @Override
            public void onDataNotAvailable()
            {
                mView.setLoadingIndicator(false);
                mView.showMissing( R.string.empty_ideas_message );
            }
        });
    }

    public void save( String name, int color )
    {
        if( name.isEmpty() ) {
            mView.showMissing( R.string.empty_idea_message );
            return;
        }

        if( isNewWord() ) {
            createWord(name,color);
        } else {
            updateWord(name,color);
        }
    }

    private void createWord( String name, int color )
    {
        Word word = new Word();
        word.setName(name);
        word.setColor(color);
        word.setIdeaId(mIdeaId);
        mIdeaStarsRepository.saveWord(word);
        mView.back();
    }

    private void updateWord( String name, int color )
    {
        Word word = new Word();
        word.setName(name);
        word.setColor(color);
        word.setId(mWordId);
        word.setIdeaId(mIdeaId);
        mIdeaStarsRepository.saveWord(word);
        mView.back();
    }

    private boolean isNewWord()
    {
        return (mWordId == -1);
    }

}

