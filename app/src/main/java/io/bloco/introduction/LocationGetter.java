package io.bloco.introduction;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationGetter implements LocationListener {

  public static final int MIN_TIME = 5000;
  public static final int MIN_DISTANCE = 300;
  private final LocationManager systemService;
  private Callback callback;
  private boolean requestingUpdates;

  public LocationGetter(Context context) {
    systemService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @SuppressWarnings("MissingPermission")
  public void start(final Callback callback) {
    this.callback = callback;
    String provider = getProvider();
    if (provider == null) {
      return;
    }

    Location location = systemService.getLastKnownLocation(provider);
    if (location != null) {
      callback.onNewLocation(location);
    }

    systemService.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
    requestingUpdates = true;
  }

  @SuppressWarnings("MissingPermission")
  public void stop() {
    if (requestingUpdates) {
      systemService.removeUpdates(this);
      requestingUpdates = false;
    }
  }

  private String getProvider() {
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    return systemService.getBestProvider(criteria, true);
  }

  @Override public void onLocationChanged(Location location) {
    callback.onNewLocation(location);
  }

  @Override public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override public void onProviderEnabled(String provider) {
  }

  @Override public void onProviderDisabled(String provider) {
  }

  public interface Callback {
    void onNewLocation(Location location);
  }
}
