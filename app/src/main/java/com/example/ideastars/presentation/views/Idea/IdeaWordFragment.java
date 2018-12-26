package com.example.ideastars.views.Idea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideastars.Injection;
import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.Word;
import com.example.ideastars.utils.SimpleItemTouchHelperCallback;
import com.example.ideastars.utils.VerboseFragment;
import com.example.ideastars.views.Word.WordActivity;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hiyakayoyayo on 2017/06/30.
 */

public class IdeaWordFragment extends VerboseFragment implements IdeaContract.IIdeaWordView
{
    public static final String ARG_IDEA_ID = "idea_id";

    private IdeaContract.IIdeaPresenter mPresenter = null;
    private Context mContext = null;
    private ItemListContract.ItemListAdapter mItemListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private LinearLayout mWordsView;
    private LinearLayout mNoWordsView;
    private RecyclerView mRecycleView;

    private TextView mIdeaName;

    private Snackbar mSnackbar;

    private long mIdeaId;
    private int mParentId;

    public static IdeaWordFragment newInstance(@NonNull long ideaId )
    {
        IdeaWordFragment fragment = new IdeaWordFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_IDEA_ID, ideaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdeaId = getArguments().getLong(ARG_IDEA_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_idea_word, container, false);

        mWordsView = (LinearLayout) root.findViewById(R.id.words);
        mNoWordsView = (LinearLayout) root.findViewById(R.id.noWords);

        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();

        mRecycleView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mItemListAdapter = new ItemListContract.ItemListAdapter(data, ItemListContract.ItemType.WORD );

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
        if( mContext instanceof IdeaContract.IIdeaActivity) {
            mPresenter = ((IdeaContract.IIdeaActivity) mContext).getPresenter();
            mItemListAdapter.setListener(mPresenter);
        }
    }

    @Override
    public void setPresenter( IdeaContract.IIdeaPresenter presenter )
    {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void onPageWord(@NonNull long ideaId, @NonNull long wordId)
    {
        Intent intent = new Intent(mContext,WordActivity.class);
        intent.putExtra(WordActivity.IDEA_ID,ideaId);
        intent.putExtra(WordActivity.WORD_ID,wordId);
        startActivity(intent);
    }

    @Override
    public void setLoadingIndicator(boolean enable)
    {

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if( null != mSnackbar ) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void removeAt(final int position)
    {
        ArrayList<ViewListItem> items = mItemListAdapter.getViewItems();
        final ViewListItem item = items.get(position);

        if (mSnackbar != null) {
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
                        Injection.provideIdeaStarsRepository(mContext).deleteWords(new long[]{ item.getId() });
                    }
                } );
        mSnackbar.show();
        mItemListAdapter.removeAt(position);
    }

    @Override
    public void showWords( Idea idea )
    {
        mPresenter.changeTitle(idea.getName());

        if( 0 == idea.getWords().length ) {
            showNotFoundWords();
            return;
        }
        setWords(idea);
        mWordsView.setVisibility(View.VISIBLE);
        mNoWordsView.setVisibility(View.GONE);
    }

    public void setWords(Idea idea)
    {
        Word[] words = idea.getWords();
        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();
        for (int i = 0; i < words.length; i++) {
            ViewListItem item = new ViewListItem( false, words[i].getId(), words[i].getName(), words[i].getColor() );
            data.add(item);
        }
        mItemListAdapter.replaceData(data);
    }

    @Override
    public void showWordsError()
    {
        Log.d("Error","showIdeasError!");
    }

    private void showNotFoundWords()
    {
        mWordsView.setVisibility(View.GONE);
        mNoWordsView.setVisibility(View.VISIBLE);
    }

}
