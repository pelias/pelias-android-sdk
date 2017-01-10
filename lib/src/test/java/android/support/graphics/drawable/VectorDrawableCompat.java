package android.support.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;

/**
 *
 */
abstract class VectorDrawableCommon extends android.graphics.drawable.Drawable {

}

public class VectorDrawableCompat extends android.support.graphics.drawable.VectorDrawableCommon {
  public static android.support.graphics.drawable.VectorDrawableCompat create(
      android.content.res.Resources r, int i, android.content.res.Resources.Theme t) {
    return null;
  }

  public static android.support.graphics.drawable.VectorDrawableCompat createFromXmlInner(
      android.content.res.Resources r, org.xmlpull.v1.XmlPullParser p, android.util.AttributeSet a,
      android.content.res.Resources.Theme t)
      throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
    return null;
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
