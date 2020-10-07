package com.dum.dodam.httpConnection;
import com.dum.dodam.Login.Data.UserJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/auth/login")
    Call<UserJson> kakaoLogin(@Query("id") long id, @Query("token") String token);
}