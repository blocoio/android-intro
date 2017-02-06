package io.bloco.introduction.api;

import android.location.Location;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchEvents {

  private static final String API_URL = "https://www.eventbriteapi.com/v3/";
  private static final String API_TOKEN = "BENLVZYFPCSBXLXVTO25";
  private static final String EXPAND_FIELDS = "venue";
  private static final String WITHIN = "20km";
  private final EventbriteApi evenbriteApi;

  public SearchEvents() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create(
            new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'hh:mm:ss")
                .create()
        ))
        .build();
    evenbriteApi = retrofit.create(EventbriteApi.class);
  }

  public void search(Location location, final Callback callback) {
    evenbriteApi.eventSearch(API_TOKEN, EXPAND_FIELDS, WITHIN, location.getLatitude(),
        location.getLongitude()).enqueue(new retrofit2.Callback<SearchResponse>() {
      @Override
      public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        if (response.isSuccessful()) {
          callback.onEventResults(response.body().events);
        } else {
          callback.onError();
        }
      }

      @Override public void onFailure(Call<SearchResponse> call, Throwable t) {
        callback.onError();
      }
    });
  }

  public interface Callback {
    void onEventResults(List<SearchResponse.Event> events);

    void onError();
  }
}
