package com.example.ideastars.presentation.fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ideastars.R;
import com.example.ideastars.utils.ItemTouchHelperAdapter;
import com.example.ideastars.utils.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

public class ItemListContract
{
    public enum ItemType
    {
        IDEA,
        WORD,
        ITEM
    }

    public enum ItemMode
    {
        SELECT,
        DELETE
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder
    {
        private View view;
        private FrameLayout frame;
        private Button btnColor;
        private TextView name;

        public ListViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            frame = (FrameLayout) view.findViewById(R.id.frame);
            btnColor = (Button) view.findViewById(R.id.colorChange);
            name = (TextView) view.findViewById(R.id.textView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public static class ItemListAdapter extends RecyclerView.Adapter<ListViewHolder> implements ItemTouchHelperAdapter
    {
        private ArrayList<ViewListItem> mItems;
        private IItemListListener mPresenter;
        private ItemType mItemType;
        private ItemMode mItemMode;

        public ItemListAdapter(ArrayList<ViewListItem> mData, ItemType itemType)
        {
            this.mItems = mData;
            this.mItemType = itemType;
            this.mItemMode = ItemMode.SELECT;
        }

        public void setListener(IItemListListener listener)
        {
            this.mPresenter = listener;
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            return new ListViewHolder(view);
        }

        public void replaceData(ArrayList<ViewListItem> items) {
            setList(items);
            notifyDataSetChanged();
        }

        public void changeItemMode( ItemMode itemMode )
        {
            if( mItemMode == itemMode ) {
                return;
            }
            mItemMode = itemMode;
            notifyDataSetChanged();
        }

        private void setList(ArrayList<ViewListItem> items) {
            mItems = checkNotNull(items);
        }

        @Override
        public void onBindViewHolder(final ListViewHolder holder, int position) {
            final ViewListItem viewListItem = mItems.get(position);
            boolean bCb = viewListItem.isSelected();
            holder.name.setText(viewListItem.getName());
            holder.btnColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.OnColorClick(viewListItem);
                }
            });
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( null != mPresenter) {
                        mPresenter.OnClick(viewListItem);
                    }
                }
            });
        }

        public ArrayList<ViewListItem> getViewItems(){ return mItems; }

        public void addAt(int position,ViewListItem item)
        {
            mItems.add(position,item);
            notifyItemInserted(position);
        }

        public void removeAt(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mItems.size());
        }

        @Override
        public void onItemDismiss(int position) {
            mPresenter.onItemDismiss(position);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(mItems, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    public interface IItemListView{

    }

    public interface IItemListListener {

        void OnColorClick(ViewListItem viewListItem);

        void OnClick(ViewListItem viewListItem);

        void onItemDismiss(int position);

    }
}
