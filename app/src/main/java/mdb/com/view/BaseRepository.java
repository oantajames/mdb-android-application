package mdb.com.view;

import android.support.annotation.NonNull;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.LifecycleProvider;
import javax.inject.Inject;


/**
 * @author james on 9/9/17.
 */

public class BaseRepository {

    private LifecycleProvider<ActivityEvent> provider;

    @Inject
    public BaseRepository() {
    }

    public void setLifeCycle(LifecycleProvider<ActivityEvent> lifecycleTransformer) {
        this.provider = lifecycleTransformer;
    }

    @NonNull
    public LifecycleProvider<ActivityEvent> getLifecycleProvider() {
        return provider;
    }

}
