package com.example.ideastars.views.Top;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ideastars.Injection;
import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.utils.SimpleItemTouchHelperCallback;
import com.example.ideastars.utils.VerboseFragment;
import com.example.ideastars.views.Idea.IdeaActivity;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class TopFragment extends VerboseFragment implements TopContract.ITopView
{

    private TopContract.ITopPresenter mPresenter = null;
    private Context mContext = null;
    private ItemListContract.ItemListAdapter mItemListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private LinearLayout mIdeasView;
    private LinearLayout mNoIdeasView;
    private RecyclerView mRecycleView;

    private Snackbar mSnackbar;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("lifecycle","TopFragment::onCreateView");
        View root = inflater.inflate(R.layout.fragment_top, container, false);

        mIdeasView = (LinearLayout) root.findViewById(R.id.ideas);
        mNoIdeasView = (LinearLayout) root.findViewById(R.id.noIdeas);

        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();

        mRecycleView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mItemListAdapter = new ItemListContract.ItemListAdapter(data, ItemListContract.ItemType.IDEA );

        mRecycleView.setAdapter(mItemListAdapter);
        mRecycleView.setLayoutManager(manager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mItemListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecycleView);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if( mContext instanceof TopContract.ITopActivity) {
            mItemListAdapter.setListener(mPresenter);
        }
    }

    @Override
    public void setPresenter( TopContract.ITopPresenter presenter )
    {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    public void setIdeas(Idea[] ideas)
    {
        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();
        for (int i = 0; i < ideas.length; i++) {
            ViewListItem item = new ViewListItem( false, ideas[i].getId(), ideas[i].getName() );
            data.add(item);
        }
        mItemListAdapter.replaceData(data);
    }

    @Override
    public void removeAt(final int position)
    {
        ArrayList<ViewListItem> items = mItemListAdapter.getViewItems();
        final ViewListItem item = items.get(position);

        if( null != mSnackbar ) {
            mSnackbar.dismiss();
        }
        mSnackbar = Snackbar.make( mRecycleView, "REMOVED", Snackbar.LENGTH_LONG )
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // add
                        mItemListAdapter.addAt(position,item);
                        mRecycleView.scrollToPosition(position);
                    }
                }).addCallback( new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event)
                    {
                        // remove
                        Injection.provideIdeaStarsRepository(mContext).deleteIdeas(new long[]{ item.getId() });
                    }
                } );
        mSnackbar.show();
        mItemListAdapter.removeAt(position);
    }

    @Override
    public void onPageIdea(@NonNull long ideaId)
    {
        Intent intent = IdeaActivity.newInstance((FragmentActivity) mContext,ideaId);
        intent.putExtra(IdeaActivity.IDEA_ID,ideaId);
        startActivity(intent);
    }

    @Override
    public void setLoadingIndicator(boolean enable)
    {

    }

    @Override
    public void showIdeas( Ideas ideas )
    {
        if( 0 == ideas.getIdeas().length ) {
            showNotFoundIdeas();
            return;
        }
        setIdeas(ideas.getIdeas());
        mIdeasView.setVisibility(View.VISIBLE);
        mNoIdeasView.setVisibility(View.GONE);
    }

    @Override
    public void showIdeasError()
    {
        Log.d("Error","showIdeasError!");
    }

    private void showNotFoundIdeas()
    {
        mIdeasView.setVisibility(View.GONE);
        mNoIdeasView.setVisibility(View.VISIBLE);
    }
}
