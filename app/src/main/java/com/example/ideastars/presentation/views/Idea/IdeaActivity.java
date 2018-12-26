package com.example.ideastars.views.Idea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ideastars.Injection;
import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.utils.ActivityUtils;
import com.example.ideastars.utils.VerboseActivity;
import com.example.ideastars.views.SetIdea.SetIdeaActivity;
import com.example.ideastars.views.SetWord.SetWordActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class IdeaActivity extends VerboseActivity implements IdeaContract.IIdeaActivity {

    public static String IDEA_ID = "_idea_id";
    private IdeaContract.IIdeaPresenter mPresenter;

    private long mIdeaId;

    public Context getContext() {
        return (Context)this;
    }
    public AppCompatActivity getActivity() { return this; }

    public static Intent newInstance(FragmentActivity activity, long idea_id )
    {
        checkNotNull(activity);
        Intent intent = new Intent(activity,IdeaActivity.class);
        intent.putExtra( IDEA_ID, idea_id );
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIdeaId = getIntent().getLongExtra(IDEA_ID,-1);

        // add a idea data
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SetWordActivity.newInstance(getActivity(),mIdeaId,-1,SetWordActivity.PARENT_ID_IDEA);
                startActivityForResult(intent,0);
            }
        });

        IdeaFragment ideaFragment =
                (IdeaFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (ideaFragment == null) {
            // Create the fragment
            ideaFragment = IdeaFragment.newInstance(mIdeaId, SetIdeaActivity.PARENT_ID_IDEA);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), ideaFragment, R.id.contentFrame);
        }

        mPresenter = new IdeaPresenter( mIdeaId, Injection.provideIdeaStarsRepository(getApplicationContext()), this, ideaFragment );

    }

    @Override
    public void changeTitle(String title)
    {
        setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_bar_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit:
                mPresenter.onPageSetIdeaName();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public IdeaContract.IIdeaPresenter getPresenter()
    {
        return mPresenter;
    }

}
