package com.kiptechie.diceroller.api;

public class ApiUtils {

    private ApiUtils() {}

    public static ApiInterface getAPIService() {

        return ApiClient.getClient().create(ApiInterface.class);
    }
}
