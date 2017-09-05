package mdb.com.view;

import android.support.v7.app.AppCompatActivity;
import com.trello.rxlifecycle.components.RxActivity;
import mdb.com.MdbApplication;
import mdb.com.di.component.ApplicationComponent;

public abstract class BaseActivity extends RxActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((MdbApplication) getApplication()).getApplicationComponent();
    }

}
