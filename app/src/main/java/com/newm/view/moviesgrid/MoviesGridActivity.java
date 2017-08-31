package com.newm.view.moviesgrid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.newm.R;
import com.newm.di.HasComponent;
import com.newm.di.component.DaggerMoviesGridComponent;
import com.newm.di.component.MoviesGridComponent;
import com.newm.di.module.MoviesGridModule;
import com.newm.view.BaseActivity;
import java.util.ArrayList;
import java.util.List;

import static com.newm.view.moviesgrid.FragmentMoviesList.*;
import static com.newm.view.moviesgrid.FragmentMoviesList.MOST_POPULAR;
import static com.newm.view.moviesgrid.FragmentMoviesList.TOP_RATED;

public class MoviesGridActivity extends BaseActivity implements HasComponent<MoviesGridComponent> {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MoviesGridActivity.class);
    }


    public static final int MOVIE_LOADER_ID = 10;
    public static String MOVIE_ENTITY = "movie_entity";

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;

    private DaggerMoviesGridComponent moviesGridComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        inject();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabLayoutListener();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        initTabIcons();
        //default position
        viewPager.setCurrentItem(1);
    }

    private void inject() {
        moviesGridComponent = (DaggerMoviesGridComponent) DaggerMoviesGridComponent.builder()
                .applicationComponent(getApplicationComponent())
                .moviesGridModule(new MoviesGridModule())
                .build();
        moviesGridComponent.inject(this);
    }

    private void initTabIcons() {
        TextView mostPopular = (TextView) LayoutInflater.from(this).inflate(R.layout.item_custom_tab, null);
        mostPopular.setText(getString(R.string.most_popular));
        tabLayout.getTabAt(0).setCustomView(mostPopular);

        TextView topRated = (TextView) LayoutInflater.from(this).inflate(R.layout.item_custom_tab, null);
        topRated.setText(getString(R.string.top_rated));
        tabLayout.getTabAt(1).setCustomView(topRated);

        TextView myFavorites = (TextView) LayoutInflater.from(this).inflate(R.layout.item_custom_tab, null);
        myFavorites.setText(getString(R.string.my_favorites));
        tabLayout.getTabAt(2).setCustomView(myFavorites);
    }

    @Override
    public MoviesGridComponent getComponent() {
        return moviesGridComponent;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(newInstance(MOST_POPULAR), getString(R.string.most_popular));
        adapter.addFrag(newInstance(TOP_RATED), getString(R.string.top_rated));
        adapter.addFrag(newInstance(MY_FAVORITES), getString(R.string.my_favorites));
        viewPager.setAdapter(adapter);
    }

    private void setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                textView.setBackground(getDrawable(R.drawable.round_green_filter_button_background));
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                textView.setSelected(false);
                textView.setBackground(getDrawable(R.drawable.round_green_button_with_stroke));
                textView.setTextColor(getResources().getColorStateList(R.color.color_mdb_green));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
