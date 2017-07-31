package com.newm.util.loader;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;


public class RetrofitLoaderManager {

    public static <D, R> void restart(final LoaderManager manager, final int loaderId, final RetrofitLoader<D, R> loader, final Callback<D> callback) {
        manager.restartLoader(loaderId, Bundle.EMPTY, new LoaderCallbacks<Response<D>>() {
            @Override
            public Loader<Response<D>> onCreateLoader(int id, Bundle args) {
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Response<D>> loader, Response<D> data) {
                if (data.hasError()) {
                    callback.onFailure(data.getException());

                } else {
                    callback.onSuccess(data.getResult());
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<D>> loader) {
                //Nothing to do here
            }
        });
    }

}
