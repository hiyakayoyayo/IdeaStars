package com.example.ideastars.views.Idea;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.utils.VerboseFragment;

import java.util.ArrayList;

public class IdeaRankingFragment extends VerboseFragment
{
    public static final String ARG_IDEA_ID = "idea_id";

    private IdeaRankingFragment.RankingListener mListener = null;
    private IdeaContract.IIdeaPresenter mPresenter = null;
    private Context mContext = null;

    public static IdeaRankingFragment newInstance(@NonNull long ideaId )
    {
        IdeaRankingFragment fragment = new IdeaRankingFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_IDEA_ID, ideaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_idea_ranking, container, false);

        ArrayList<ViewListItem> data = new ArrayList<ViewListItem>();
        for (int i = 0; i < 15; i++) {
            ViewListItem item = new ViewListItem(false, i, "item" + i);
            data.add(item);
        }

        // Set up tasks view
        final HorizontalScrollView hScroll = (HorizontalScrollView) root.findViewById(R.id.hScroll);
        final RecyclerView reciclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        reciclerView.setHasFixedSize(true);
        //レイアウトマネージャ設定
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        reciclerView.setLayoutManager(manager);
        //アダプターを設定
        RecyclerView.Adapter adapter = new IdeaRankingFragment.RankingAdapter(data, mListener);
        reciclerView.setAdapter(adapter);

        reciclerView.setOnTouchListener( new View.OnTouchListener() { //inner scroll listener
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        hScroll.setOnTouchListener(new View.OnTouchListener() { //outer scroll listener
            private float mx, my, curX, curY;
            private boolean started = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                curX = event.getX();
                curY = event.getY();
                int dx = (int) (mx - curX);
                int dy = (int) (my - curY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (started) {
                            reciclerView.scrollBy(0, dy);
                            hScroll.scrollBy(dx, 0);
                        } else {
                            started = true;
                        }
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        reciclerView.scrollBy(0, dy);
                        hScroll.scrollBy(dx, 0);
                        started = false;
                        break;
                }
                return true;
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if( mContext instanceof ItemListContract.IItemListListener) {
            mListener = (IdeaRankingFragment.RankingListener)mContext;
        }
        if( mContext instanceof IdeaContract.IIdeaActivity) {
            mPresenter = ((IdeaContract.IIdeaActivity) mContext).getPresenter();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static class RankingViewHolder extends RecyclerView.ViewHolder
    {
        private View view;

        public RankingViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    private static class RankingAdapter extends RecyclerView.Adapter<IdeaRankingFragment.RankingViewHolder>
    {
        private ArrayList<ViewListItem> data;
        private IdeaRankingFragment.RankingListener listener;

        public RankingAdapter(ArrayList<ViewListItem> data, IdeaRankingFragment.RankingListener listener)
        {
            this.data = data;
            this.listener = listener;
        }

        @Override
        public IdeaRankingFragment.RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            return new IdeaRankingFragment.RankingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IdeaRankingFragment.RankingViewHolder holder, int position) {
            final ViewListItem viewListItem = data.get(position);
        }

        public ArrayList<ViewListItem> getViewItems(){ return data; }

        public void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public interface RankingListener {

        void OnClick(ViewListItem viewListItem);

    }

}
