package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
}
