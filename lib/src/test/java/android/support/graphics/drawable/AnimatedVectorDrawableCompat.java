package android.support.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;

/**
 *
 */
public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable {
  @Override public void start() {

  }

  @Override public void stop() {

  }

  @Override public boolean isRunning() {
    return false;
  }

  @Override public void draw(Canvas canvas) {

  }

  @Override public void setAlpha(int i) {

  }

  @Override public void setColorFilter(ColorFilter colorFilter) {

  }

  @Override public int getOpacity() {
    return PixelFormat.OPAQUE;
  }
}
