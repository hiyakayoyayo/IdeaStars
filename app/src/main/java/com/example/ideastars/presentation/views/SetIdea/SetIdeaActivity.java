package com.example.ideastars.views.SetIdea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.presentation.fragments.SetNamePresenter;
import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.local.IdeaStarsLocalData;
import com.example.ideastars.utils.ActivityUtils;
import com.example.ideastars.utils.VerboseActivity;
import com.example.ideastars.views.Idea.IdeaActivity;
import com.example.ideastars.views.Top.TopActivity;
import com.example.ideastars.views.Word.WordActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetIdeaActivity extends VerboseActivity
{
    public static final String IDEA_ID = "idea_id";
    public static final String PARENT_ID = "parent_id";
    public static final String PARENT_IDEA = "parent_idea";

    public static final int PARENT_ID_TOP = 1;
    public static final int PARENT_ID_IDEA = 2;
    public static final int PARENT_ID_WORD = 3;
    public static final int PARENT_ID_ITEM = 4;

    private SetIdeaPresenter mPresenter;
    private long mIdeaId;
    private int mParentId;

    static public Intent newInstance(@NonNull FragmentActivity activity, long ideaId, int parentId )
    {
        checkNotNull(activity);
        Intent intent = new Intent(activity, SetIdeaActivity.class);
        intent.putExtra(IDEA_ID,ideaId);
        intent.putExtra(PARENT_ID,parentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_idea);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIdeaId = getIntent().getLongExtra(IDEA_ID,-1);
        mParentId = getIntent().getIntExtra(PARENT_ID,1);

        SetNameFragment setNameFragment =
                (SetNameFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (setNameFragment == null) {
            // Create the fragment
            setNameFragment = SetNameFragment.newInstance(SetNameFragment.Type.IDEA,mIdeaId,-1,-1);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), setNameFragment, R.id.contentFrame);
        }

        mPresenter = new SetIdeaPresenter( mIdeaId, IdeaStarsRepository.getInstance( IdeaStarsLocalData.getInstance(getApplicationContext())), setNameFragment );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent;
                switch ( this.mParentId ) {
                    case PARENT_ID_WORD:
                        upIntent = WordActivity.newInstance(this,mIdeaId,-1);
                        break;
                    case PARENT_ID_IDEA:
                        upIntent = IdeaActivity.newInstance(this,mIdeaId);
                        break;
                    default:
                        upIntent = TopActivity.newInstance(this);
                        break;
                }
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
