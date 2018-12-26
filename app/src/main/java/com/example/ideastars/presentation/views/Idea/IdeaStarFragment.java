package com.example.ideastars.views.Idea;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideastars.R;
import com.example.ideastars.utils.VerboseFragment;

public class IdeaStarFragment extends VerboseFragment
{
    public static final String ARG_IDEA_ID = "idea_id";

    private IdeaContract.IIdeaPresenter mPresenter = null;
    private Context mContext = null;

    public static IdeaStarFragment newInstance(@NonNull long ideaId )
    {
        IdeaStarFragment fragment = new IdeaStarFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_IDEA_ID, ideaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_idea_star, container, false);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if( mContext instanceof IdeaContract.IIdeaActivity) {
            mPresenter = ((IdeaContract.IIdeaActivity) mContext).getPresenter();
        }
    }

}
