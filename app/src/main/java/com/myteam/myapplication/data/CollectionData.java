package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Collection;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollectionData {
    Collection collection;
    Playlist playlist;
    ArrayList<Song> songArrayList;

    // Lấy danh sách bài hát của một playlist = Playlist_id
    // @playlistId : ID của playlist
    // @callback
    public void getCollectionByPlaylistId(int playlistId, CollectionAsyncResponse callBack) {

         collection = new Collection();
         playlist = new Playlist();
         songArrayList = new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.COLLECTION + "/" + playlistId;

        // Tạo Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject playlistObj = response.getJSONObject("playlist");
                            playlist.setId(playlistObj.getInt("PL_ID"));
                            playlist.setName(playlistObj.getString("PL_NAME"));
                            playlist.setDes(playlistObj.getString("PL_DES"));
                            playlist.setImg(playlistObj.getString("PL_IMG"));

                            collection.setPlaylist(playlist);

                            JSONArray songsArrayObj = response.getJSONArray("songs");
                            for (int i = 0; i < songsArrayObj.length(); i++) {

                                JSONObject songObj = songsArrayObj.getJSONObject(i);

                                Song song = new Song();
                                song.setId(songObj.getInt("SO_ID"));
                                song.setName(songObj.getString("SO_NAME"));
                                song.setImg(songObj.getString("SO_IMG"));
                                song.setSrc(songObj.getString("SO_SRC"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("API", "ROOT = " + response.toString());

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.toString());
                    }
                });


        // Thêm vào hàng đợi request
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
















}
