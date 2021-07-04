package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumData {
    private Album album;
    private ArrayList<Album> albumArrayList;
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public ArrayList<Album> getAlbumList() {
        return albumArrayList;
    }

    public void setAlbumList(ArrayList<Album> albumList) {
        this.albumArrayList = albumList;
    }

    // Test Server
    public void getServer() {
        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.SONG;
        Log.d("API", "URL = " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("API", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.toString());
                    }
                });

        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // Lấy ra 4 album mới nhất
    public ArrayList<Album> getNewestAlbums (final NewestAlbumsAsyncRespone callback) {
        albumArrayList = new ArrayList<>();
        String url = ServerInfo.SERVER_BASE + "/album/newest";

        Log.d("API", "URL = " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        for (int i= 0; i<size; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Album album = new Album();
                                album.setId(obj.getInt("AL_ID"));
                                album.setName(obj.getString("AL_NAME"));
                                album.setImg(obj.getString("AL_IMG"));

                                albumArrayList.add(album);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.processFinished(albumArrayList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());

            }

        });
        // Thêm vào hàng đợi request
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return albumArrayList;
    }
}
