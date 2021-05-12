package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaylistData {

    ArrayList<Playlist> playlistNewest;
    ArrayList<Playlist> playlistType;
    Playlist playlist;

    // Lấy Playlist mới nhất
    // use: Hiển thị banner
    public ArrayList<Playlist> getNewUpload(final PlaylistArrayListAsyncResponse callback) {
        playlistNewest= new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.PLAYLIST_NEWEST;

        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
//                        Log.d("SONGLIST", "result:" + response.toString());

                        for (int i = 0; i < size; i++) {

                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Playlist playlist = new Playlist();
                                playlist.setId(obj.getInt("PL_ID"));
                                playlist.setName(obj.getString("PL_NAME"));
                                playlist.setDes(obj.getString("PL_DES"));
                                playlist.setImg(obj.getString("PL_IMG"));

                                playlistNewest.add(playlist);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Gọi callback để truyền vào playlistNewest sau khi hoàn thành.
                        // Hàm callback sẽ được gọi lại trong Fragment hoặc Layout
                        callback.processFinished(playlistNewest);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return playlistNewest;
    }


    // Playlist chủ đề HOT => Playlist type = 1
    public ArrayList<Playlist> getPlaylistsType(int type, final PlaylistArrayListAsyncResponse callback) {

        playlistType= new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.PLAYLIST_TYPE + "/" + type;
        Log.d("API", "URL = " + url);

        // Bất đồng bộ: gửi request lên nhưng không có chờ đến khi kết quả trả về, một lát sau mới trả về kết quả
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
//                        Log.d("SONGLIST", "result:" + response.toString());

                        for (int i = 0; i < size; i++) {

                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Playlist playlist = new Playlist();
                                playlist.setId(obj.getInt("PL_ID"));
                                playlist.setName(obj.getString("PL_NAME"));
                                playlist.setDes(obj.getString("PL_DES"));
                                playlist.setImg(obj.getString("PL_IMG"));

                                playlistType.add(playlist);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Gọi callback để truyền vào playlistNewest sau khi hoàn thành.
                        // Hàm callback sẽ được gọi lại trong Fragment hoặc Layout
                        callback.processFinished(playlistType);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return playlistType;
    }

























}
