package io.bloco.introduction.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventbriteApi {

  @GET("events/search") Call<SearchResponse> eventSearch(@Query("token") String authToken,
      @Query("expand") String expand, @Query("location.within") String within,
      @Query("location.latitude") double latitude, @Query("location.longitude") double longitude);
}
