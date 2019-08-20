package com.developertest.sterlingproject.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developertest.sterlingproject.Adapter.MyPagerAdapter;
import com.developertest.sterlingproject.Model.Competition;
import com.developertest.sterlingproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueFragment extends Fragment {


    ViewPager viewPager;
    TabLayout tabLayout;
    private List<Fragment> tabFragmentList = new ArrayList<>();
    private String[] tabTitles;
    private LeagueTableFragment leagueTableFragment;
    private FixtureFragment fixtureFragment;
    private LeagueTeamFragment leagueTeamFragment;
    Competition competition;
    public LeagueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        try{
            competition = (Competition) bundle.getSerializable("competition");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_league, container, false);

        initControls(rootView);

        return rootView;
    }

    private void initControls(View rootView) {
        viewPager = rootView.findViewById(R.id.pager_view);
        tabLayout = rootView.findViewById(R.id.tab_view);

        Bundle bundle = new Bundle();
        bundle.putSerializable("competition", competition);

        leagueTableFragment = new LeagueTableFragment();
        fixtureFragment = new FixtureFragment();
        leagueTeamFragment = new LeagueTeamFragment();

        leagueTableFragment.setArguments(bundle);
        fixtureFragment.setArguments(bundle);
        leagueTeamFragment.setArguments(bundle);

        tabFragmentList.add(leagueTableFragment);
        tabFragmentList.add(fixtureFragment);
        tabFragmentList.add(leagueTeamFragment);

        tabTitles = getResources().getStringArray(R.array.tabs_name);

        viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager(), tabFragmentList, tabTitles));
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);

    }
}
