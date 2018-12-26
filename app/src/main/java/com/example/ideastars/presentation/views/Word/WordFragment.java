package com.example.ideastars.views.Word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.example.ideastars.data.models.Item;
import com.example.ideastars.data.models.Word;
import com.example.ideastars.utils.SimpleItemTouchHelperCallback;
import com.example.ideastars.views.SetItem.SetItemActivity;
import com.example.ideastars.views.SetWord.SetWordActivity;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class WordFragment extends Fragment implements WordContract.IWordView {
    public static final String ARG_IDEA_ID = "idea_id";
    public static final String ARG_WORD_ID = "word_id";
    public static final String ARG_PARENT_ID = "parent_id";

    private WordContract.IWordPresenter mPresenter = null;
    private Context mContext = null;
    private ItemListContract.ItemListAdapter mItemListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private LinearLayout mWordNameView;
    private LinearLayout mItemsView;
    private LinearLayout mNoItemsView;
    private RecyclerView mRecycleView;

    private Snackbar mSnackbar;

    private long mIdeaId;
    private long mWordId;
    private int mParentId;

    public static WordFragment newInstance(@NonNull long ideaId, @NonNull long wordId, int parentId )
    {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_IDEA_ID, ideaId);
        args.putLong(ARG_WORD_ID, wordId);
        args.putInt(ARG_PARENT_ID, parentId);
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
            mWordId = getArguments().getLong(ARG_WORD_ID);
            mParentId = getArguments().getInt(ARG_PARENT_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_word, container, false);

        mItemsView = (LinearLayout) root.findViewById(R.id.items);
        mNoItemsView = (LinearLayout) root.findViewById(R.id.noItems);

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
        if( mContext instanceof WordContract.IWordActivity) {
            mItemListAdapter.setListener(mPresenter);
        }
    }

    @Override
    public void setPresenter( WordContract.IWordPresenter presenter )
    {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void onPageItem(@NonNull long ideaId, @NonNull long wordId, @NonNull long itemId )
    {
        Intent intent = SetItemActivity.newInstance(getActivity(),ideaId,wordId,itemId);
        startActivityForResult(intent,0);
    }

    @Override
    public void onPageSetWordName()
    {
        Intent intent = SetWordActivity.newInstance(getActivity(),mIdeaId,mWordId,mParentId);
        getActivity().startActivityForResult(intent,0);
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
                        Injection.provideIdeaStarsRepository(mContext).deleteItems(new long[]{ item.getId() });
                    }
                } );
        mSnackbar.show();
        mItemListAdapter.removeAt(position);
    }

    @Override
    public void showItems( Word word )
    {
        mPresenter.changeTitle(word.getName());

        if( 0 == word.getItems().length ) {
            showNotFoundItems();
            return;
        }
        setItems(word);
        mItemsView.setVisibility(View.VISIBLE);
        mNoItemsView.setVisibility(View.GONE);
    }

    public void setItems(Word word)
    {
        Item[] items = word.getItems();
        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();
        for (int i = 0; i < items.length; i++) {
            ViewListItem item = new ViewListItem( false, items[i].getId(), items[i].getName() );
            data.add(item);
        }
        mItemListAdapter.replaceData(data);
    }

    @Override
    public void showItemsError()
    {
        Log.d("Error","showIdeasError!");
    }

    private void showNotFoundItems()
    {
        mItemsView.setVisibility(View.GONE);
        mNoItemsView.setVisibility(View.VISIBLE);
    }
}
