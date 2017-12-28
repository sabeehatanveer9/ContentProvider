package com.example.maqso.bonusassignment;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.POST;



public interface Service {


    @POST("/api/contacts")
    Call<List<Contacts>> contactMatch(@Body ArrayList<Contacts> list);

    @GET("/api/contacts")
    Call<List<Contacts>> getcontactMatch();

}
