package com.karloservices.heykarlo.interfaces;
import android.util.Log;

import com.loopj.android.http.*;

/**
 * Created by eli on 10/25/17.
 */

public class HKRestClient
{
    private static final String BASE_URL = "http://10.76.37.47/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static SyncHttpClient syncClient = new SyncHttpClient();

    // SYNC HTTP
    public static void syncGet(String url, JsonHttpResponseHandler responseHandler){
        Log.i("REST", "Enviando al servidor: " + getAbsoluteUrl(url));
        syncClient.get(getAbsoluteUrl(url), responseHandler);

    }

    public static void syncPost(String url, RequestParams params, JsonHttpResponseHandler responseHandler){
        syncClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        syncClient.setCookieStore(cookieStore);
    }


    // ASYNC HTTP
    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}
