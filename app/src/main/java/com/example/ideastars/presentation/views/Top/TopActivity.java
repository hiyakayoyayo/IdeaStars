package com.example.ideastars.views.Top;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;

import com.example.ideastars.R;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.document.IdeaStarsDocumentData;
import com.example.ideastars.data.models.local.IdeaStarsLocalData;
import com.example.ideastars.utils.ActivityUtils;

import com.example.ideastars.utils.VerboseActivity;
import com.example.ideastars.views.SetIdea.SetIdeaActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class TopActivity extends VerboseActivity implements NavigationView.OnNavigationItemSelectedListener, TopContract.ITopActivity
{

    public Context getContext() {
        return (Context)this;
    }
    public AppCompatActivity getActivity() { return this; }
// View Logic

    private static final int SAVE_DOCUMENT_REQUEST = 1;
    private static final int LOAD_DOCUMENT_REQUEST = 2;

    public interface TopActivityCallback
    {
        public void onOk(String str);
        public void onCancel();
    }

    private DrawerLayout mDrawerLayout;

    private TopPresenter mTopPresenter;


    public static Intent newInstance( FragmentActivity activity )
    {
        checkNotNull(activity);
        Intent intent = new Intent(activity,TopActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // add a idea data
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = SetIdeaActivity.newInstance(getActivity(),-1,SetIdeaActivity.PARENT_ID_TOP);
            getActivity().startActivityForResult(intent, 0);
            }
        });

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        TopFragment topFragment =
                (TopFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (topFragment == null) {
            // Create the fragment
            topFragment = new TopFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), topFragment, R.id.contentFrame);
        }

        mTopPresenter = new TopPresenter( IdeaStarsLocalData.getInstance(getContext()), this, topFragment );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_save:
                                {
                                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                    intent.setType("text/plain");
                                    startActivityForResult(intent, SAVE_DOCUMENT_REQUEST);
                                }
                                break;
                            case R.id.nav_load:
                                {
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.setType("text/plain");
                                    startActivityForResult(intent, LOAD_DOCUMENT_REQUEST);
                                }
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void setLoadingIndicator( boolean active )
    {

    }

// Business Logic

    private Uri mCachedUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SAVE_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;
            mCachedUri = data.getData();
            setLoadingIndicator(true);
            IdeaStarsLocalData.getInstance(this).getIdeas(new IdeaStarsData.LoadIdeasCallback() {
                @Override
                public void onIdeasLoaded(Ideas ideas) {
                    IdeaStarsDocumentData.getInstance(getApplicationContext()).saveIdeas(mCachedUri.toString(), ideas, new IdeaStarsData.SaveIdeasCallback() {
                        @Override
                        public void onSaved() {
                            // Success
                            View view = (View) getActivity().findViewById(R.id.nav_save);
                            Snackbar.make(view, "Save Successful.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        @Override
                        public void onFailed() {
                            // Failed
                        }
                    });
                }

                @Override
                public void onDataNotAvailable() {
                    View view = (View) getActivity().findViewById(R.id.nav_save);
                    Snackbar.make(view, "Cant Save Data...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        if (requestCode == LOAD_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;
            IdeaStarsDocumentData.getInstance(getApplicationContext()).getIdeas( data.getData().toString(), new IdeaStarsData.LoadIdeasCallback() {
                @Override
                public void onIdeasLoaded(Ideas ideas) {
                    IdeaStarsLocalData.getInstance(getContext()).mergeIdeas(ideas);
                }

                @Override
                public void onDataNotAvailable() {
                    View view = (View) getActivity().findViewById(R.id.nav_load);
                    Snackbar.make(view, "Data Not Available...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }
}
