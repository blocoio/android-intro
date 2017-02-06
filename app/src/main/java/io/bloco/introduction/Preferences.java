package io.bloco.introduction;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

  private static final String NAME_KEY = "name";

  private final SharedPreferences sharedPreferences;

  public Preferences(Context context) {
    sharedPreferences =
         context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
  }

  public void saveName(String name) {
    sharedPreferences.edit().putString(NAME_KEY, name).apply();
  }

  public String getName() {
    return sharedPreferences.getString(NAME_KEY, null);
  }
}
