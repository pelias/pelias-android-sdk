package com.mapzen.pelias;

import com.mapzen.pelias.widget.AutoCompleteItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Parcel;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.provider.BaseColumns._ID;

/**
 * Used by {@link PeliasSearchView} to facilitate storing/retrieving saved search results.
 */
public final class SavedSearch {

  public static final String TAG = SavedSearch.class.getSimpleName();

  public static final int DEFAULT_SIZE = 3;
  public static final int MAX_ENTRIES = 10;

  public static final String SEARCH_TERM = "search_term";
  public static final String PAYLOAD = "payload";
  public static final String[] COLUMNS = {
      _ID, SEARCH_TERM
  };

  public static final String JSON_STRING_EMPTY_ARRAY = "[]";

  private LinkedList<Member> store = new LinkedList<>();

  /**
   * Helps with serializing/deserializing search terms.
   */
  public class Member {
    private String term;
    private Parcel payload;

    /**
     * Constructs a new object with the given term and payload.
     */
    public Member(String term, Parcel payload) {
      this.term = term;
      this.payload = payload;
    }

    /**
     * Constructs a new object from a search term.
     */
    public Member(String term) {
      this.term = term;
    }

    /**
     * Returns the search term.
     */
    public String getTerm() {
      return term;
    }

    /**
     * Returns the parcelled search term.
     */
    public Parcel getPayload() {
      return payload;
    }

    /**
     * Returns a json representation of the search term.
     */
    public JSONObject toJson() {
      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject();
        jsonObject.put(SEARCH_TERM, getTerm());
        Parcel payload = getPayload();
        if (payload != null) {
          jsonObject.put(PAYLOAD, new String(payload.marshall(), "ISO-8859-1"));
        }
      } catch (UnsupportedEncodingException | JSONException e) {
        Log.e(TAG, "Unable to convert member to JSON", e);
      }

      return jsonObject;
    }

    @Override public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Member member = (Member) o;

      return term.equals(member.term);
    }

    @Override public int hashCode() {
      int result = term.hashCode();
      if (payload != null) {
        result = 31 * result + payload.hashCode();
      }
      return result;
    }
  }

  /**
   * Stores the term and payload. Creates a {@link Member} object from the term and payload and
   * inserts it at the beginning of the linked list. Also truncates the number of stored terms so
   * that it is not greater than {@link MAX_ENTRIES}
   */
  public int store(String term, Parcel payload) {
    truncate();
    Member member = new Member(term, payload);
    store.remove(member);
    store.addFirst(member);
    return 0;
  }

  /**
   * Stores the term. Creates a {@link Member} object from the term and inserts it at the beginning
   * of the linked list. Also truncates the number of stored terms so that it is not greater than
   * {@link MAX_ENTRIES}
   */
  public int store(String term) {
    return store(term, null);
  }

  /**
   * Returns the search term at the given index.
   */
  public Member get(int i) {
    return store.get(i);
  }

  /**
   * Returns the store's iterator.
   */
  public Iterator<Member> getIterator() {
    return getSubIterator(DEFAULT_SIZE);
  }

  /**
   * Returns the store's iterator if the store's size is smaller than the requested size, otherwise
   * returns an iterator for a sublist of the store.
   */
  public Iterator<Member> getSubIterator(int size) {
    if (store.size() == 0 || store.size() < size) {
      return store.iterator();
    }
    return store.subList(0, size).iterator();
  }

  /**
   * Clears the store.
   */
  public void clear() {
    store.clear();
  }

  /**
   * Returns whether or not the store is empty.
   */
  public boolean isEmpty() {
    return store.size() == 0;
  }

  /**
   * Returns a json string for the store.
   */
  public String serialize() {
    JSONArray jsonArray = new JSONArray();
    for (Member member : store) {
      jsonArray.put(member.toJson());
    }
    return jsonArray.toString();
  }

  private boolean isEmptyArrayString(String serializedString) {
    return serializedString.equals(JSON_STRING_EMPTY_ARRAY);
  }

  /**
   * Populates the store with the values of the serialized json string.
   */
  public void deserialize(String serializedSavedSearch) {
    if (serializedSavedSearch == null || serializedSavedSearch.isEmpty() || isEmptyArrayString(
        serializedSavedSearch)) {
      return;
    }

    store.clear();

    JSONArray jsonArray;
    try {
      jsonArray = new JSONArray(serializedSavedSearch);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String term = jsonObject.getString(SEARCH_TERM);
        Parcel payload = null;
        if (jsonObject.has(PAYLOAD)) {
          payload = Parcel.obtain();
          String rawPayload = jsonObject.getString(PAYLOAD);
          payload.unmarshall(rawPayload.getBytes("ISO-8859-1"), 0,
              rawPayload.getBytes("ISO-8859-1").length);
          payload.setDataPosition(0);
        }
        store.add(new Member(term, payload));
      }
    } catch (UnsupportedEncodingException | JSONException e) {
      Log.e(TAG, "Unable to deserialize saved search terms", e);
    }
  }

  /**
   * Returns a {@link Cursor} for the store.
   */
  public Cursor getCursor() {
    final MatrixCursor cursor = new MatrixCursor(COLUMNS);
    for (int i = 0; i < store.size(); i++) {
      cursor.addRow(new Object[] { i, store.get(i).getTerm() });
    }

    return cursor;
  }

  /**
   * Returns a list of saved search terms (text only).
   */
  public List<String> getTerms() {
    final ArrayList<String> terms = new ArrayList<>();
    for (Member member : store) {
      terms.add(member.getTerm());
    }
    return terms;
  }

  /**
   * Returns a list of {@link AutoCompleteItem} objects with optional payload.
   */
  public List<AutoCompleteItem> getItems() {
    final ArrayList<AutoCompleteItem> items = new ArrayList<>();
    for (Member member : store) {
      final String term = member.getTerm();
      final Parcel parcel = member.getPayload();
      if (parcel != null) {
        items.add(new AutoCompleteItem(parcel));
      } else {
        items.add(new AutoCompleteItem(term));
      }
    }
    return items;
  }

  private void truncate() {
    if (store.size() >= MAX_ENTRIES) {
      store.removeLast();
    }
  }

  /**
   * Returns the size of the store.
   */
  public int size() {
    return store.size();
  }
}
