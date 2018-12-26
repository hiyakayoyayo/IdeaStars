package com.example.ideastars.views.Word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ideastars.Injection;
import com.example.ideastars.R;
import com.example.ideastars.utils.ActivityUtils;
import com.example.ideastars.views.Idea.IdeaActivity;
import com.example.ideastars.views.Idea.IdeaContract;
import com.example.ideastars.views.Idea.IdeaFragment;
import com.example.ideastars.views.Idea.IdeaPresenter;
import com.example.ideastars.views.SetIdea.SetIdeaActivity;
import com.example.ideastars.views.SetItem.SetItemActivity;
import com.example.ideastars.views.SetWord.SetWordActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class WordActivity extends AppCompatActivity implements WordContract.IWordActivity {

    public static String IDEA_ID = "idea_id";
    public static String WORD_ID = "word_id";

    private WordContract.IWordPresenter mPresenter;

    private long mIdeaId;
    private long mWordId;

    public Context getContext() {
        return (Context)this;
    }
    public AppCompatActivity getActivity() { return this; }

    public static Intent newInstance(FragmentActivity activity, long idea_id, long word_id )
    {
        checkNotNull(activity);
        Intent intent = new Intent(activity,WordActivity.class);
        intent.putExtra( IDEA_ID, idea_id );
        intent.putExtra( WORD_ID, word_id );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIdeaId = getIntent().getLongExtra(IDEA_ID,-1);
        mWordId = getIntent().getLongExtra(WORD_ID,-1);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SetItemActivity.newInstance(getActivity(),mIdeaId,mWordId,-1);
                getActivity().startActivityForResult(intent,0);
            }
        });

        WordFragment wordFragment =
                (WordFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (wordFragment == null) {
            // Create the fragment
            wordFragment = WordFragment.newInstance(mIdeaId, mWordId, SetWordActivity.PARENT_ID_WORD);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), wordFragment, R.id.contentFrame);
        }

        mPresenter = new WordPresenter( mIdeaId, mWordId, Injection.provideIdeaStarsRepository(getApplicationContext()), this, wordFragment );

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
                mPresenter.onPageSetWordName();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
