package com.example.ideastars.views.Idea;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ideastars.R;

public class IdeaPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 3;

    private long mIdeaId;

    public static final int[] ICONS = {
        R.drawable.ic_lightbulb_outline,
        R.drawable.ic_stars,
        R.drawable.ic_list,
    };

    public static final int[] TEXTS = {
        R.string.title_idea,
        R.string.title_star,
        R.string.title_ranking,
    };

    public IdeaPagerAdapter(FragmentManager fm, long ideaId) {
        super(fm);
        mIdeaId = ideaId;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch( position ) {
            case 0:
                return IdeaWordFragment.newInstance(mIdeaId);
            case 1:
                return IdeaStarFragment.newInstance(mIdeaId);
            case 2:
                return IdeaRankingFragment.newInstance(mIdeaId);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
