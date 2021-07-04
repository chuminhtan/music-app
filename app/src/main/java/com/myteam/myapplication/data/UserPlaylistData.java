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
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPlaylistData {
    ArrayList<Playlist> playlists;

    public ArrayList<Playlist> getPlaylistsbyId(User user, final UserPlaylistAsyncResponse callback) {
        playlists = new ArrayList<>();
        // user_id từ shared preferences
        String url = ServerInfo.SERVER_BASE + "/user/liked/playlist/" + user.getId();
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

    public void createUserPlaylist(Playlist playlist, Song song, final UserPlaylistAsycnResponse callback) {
        Log.d("CREATE USER PLAYLIST", "From UserPlaylistData Started");

        final Map<String, String> mapResponse = new HashMap<>();
        try {
            final String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.USER_PLAYLIST_CREATE;
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_playlist_name", playlist.getName());
            jsonBody.put("user_playlist_use_id", playlist.getUser().getId());
            jsonBody.put("user_playlist_type", playlist.getType());
            jsonBody.put("song_id", song.getId());
            jsonBody.put("song_img", song.getImg());
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("CREATE USER PLAYLIST", "result response from create new playlist : " + response.toString());

                    try {
                        mapResponse.put("result", response.getString("result"));
                        mapResponse.put("message", response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.processFinished(mapResponse);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("CREATE USER PLAYLIST", error.getMessage());

                }
            }) {
            };
            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addSongToPlaylist(int SongId, int PlaylistId, final UserPlaylistAsycnResponse callback) {
        Log.d("USERPLAYLIST", "From UserPlaylistData Started");
        Log.d("USERPLAYLIST", "song id, playlist id" + String.valueOf(SongId) + " " + String.valueOf(PlaylistId));
        final Map<String, String> mapResponse = new HashMap<>();
        try {
            final String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.ADD_SONG_USERPLAYLIST;
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_playlist_id", PlaylistId);
            jsonBody.put("user_playlist_song_id", SongId);
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("USERPLAYLIST", "result response of add song to playlist: " + response.toString());

                    try {
                        mapResponse.put("result", response.getString("result"));
                        mapResponse.put("message", response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.processFinished(mapResponse);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("USERPLAYLIST", error.getMessage());

                }
            }) {
            };
            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

