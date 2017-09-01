package mdb.com.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * @author james on 8/4/17.
 */

public class ImageUtils {

    public static void getPrimaryImageColorsAsync(Bitmap bitmap, PalleteListener listener) {
        Palette.from(bitmap).generate(listener::onColorGenerate);
    }

    public static void getBitmapFromUrl(Uri url, BitmapRequestListener listener, Context context) {
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(url)
                .setAutoRotateEnabled(true)
                .build();
        ImagePipeline pipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = pipeline.fetchDecodedImage(request, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (dataSource.isFinished() && bitmap != null) {
                    Bitmap image = Bitmap.createBitmap(bitmap);
                    listener.returnBitmap(image);
                    dataSource.close();
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (dataSource != null) {
                    dataSource.close();
                }
                listener.bitmapFailure();
            }
        }, CallerThreadExecutor.getInstance());
    }

    public interface PalleteListener {
        void onColorGenerate(Palette palette);
    }

    public interface BitmapRequestListener {
        void returnBitmap(Bitmap bitmap);

        void bitmapFailure();
    }
}
