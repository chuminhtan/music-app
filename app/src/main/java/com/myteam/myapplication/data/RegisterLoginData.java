package com.myteam.myapplication.data;

import android.content.pm.ServiceInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterLoginData {


    // REGISTER
    public void register(User user, final RegisterLoginAsyncResponse callback) {
        Log.d("REGISTER", "From RegisterData Started!");

        final Map<String, String> mapResponse = new HashMap<>();
        try {
            String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.USER + "/register";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_name", user.getName());
            jsonBody.put("user_email", user.getEmail());
            jsonBody.put("user_password", user.getPassword());

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("REGISTER", "result response : " + response.toString());

                    try {
                        mapResponse.put("result",response.getString("result"));
                        mapResponse.put("message", response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.processFinished(mapResponse);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("REGISTER", error.getMessage());

                }
            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    final Map<String, String> headers = new HashMap<>();
//                    headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
//                    return headers;
//                }
            };

            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // LOGIN
    public void login(String user_email, String user_password, final RegisterLoginAsyncResponse callback) {
        Log.d("REGISTER", "From LoginData Started!");

        final Map<String, String> mapResponse = new HashMap<>();
        try {
            String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.USER + "/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_email", user_email);
            jsonBody.put("user_password", user_password);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("REGISTER", "result response : " + response.toString());

                    try {

                        String result = response.getString("result");
                        mapResponse.put("result", result);
                        mapResponse.put("message", response.getString("message"));


                        if (result.equalsIgnoreCase(ServerInfo.RESPONSE_SUCCESS)) {
                            JSONObject userJson =  response.getJSONObject("user");
                            mapResponse.put("user_name", userJson.getString("US_NAME"));
                            mapResponse.put("user_id", String.valueOf(userJson.getInt("US_ID")));
                            mapResponse.put("user_email", userJson.getString("US_EMAIL"));
                        };

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.processFinished(mapResponse);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("REGISTER", error.getMessage());

                }
            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    final Map<String, String> headers = new HashMap<>();
//                    headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
//                    return headers;
//                }
            };

            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
