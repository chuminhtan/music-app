package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Collection;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPlaylistData {
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

                    Log.d("CREATE USER PLAYLIST", error.getMessage());

                }
            }) {
            };
            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addSongToPlaylist(int SongId, int PlaylistId, final UserPlaylistAsycnResponse callback ){
        Log.d("USERPLAYLIST", "From UserPlaylistData Started");
        Log.d("USERPLAYLIST", "song id, playlist id" + String.valueOf(SongId) + " " + String.valueOf(PlaylistId));
        final Map<String, String> mapResponse = new HashMap<>();
        try {
            final String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.ADD_SONG_USERPLAYLIST;
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_playlist_id",PlaylistId);
            jsonBody.put("user_playlist_song_id", SongId);
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("USERPLAYLIST", "result response of add song to playlist: " + response.toString());

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
