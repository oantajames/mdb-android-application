package mdb.com.util;

import android.support.annotation.CallSuper;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author james on 9/13/17.
 */

public class DisposingObserver<T> implements Observer<T> {

    @Override
    @CallSuper
    public void onSubscribe(@NonNull Disposable d) {
        DisposableManager.add(d);
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
