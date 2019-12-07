package com.example.proyek_sdp;

import com.example.proyek_sdp.Notifications.MyResponse;
import com.example.proyek_sdp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsaGdhuY:APA91bFdTNpCLFeflj8_UuitTD3N8yHYCrKM0K2g7YEz1XJ0sdqCqv0cdj6RfjK6iHdSYIERAFTjFsouetFmWbk8MFoYpW6lGS_4Jssb3JXzHuDxtAOElV0dOVcROyqVus51CzzsiPJl"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
