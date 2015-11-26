package com.mapzen.pelias.widget;

import com.mapzen.pelias.BuildConfig;
import com.mapzen.pelias.SimpleFeature;
import com.mapzen.pelias.SimpleFeatureTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.os.Parcel;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class AutoCompleteItemTest {
    @Test
    public void shouldBuildFromParcel() throws Exception {
        SimpleFeature simpleFeature = SimpleFeatureTest.getTestSimpleFeature();
        Parcel parcel = Parcel.obtain();
        simpleFeature.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        AutoCompleteItem item = new AutoCompleteItem(parcel);
        assertThat(item.getText()).isEqualTo("Test SimpleFeature");
        assertThat(item.getSimpleFeature()).isEqualTo(simpleFeature);
    }
}
