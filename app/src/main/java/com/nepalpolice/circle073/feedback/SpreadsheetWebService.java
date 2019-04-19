package com.nepalpolice.circle073.feedback;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SpreadsheetWebService {

    @POST("1FAIpQLScUYcl3K4RYdTggTMPn2_qc5S62RWM_TtATXUDZJQCxvfmn8Q/viewform")
    @FormUrlEncoded
    Call<Void> feedbackSend(
            @Field("entry.1445364870") String name,
            @Field("entry.1868862691") String email
    );

}
