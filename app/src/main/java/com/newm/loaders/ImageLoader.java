package com.newm.loaders;

import android.content.Context;
import android.net.Uri;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.newm.data.api.ApiConstants;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author james on 7/23/17.
 */
public class ImageLoader {

    public ImageLoader() {
    }

    private final PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
    private final ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.EMPTY);

    public final void loadImageToView(String urlOfImage, SimpleDraweeView simpleDraweeView) {
        simpleDraweeView.setController(
                controllerBuilder
                        .setOldController(simpleDraweeView.getController())
                        .setImageRequest(
                                imageRequestBuilder
                                        .setSource(Uri.parse(buildUrl(urlOfImage)))
                                        .build())
                        .build());

    }

    private String buildUrl(String url) {
        return ApiConstants.BASE_IMAGE_URL + url;
    }

}
