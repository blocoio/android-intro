package io.bloco.introduction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import io.bloco.introduction.api.SearchEvents;
import io.bloco.introduction.api.SearchResponse;
import java.util.List;

public class EventsActivity extends AppCompatActivity
    implements LocationGetter.Callback, SearchEvents.Callback {

  private static final int REQUEST_PERMISSION_CODE = 123;

  private TextView nameText;
  private TextView locationText;
  private RecyclerView resultsRecyclerView;
  private Preferences preferences;
  private LocationGetter locationGetter;
  private SearchEvents searchEvents;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_events);

    preferences = new Preferences(this);
    locationGetter = new LocationGetter(this);
    searchEvents = new SearchEvents();
    nameText = (TextView) findViewById(R.id.events_name);
    locationText = (TextView) findViewById(R.id.events_location);

    resultsRecyclerView = (RecyclerView) findViewById(R.id.events_results);
    resultsRecyclerView.setLayoutManager(
        new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns)));

    String name = preferences.getName();
    if (name == null) {
      Intent intent = new Intent(this, LoginActivity.class);
      startActivity(intent);
      finish();
    } else {
      nameText.setText(getResources().getString(R.string.events_name, name));

      tryAndGetLocation();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    locationGetter.stop();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSION_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLocation();
      }
    }
  }

  private void tryAndGetLocation() {
    String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] { permission }, REQUEST_PERMISSION_CODE);
      return;
    }

    getLocation();
  }

  private void getLocation() {
    locationGetter.start(this);
  }

  @Override public void onNewLocation(Location location) {
    locationText.setText(getResources().getString(R.string.events_location, location.getLatitude(),
        location.getLongitude()));

    searchEvents.search(location, this);
  }

  @Override public void onEventResults(List<SearchResponse.Event> events) {
    resultsRecyclerView.setAdapter(new EventsAdapter(events));
  }

  @Override public void onError() {
    Toast.makeText(this, R.string.events_search_error, Toast.LENGTH_LONG).show();
  }
}
