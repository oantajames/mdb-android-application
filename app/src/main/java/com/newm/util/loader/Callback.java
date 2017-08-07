package com.newm.util.loader;

import android.support.annotation.Nullable;

public interface Callback<D> {

    public abstract void onFailure(@Nullable Exception ex, @Nullable String message);

    public abstract void onSuccess(D result);
}
