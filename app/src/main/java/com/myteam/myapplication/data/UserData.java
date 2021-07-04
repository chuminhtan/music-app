package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserData {


    // Change Info
    public void changeInfo(int id, String name, String currentPassword, String newPassword, final UserAsyncResponse callback) {
        Log.d("REGISTER", "From RegisterData Started!");
        final String[] result = new String[1];
        try {
            String URL = ServerInfo.SERVER_BASE + "/user/change-info";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_name", name);
            jsonBody.put("user_id", 1);
            jsonBody.put("user_old_password", currentPassword);
            jsonBody.put("user_new_password", newPassword);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("API", "result response : " + response.toString());

                    try {
                        result[0] = response.getString("result");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.processFinished(result[0]);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("REGISTER", error.getMessage());

                }
            }) {
            };
            AppController.getInstance().addToRequestQueue(jsonOblect);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Set Like Playlist
    public void setLikePlaylist(int userId, int playlistId, String type,final UserAsyncResponse callback) {
        String url = ServerInfo.SERVER_BASE + "/user/like-"+ type + "/" + userId +"/" + playlistId;
        Log.d("API", "URL = " + url);
        final String[] result = {""};
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result[0] = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(result[0]);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    // Check user like playlist or album
    public void checkLiked(int userId, int playlistId, String type, final UserAsyncResponse callback) {
        String url = ServerInfo.SERVER_BASE + "/user/check-"+ type +"-liked/" + userId +"/" + playlistId;
        final String[] result = {""};
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result[0] = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(result[0]);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

}
