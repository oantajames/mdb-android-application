package com.newm.util;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

/**
 * @author james on 8/3/17.
 */

public class GradientUtils {
//
//    private void fillCustomGradient(View v, Context context) {
//        final View view = v;
//        Drawable[] layers = new Drawable[1];
//
//        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
//            @Override
//            public Shader resize(int width, int height) {
//                LinearGradient lg = new LinearGradient(
//                        0,
//                        0,
//                        0,
//                        view.getHeight(),
//                        new int[]{
//                                context.getResources().getColor(R.color.color1), // please input your color from resource for color-4
//                                context.getResources().getColor(R.color.color2),
//                                context.getResources().getColor(R.color.color3),
//                                context.getResources().getColor(R.color.color4)},
//                        new float[]{0, 0.49f, 0.50f, 1},
//                        Shader.TileMode.CLAMP);
//                return lg;
//            }
//        };
//        PaintDrawable p = new PaintDrawable();
//        p.setShape(new RectShape());
//        p.setShaderFactory(sf);
//        p.setCornerRadii(new float[]{5, 5, 5, 5, 0, 0, 0, 0});
//        layers[0] = (Drawable) p;
//
//        LayerDrawable composite = new LayerDrawable(layers);
//        view.setBackgroundDrawable(composite);
//    }
}
