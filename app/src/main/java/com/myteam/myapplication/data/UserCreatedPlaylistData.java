package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCreatedPlaylistData {
    ArrayList<Playlist> playlists;

    public ArrayList<Playlist> getPlaylistsbyId(User user, final UserCreatedPlaylistAsyncResponse callback) {
        playlists = new ArrayList<>();
        // user_id từ shared preferences
        String url = ServerInfo.SERVER_BASE + "/user/playlist/" + user.getId();
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();

                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Playlist playlist = new Playlist();
                                playlist.setId(obj.getInt("PL_ID"));
                                playlist.setName(obj.getString("PL_NAME"));
                                playlist.setImg(obj.getString("PL_IMG"));
                                playlist.setImg2(obj.getString("PL_IMG2"));
                                playlists.add(playlist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        callback.processFinished(playlists);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return playlists;
    }
}
