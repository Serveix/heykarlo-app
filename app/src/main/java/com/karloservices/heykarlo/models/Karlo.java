package com.karloservices.heykarlo.models;

import android.util.Log;

import com.karloservices.heykarlo.interfaces.HKRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by eli on 11/9/17.
 */

public class Karlo
{
    private static final String url = "api/response/";
    private static String completeUrl;
    private static Response answer;

    public static Response respondTo(final String whatUserSaid) {
        completeUrl = url + whatUserSaid;
        HKRestClient.syncGet(completeUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try
                {
                     Log.i("RESPONSE", "Respuesta del servidor recibida: " + responseBody.toString());
                     answer = new Response(responseBody.getString("type"),
                                          responseBody.getString("action"),
                                          responseBody.getString("message"),
                                          responseBody.getString("info"));
                } catch (JSONException e){
                    Log.e("Error getting response", e.toString());
                    answer = new Response("error",
                                         null,
                                       "Hubo un error en la respuesta recibida desde el servidor",
                                           null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error getting response", throwable.getMessage());
                answer = new Response("error", null, "Hubo un error en la respuesta recibida desde el servidor", null);
            }

        });

        return answer;
    }

}
