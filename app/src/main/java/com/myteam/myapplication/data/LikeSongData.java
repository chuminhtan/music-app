package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    public void getLikeSongbyUserId (int userId, final LikeSongAsyncResponse callback) {
        songArrayList = new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/user/liked-song/" + userId;
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();

                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Song song = new Song();
                                song.setId(obj.getInt("SO_ID"));
                                song.setName(obj.getString("SO_NAME"));
                                song.setImg(obj.getString("SO_IMG"));
                                song.setSrc(obj.getString("SO_SRC"));

                                // Lấy arrayObj nghệ sĩ
                                JSONArray artistArrayObj = obj.getJSONArray("ARTISTS");
                                for (int j = 0; j < artistArrayObj.length(); j++) {
                                    JSONObject artistObj = artistArrayObj.getJSONObject(j);

                                    if (artistObj.getInt("AR_ID") == 0) {
                                        continue;
                                    }
                                    Artist artist = new Artist();

                                    artist.setId(artistObj.getInt("AR_ID"));
                                    artist.setName(artistObj.getString("AR_NAME"));
                                    song.addArtist(artist);
                                }

                                songArrayList.add(song);

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        callback.processFinished(songArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.getMessage());
            }
        });

        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


}
