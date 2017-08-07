package com.newm.presenter.moviedetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import com.newm.util.ImageUtils;
import javax.inject.Inject;

/**
 * @author james on 8/7/17.
 */

public class MovieDetailsPresenterImpl implements MovieDetailsPresenter,
        ImageUtils.BitmapRequestListener, ImageUtils.PalleteListener {

    @Inject
    Context context;
    private Palette palette;
    private PresenterPaletteListener presenterPaletteListener;

    @Override
    public void onCreate(Uri imageUrl, PresenterPaletteListener presenterPaletteListener) {
        this.presenterPaletteListener = presenterPaletteListener;
        ImageUtils.getBitmapFromUrl(imageUrl, this, context);
    }

    @Override
    public void updateUiColors() {

    }

    @Override
    public void returnBitmap(Bitmap bitmap) {
        ImageUtils.getPrimaryImageColorsAsync(bitmap, this);
    }

    @Override
    public void bitmapFailure() {
        presenterPaletteListener.onPaletteFailure();
    }

    @Override
    public void onColorGenerate(Palette palette) {
        this.palette = palette;
        presenterPaletteListener.onPaletteRetrieved(palette);
    }

    public interface PresenterPaletteListener {
        void onPaletteRetrieved(Palette palette);

        void onPaletteFailure();
    }
}
