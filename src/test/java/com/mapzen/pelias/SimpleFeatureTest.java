package com.mapzen.pelias;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.os.Parcel;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class SimpleFeatureTest {
    @Test
    public void shouldReadAndWriteParcel() throws Exception {
        SimpleFeature before = getTestSimpleFeature();
        Parcel parcel = before.toParcel();
        SimpleFeature after = SimpleFeature.readFromParcel(parcel);
        assertThat(before).isEqualTo(after);
    }

    @Test
    public void getAddress_shouldReturnLabelTextWithoutName() throws Exception {
        assertThat(getTestSimpleFeature().address()).isEqualTo("Manhattan, NY");
    }

    public static SimpleFeature getTestSimpleFeature() {
        return getTestSimpleFeature("Test SimpleFeature");
    }

    public static SimpleFeature getTestSimpleFeature(String name) {
        return SimpleFeature.builder()
                .lat(1.0)
                .lng(1.0)
                .id("123")
                .name(name)
                .country("United States")
                .countryAbbr("USA")
                .region("New York")
                .regionAbbr("NY")
                .county("New York County")
                .localAdmin("Manhattan")
                .locality("New York")
                .neighborhood("Koreatown")
                .label("Test SimpleFeature, Manhattan, NY")
                .build();
    }
}
