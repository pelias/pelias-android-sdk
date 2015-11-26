package com.mapzen.pelias;

import com.mapzen.pelias.widget.AutoCompleteItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.os.Parcel;

import java.util.Iterator;
import java.util.List;

import static com.mapzen.pelias.SavedSearch.MAX_ENTRIES;
import static com.mapzen.pelias.SimpleFeatureTest.getTestSimpleFeature;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class SavedSearchTest {
    SavedSearch savedSearch;
    Parcel payload;

    @Before
    public void setUp() throws Exception {
        savedSearch = new SavedSearch();
        savedSearch.clear();
        payload = Parcel.obtain();
        getTestSimpleFeature().writeToParcel(payload, 0);
        payload.setDataPosition(0);
    }

    @Test
    public void store_shouldHaveNullPayload() throws Exception {
        savedSearch.store("term");
        assertThat(savedSearch.get(0).getPayload()).isNull();
    }

    @Test
    public void store_shouldHaveNullPayloadAfterSerialize() throws Exception {
        savedSearch.store("term");
        String serialized = savedSearch.serialize();
        savedSearch.clear();
        savedSearch.deserialize(serialized);
        assertThat(savedSearch.get(0).getPayload()).isNull();
    }

    @Test
    public void payloadShouldBeHealthy() throws Exception {
        savedSearch.store("term", payload);
        String serialized = savedSearch.serialize();
        savedSearch.clear();
        savedSearch.deserialize(serialized);
        SimpleFeature feature = SimpleFeature.readFromParcel(savedSearch.get(0).getPayload());
        assertThat(feature.id()).isNotNull();
    }

    @Test
    public void store_updateRecordWithPayload() throws Exception {
        savedSearch.store("term");
        savedSearch.store("term", payload);
        assertThat(savedSearch.get(0).getPayload()).isNotNull();
        assertThat(countTerms(savedSearch.getIterator())).isEqualTo(1);
    }

    @Test
    public void store_shouldStoreThingsAtTop() throws Exception {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("expected");
        assertThat(savedSearch.getSubIterator(2).next().getTerm()).isEqualTo("expected");
    }

    @Test
    public void store_shouldStoreMaximumNumberOfElements() throws Exception {
        for (int i = 0; i < MAX_ENTRIES + 3; i++) {
            savedSearch.store(String.valueOf(i));
        }
        assertThat(countTerms(savedSearch.getSubIterator(MAX_ENTRIES + 10))).isEqualTo(MAX_ENTRIES);
    }

    @Test
    public void store_shouldEvictOldEntriesWhenMaxReached() throws Exception {
        savedSearch.store("search1");
        for (int i = 0; i < MAX_ENTRIES; i++) {
            savedSearch.store(String.valueOf(i));
        }
        Iterator<SavedSearch.Member> it = savedSearch.getSubIterator(MAX_ENTRIES);
        while (it.hasNext()) {
            assertThat(it.next().getTerm()).isNotEqualTo("search1");
        }
    }

    @Test
    public void store_shouldNotStoreExistingTerms() throws Exception {
        savedSearch.store("expected");
        savedSearch.store("search1", payload);
        savedSearch.store("search2");
        savedSearch.store("expected");
        assertThat(countTerms(savedSearch.getSubIterator(MAX_ENTRIES))).isEqualTo(3);
    }

    @Test
    public void store_shouldUpdateEntriesWithPayload() throws Exception {
        Parcel newPayload = Parcel.obtain();
        SimpleFeature expectedFeature = getTestSimpleFeature("New SimpleFeature");
        expectedFeature.writeToParcel(newPayload, 0);
        newPayload.setDataPosition(0);
        savedSearch.store("expected", payload);
        savedSearch.store("expected", newPayload);
        SavedSearch.Member member = savedSearch.getIterator().next();
        SimpleFeature simpleFeature = SimpleFeature.readFromParcel(member.getPayload());
        assertThat(simpleFeature.name()).isEqualTo(expectedFeature.name());
        assertThat(countTerms(savedSearch.getSubIterator(MAX_ENTRIES))).isEqualTo(1);
    }

    @Test
    public void store_shouldPutExistingTermsAtTheTop() throws Exception {
        savedSearch.store("expected");
        savedSearch.store("search1");
        savedSearch.store("search2");
        savedSearch.store("expected", payload);
        assertThat(savedSearch.getSubIterator(1).next().getTerm()).isEqualTo("expected");
    }

    @Test
    public void get_shouldReturnDefaultNumberOfTerms() throws Exception {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("search3");
        savedSearch.store("search4");
        assertThat(countTerms(savedSearch.getIterator())).isEqualTo(SavedSearch.DEFAULT_SIZE);
    }

    @Test
    public void get_shouldReturnRequestedNumberOfTerms() throws Exception {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("search3");
        assertThat(countTerms(savedSearch.getSubIterator(1))).isEqualTo(1);
    }

    @Test
    public void get_shouldReturnEmptyList() throws Exception {
        assertThat(savedSearch.getIterator().hasNext()).isFalse();
    }

    @Test
    public void isEmpty_shouldBeTrue() {
        assertThat(savedSearch.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_shouldBeFalse() {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("search3");
        assertThat(savedSearch.isEmpty()).isFalse();
    }

    @Test
    public void clearShouldEmptyCollection() throws Exception {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("search3");
        savedSearch.clear();
        assertThat(savedSearch.getIterator().hasNext()).isFalse();
    }

    @Test
    public void shouldBeSerializable() throws Exception {
        savedSearch.store("search1");
        savedSearch.store("search2", payload);
        savedSearch.store("expected");
        String serialized = savedSearch.serialize();
        savedSearch.clear();
        assertThat(savedSearch.getIterator().hasNext()).isFalse();
        savedSearch.deserialize(serialized);
        Iterator<SavedSearch.Member> it = savedSearch.getIterator();
        assertThat(it.next().getTerm()).isEqualTo("expected");
        assertThat(it.next().getTerm()).isEqualTo("search2");
        assertThat(it.next().getTerm()).isEqualTo("search1");
    }

    @Test
    public void deserialize_shouldHandleEmptyString() throws Exception {
        String serialized = savedSearch.serialize();
        savedSearch.clear();
        assertThat(savedSearch.getIterator().hasNext()).isFalse();
        savedSearch.deserialize(serialized);
        Iterator<SavedSearch.Member> it = savedSearch.getIterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void deserialize_shouldHandleNull() throws Exception {
        savedSearch.deserialize(null);
        Iterator<SavedSearch.Member> it = savedSearch.getIterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void getCursor_shouldReturnCursorWithSavedSearchTerms() throws Exception {
        savedSearch.store("saved query 1");
        savedSearch.store("saved query 2", payload);
        savedSearch.store("saved query 3");
        Cursor cursor = savedSearch.getCursor();
        assertThat(cursor.getCount()).isEqualTo(3);
        cursor.moveToFirst();
        assertThat(cursor.getString(1)).isEqualTo("saved query 3");
        cursor.moveToNext();
        assertThat(cursor.getString(1)).isEqualTo("saved query 2");
        cursor.moveToNext();
        assertThat(cursor.getString(1)).isEqualTo("saved query 1");
    }

    @Test
    public void deserialize_shouldClearStoreBeforeLoadingTerms() throws Exception {
        savedSearch.store("saved query");
        String serialized = savedSearch.serialize();
        savedSearch.deserialize(serialized);
        assertThat(savedSearch.size()).isEqualTo(1);
    }

    @Test
    public void getTerms_shouldReturnTextList() throws Exception {
        savedSearch.store("term 1");
        savedSearch.store("term 2");
        savedSearch.store("term 3");
        assertThat(savedSearch.getTerms()).hasSize(3);
        assertThat(savedSearch.getTerms().get(0)).isEqualTo("term 3");
        assertThat(savedSearch.getTerms().get(1)).isEqualTo("term 2");
        assertThat(savedSearch.getTerms().get(2)).isEqualTo("term 1");
    }

    @Test
    public void getItems_shouldReturnAutoCompleteItemsWithSimpleFeature() throws Exception {
        savedSearch.store(getTestSimpleFeature().label(), payload);
        savedSearch.store("term 2");
        savedSearch.store("term 3");
        List<AutoCompleteItem> items = savedSearch.getItems();
        assertThat(items).hasSize(3);
        assertThat(items.get(0).getText()).isEqualTo("term 3");
        assertThat(items.get(1).getText()).isEqualTo("term 2");
        assertThat(items.get(2).getText()).isEqualTo(getTestSimpleFeature().label());
        assertThat(items.get(0).getSimpleFeature()).isEqualTo(null);
        assertThat(items.get(1).getSimpleFeature()).isEqualTo(null);
        assertThat(items.get(2).getSimpleFeature()).isEqualTo(getTestSimpleFeature());
    }

    private int countTerms(Iterator<SavedSearch.Member> results) {
        int count = 0;
        while (results.hasNext()) {
            results.next();
            count++;
        }
        return count;
    }
}
