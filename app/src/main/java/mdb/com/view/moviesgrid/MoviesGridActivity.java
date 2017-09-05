package mdb.com.view.moviesgrid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

import android.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;

import mdb.com.R;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.di.HasComponent;
import mdb.com.di.component.DaggerMoviesGridComponent;
import mdb.com.di.component.MoviesGridComponent;
import mdb.com.di.module.MoviesGridModule;
import mdb.com.sync.Sort;
import mdb.com.view.BaseActivity;
import mdb.com.view.moviedetails.MovieDetailsActivity;
import mdb.com.view.moviesgrid.util.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MoviesGridActivity extends BaseActivity implements HasComponent<MoviesGridComponent>, OnItemSelectedListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MoviesGridActivity.class);
    }

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
        //TODO : fix the freaking toolbar layout!

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(FragmentMoviesList.newInstance(String.valueOf(Sort.TOP_RATED)), getString(R.string.top_rated));
        adapter.addFrag(FragmentMoviesList.newInstance(String.valueOf(Sort.MOST_POPULAR)), getString(R.string.most_popular));
        adapter.addFrag(FragmentMoviesList.newInstance(String.valueOf(Sort.TOP_RATED)), getString(R.string.my_favorites));
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

    @Override
    public void onItemSelected(MovieEntity movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ENTITY, movie);
        startActivity(intent);
    }

    class ViewPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.app.Fragment getItem(int position) {
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
