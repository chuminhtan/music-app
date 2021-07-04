package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAlbumData {
    ArrayList<Album> albums;

    public ArrayList<Album> getAlbumsbyId (User user, final UserAlbumAsyncResponse callback){
        albums = new ArrayList<>();
        String url = ServerInfo.SERVER_BASE + "/user/liked/album/" + user.getId();
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();

                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Album album = new Album();
                                album.setId(obj.getInt("AL_ID"));
                                album.setName(obj.getString("AL_NAME"));
                                album.setImg(obj.getString("AL_IMG"));
                                albums.add(album);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        callback.processFinished(albums);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return albums;
    }
}
