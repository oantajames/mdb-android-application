package com.newm.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.newm.NewmApplication;
import com.newm.di.component.ApplicationComponent;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getApplicationComponent().inject(this);
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link com.newm.NewmApplication}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((NewmApplication) getApplication()).getApplicationComponent();
    }

}
