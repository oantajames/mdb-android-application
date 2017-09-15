package mdb.com.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import javax.inject.Inject;
import mdb.com.R;
import mdb.com.repository.MoviesRepository;
import mdb.com.view.base.BaseActivity;
import mdb.com.view.moviesgrid.MoviesGridActivity;

/**
 * @author james on 8/31/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int delay = 3000;
    //todo - make a movies call on this screen and store the data in the adapter

    @Bind(R.id.splash_screen_layout)
    ViewGroup layoutSplashScreen;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        context = this;
        layoutSplashScreen.postDelayed(() -> {
            Intent intent = MoviesGridActivity.getCallingIntent(context);
            startActivity(intent);
            finish();
        }, delay);
    }
}
