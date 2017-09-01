package mdb.com.util.loader;


import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class RetrofitLoader<D, R> extends AsyncTaskLoader<Response<D>> {

    private final R mService;

    private Response<D> cachedResponse;

    public RetrofitLoader(Context context, R service) {

        super(context);

        mService = service;
    }

    @Override
    public Response<D> loadInBackground() {

        try {
            final D data = call(mService);
            cachedResponse = Response.ok(data);

        } catch (Exception ex) {

            cachedResponse = Response.error(ex);
        }

        return cachedResponse;
    }

    @Override
    protected void onStartLoading() {

        super.onStartLoading();

        if (cachedResponse != null) {

            deliverResult(cachedResponse);
        }

        if (takeContentChanged() || cachedResponse == null) {

            forceLoad();
        }
    }

    @Override
    protected void onReset() {

        super.onReset();

        cachedResponse = null;
    }

    public abstract D call(R service);

}
