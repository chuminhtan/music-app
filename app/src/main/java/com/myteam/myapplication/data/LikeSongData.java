package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.LikeSong;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikeSongData {
    ArrayList<Song> songArrayList;

    // Lấy danh sách bài hát yêu thích qua user id
    public void getLikeSongbyUserId(int userId, final LikeSongAsyncResponse callback) {
        songArrayList = new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/user/liked-song/" + userId;
        Log.d("API", "URL = " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Lấy arrayObj bài hát
                            JSONArray songsArrayObj = response.getJSONArray("songs");
                            for (int i = 0; i < songsArrayObj.length(); i++) {

                                JSONObject songObj = songsArrayObj.getJSONObject(i);

                                Song song = new Song();
                                song.setId(songObj.getInt("SO_ID"));
                                song.setName(songObj.getString("SO_NAME"));
                                song.setImg(songObj.getString("SO_IMG"));
                                song.setSrc(songObj.getString("SO_SRC"));

                                // Lấy arrayObj nghệ sĩ
                                JSONArray artistArrayObj = songObj.getJSONArray("ARTISTS");
                                for (int j = 0; j < artistArrayObj.length(); j++) {
                                    JSONObject artistObj = artistArrayObj.getJSONObject(j);

                                    if (artistObj.getString("AR_NAME") == null) {
                                        continue;
                                    }
                                    Artist artist = new Artist();

                                    artist.setId(artistObj.getInt("AR_ID"));
                                    artist.setName(artistObj.getString("AR_NAME"));
                                    song.addArtist(artist);
                                }
                                songArrayList.add(song);

                            }


                            callback.processFinished(songArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", error.toString());
                    }
                });


        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    private boolean result;
    public void like(LikeSong likeSong, final LikeSongAsyncRespone callback) {
        Log.d("LIKESONG", "From LikeSongData Started");

        final Map<String, String> mapResponse = new HashMap<>();
        try {
            final String URL = ServerInfo.SERVER_BASE + "/" + ServerInfo.LIKE_SONG;
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", likeSong.getUserId());
            jsonBody.put("song_id", likeSong.getSongId());
            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LIKESONG", "result response : " + response.toString());

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

                    Log.d("LIKESONG", error.getMessage());

                }
            }) {
            };
            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // CHECK SONG LIKED
    public void checkIfLikeSong(final int SongId, final int UserId, final CheckLikeSongAsyncResponse callback) {
        String url = ServerInfo.SERVER_BASE + ServerInfo.REPONSE_LIKED_SONG + "/" + UserId +"/" + SongId;
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            LikeSong likeSong = new LikeSong();
                            likeSong.setSongId(obj.getInt("SO_ID"));
                            likeSong.setUserId(obj.getInt("US_ID"));
                            if(likeSong.getSongId() == SongId && likeSong.getUserId() == UserId)
                                result = true;
                            else
                                result = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LIKESONG", "CHECK LIKE SONG " + error.getMessage());
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
