package com.mapzen.pelias;

import org.robolectric.annotation.HiddenApi;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.internal.Shadow;

import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Copy of {@link org.robolectric.shadows.ShadowInputMethodManager} that adds the hidden method
 * {@link #showSoftInputUnchecked(int, android.os.ResultReceiver)} and converts
 * {@link #isSoftInputVisible()} to a static method.
 */
@Implements(value = InputMethodManager.class, callThroughByDefault = false)
public class ShadowInputMethodManager {

    private static boolean softInputVisible;

    @HiddenApi @Implementation
    static public InputMethodManager peekInstance() {
        return Shadow.newInstanceOf(InputMethodManager.class);
    }

    @Implementation
    public boolean showSoftInput(View view, int flags) {
        return showSoftInput(view, flags, null);
    }

    @Implementation
    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver) {
        softInputVisible = true;
        return true;
    }

    @HiddenApi @Implementation
    public void showSoftInputUnchecked(int flags, ResultReceiver resultReceiver) {
        softInputVisible = true;
    }

    @Implementation
    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags) {
        return hideSoftInputFromWindow(windowToken, flags, null);
    }

    @Implementation
    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags,
            ResultReceiver resultReceiver) {
        softInputVisible = false;
        return true;
    }

    @Implementation
    public void toggleSoftInput(int showFlags, int hideFlags) {
        softInputVisible = !softInputVisible;
    }

    public static boolean isSoftInputVisible() {
        return softInputVisible;
    }
}
