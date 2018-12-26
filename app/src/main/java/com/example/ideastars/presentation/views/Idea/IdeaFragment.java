package com.example.ideastars.views.Idea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ideastars.R;
import com.example.ideastars.presentation.fragments.ItemListContract;
import com.example.ideastars.presentation.fragments.SetNameFragment;
import com.example.ideastars.presentation.fragments.ViewListItem;
import com.example.ideastars.utils.CustomViewPager;
import com.example.ideastars.utils.VerboseFragment;
import com.example.ideastars.views.SetIdea.SetIdeaActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class IdeaFragment extends VerboseFragment implements ViewPager.OnPageChangeListener, IdeaContract.IIdeaView
{
    public static final String ARG_IDEA_ID = "idea_id";
    public static final String ARG_PARENT_ID = "parent_id";

    private IdeaContract.IIdeaPresenter mPresenter = null;
    private Context mContext = null;

    private long mIdeaId;
    private int mParentId;

    public static IdeaFragment newInstance(@NonNull long ideaId, int parentId )
    {
        IdeaFragment fragment = new IdeaFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_IDEA_ID, ideaId);
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
            mParentId = getArguments().getInt(ARG_PARENT_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_idea, container, false);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        CustomViewPager viewPager = (CustomViewPager) root.findViewById(R.id.pager);

        IdeaPagerAdapter adapter = new IdeaPagerAdapter(getFragmentManager(),mIdeaId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

        for( int n = 0; n < adapter.getCount(); ++n ) {
            View tabView = inflater.inflate(R.layout.idea_tab, null);
            TabLayout.Tab tab = tabLayout.getTabAt(n);
            tab.setCustomView(tabView);
            View view = tab.getCustomView();
            TextView tab_text = (TextView)view.findViewById(R.id.tab_text);
            ImageView tab_image = (ImageView)view.findViewById(R.id.tab_image);
            tab_text.setText(adapter.TEXTS[n]);
            tab_image.setImageResource(adapter.ICONS[n]);
        }

        return root;
    }

    @Override
    public void setPresenter( IdeaContract.IIdeaPresenter presenter )
    {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPageSetIdeaName()
    {
        Intent intent = SetIdeaActivity.newInstance(getActivity(),mIdeaId,mParentId);
        getActivity().startActivityForResult(intent, 0);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    public void onPageSelected(int position)
    {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    public void onPageScrollStateChanged(int state)
    {

    }
}
