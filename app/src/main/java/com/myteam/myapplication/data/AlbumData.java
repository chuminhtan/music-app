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
    private List<Album> albumList;
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
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

    // Lấy thông tin album bằng id
    public void getAlbumById(final AlbumAsyncRespone callback, int id) {
        String url = ServerInfo.SERVER_BASE + "/" + id;
        Log.d("API", "URL = " + url);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            album.setName(response.getString("AL_NAME"));
                            album.setImg(response.getString("AL_IMG"));
                            // Lấy thông tin nghệ sĩ
                            int artistId = response.getInt("AR_ID");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(album);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    // Lấy thông tin album bằng Name
    public void getAlbumByName(final AlbumListAsyncRespone callback, String name) {
        albumList = new ArrayList<Album>();
        String url = ServerInfo.SERVER_BASE + "/" + name;
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int size = response.length();
                            for(int i = 0; i < size; i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Album album_child = new Album();
                                album_child.setId(obj.getInt("ID"));
                                album_child.setName(obj.getString("AL_NAME"));
                                album_child.setImg(obj.getString("AL_IMG"));
                                // Lấy thông tin nghệ sĩ
                                int artistId = obj.getInt("AR_ID");
                                albumList.add(album_child);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(albumList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
