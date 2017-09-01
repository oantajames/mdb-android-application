package mdb.com.view;

import android.support.v7.app.AppCompatActivity;
import mdb.com.MdbApplication;
import mdb.com.di.component.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((MdbApplication) getApplication()).getApplicationComponent();
    }

}
