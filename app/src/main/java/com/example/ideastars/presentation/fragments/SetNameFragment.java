package com.example.ideastars.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideastars.R;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.Item;
import com.example.ideastars.data.models.Word;
import com.example.ideastars.utils.VerboseFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetNameFragment extends VerboseFragment {

    public enum Type {
        IDEA,
        WORD,
        ITEM
    };

    private static final String ARG_TYPE = "type";
    private static final String ARG_IDEA_ID = "idea_id";
    private static final String ARG_WORD_ID = "word_id";
    private static final String ARG_ITEM_ID = "item_id";

    private SetNamePresenter mPresenter;

    private Type mType;
    private int mIdeaId;
    private int mWordId;
    private int mItemId;

    private TextView mTitle;
    private LinearLayout mColorLayout;
    private Button mColorButton;

    public SetNameFragment() {
        // Required empty public constructor
    }

    public static SetNameFragment newInstance(Type type, long ideaId, long wordId, long itemId) {
        SetNameFragment fragment = new SetNameFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE,type);
        args.putLong(ARG_IDEA_ID, ideaId);
        args.putLong(ARG_WORD_ID, wordId);
        args.putLong(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = (Type) getArguments().getSerializable(ARG_TYPE);
            mIdeaId = getArguments().getInt(ARG_IDEA_ID);
            mWordId = getArguments().getInt(ARG_WORD_ID);
            mItemId = getArguments().getInt(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_set_name, container, false);

        mTitle = (TextView)root.findViewById(R.id.set_title);
        mColorLayout = (LinearLayout)root.findViewById(R.id.colorLayout);
        mColorButton = (Button)root.findViewById(R.id.colorButton);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.add_idea_add);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.save(mTitle.getText().toString(),mColorButton.getDrawingCacheBackgroundColor());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setPresenter( @NonNull SetNamePresenter presenter )
    {
        mPresenter = checkNotNull(presenter);
    }

    public void show( Idea idea )
    {
        // Idea do not have color
        mColorLayout.setVisibility(View.GONE);
        mTitle.setText(idea.getName());
    }

    public void show( Word word )
    {
        // Idea do not have color
        mColorLayout.setVisibility(View.VISIBLE);
        mTitle.setText(word.getName());
    }

    public void show( Item item )
    {
        // Idea do not have color
        mColorLayout.setVisibility(View.GONE);
        mTitle.setText(item.getName());
    }

    public void back()
    {
        getActivity().setResult(FragmentActivity.RESULT_OK);
        getActivity().finish();
    }

    public void showMissing( int id )
    {
        Snackbar.make(mTitle, getString(id), Snackbar.LENGTH_LONG).show();
    }

    public void setLoadingIndicator(boolean active)
    {
        if(active) {
            // now loading...
        } else {
            // load end
        }
    }

    private int getTypeId()
    {
        switch (mType) {
            case ITEM:
                return mItemId;
            case IDEA:
                return mIdeaId;
            case WORD:
                return mWordId;
        }
        return mIdeaId;
    }

}
