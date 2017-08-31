package com.newm.view;

import android.support.v7.app.AppCompatActivity;
import com.newm.MdbApplication;
import com.newm.di.component.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((MdbApplication) getApplication()).getApplicationComponent();
    }

}
