package mdb.com.view.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.trello.navi2.component.NaviActivity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.navi.NaviLifecycle;
import javax.inject.Inject;
import mdb.com.MdbApplication;
import mdb.com.di.component.ApplicationComponent;
import mdb.com.view.BaseRepository;

public abstract class BaseActivity extends NaviActivity {

    private LifecycleProvider<ActivityEvent> provider = NaviLifecycle.createActivityLifecycleProvider(this);

    @Inject
    @NonNull
    public BaseRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        repository.setLifeCycle(provider);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MdbApplication) getApplication()).getApplicationComponent();
    }

}
