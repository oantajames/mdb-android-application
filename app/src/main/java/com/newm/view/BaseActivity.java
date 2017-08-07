package com.newm.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.newm.NewmApplication;
import com.newm.di.component.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((NewmApplication) getApplication()).getApplicationComponent();
    }

}
