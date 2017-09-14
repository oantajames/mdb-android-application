package mdb.com.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import mdb.com.MdbApplication;
import mdb.com.di.component.ApplicationComponent;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MdbApplication) getApplication()).getApplicationComponent();
    }

}
