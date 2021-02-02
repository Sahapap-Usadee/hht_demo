package com.jastec.hht_demo.remote;

import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.TerTest;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyAPI {
    //http://localhost:58073/api/Login
    @POST("api/Register")
    Observable<String> registerUser(@Body TerTest user);

    @POST("api/Login")
    Observable<String> LoginUser(@Body TerTest user);
//http://localhost:5000/api/Program http://localhost:5000/api/Program
    @GET("api/Program")
   // Call<List<MsPg>> GetPg();
    Observable<List<MsPg>> GetPg();


    @POST("api/Login/user1")
        // Call<List<MsPg>> GetPg();
    Observable<String> GetUSER_COUNT(@Body List<TerTest> user);
//    @GET("api/Program")
//    void GetPg(Callback<List<MsPg>> listCallback);
}
