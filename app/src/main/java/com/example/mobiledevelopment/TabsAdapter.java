package com.example.mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobiledevelopment.fragments.EmptyFragment;
import com.example.mobiledevelopment.fragments.PanelsListFragment;
import com.example.mobiledevelopment.fragments.ProfileFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    private final String[] mTabs = new String[]{"Panels", "Tab 2", "Profile"};

    public TabsAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PanelsListFragment();
            case 1:
                return new EmptyFragment();
            case 2:
                return new ProfileFragment();
        }
        return new EmptyFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }

    @Override
    public int getCount() {
        return mTabs.length;
    }
}
