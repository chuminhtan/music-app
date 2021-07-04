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
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongData {
    private ArrayList<Song> songList;

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
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

    // Lấy Banner Bài Hát Mới Nhất
    public void getNewUpload(final SongListAsyncResponse callback) {

        songList = new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.SONG + "/new";
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
                                Song song = new Song();
                                song.setId(obj.getInt("SO_ID"));
                                song.setName(obj.getString("SO_NAME"));
                                song.setImg(obj.getString("SO_IMG"));
                                song.setSrc(obj.getString("SO_SRC"));

                                songList.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Gọi callback để truyền vào songList sau khi hoàn thành.
                        // Hàm callback sẽ được gọi lại trong Fragment hoặc Layout
                        callback.processFinished(songList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    // Lấy thông tin bài hát theo Id
    public void getSongInfoById(final SongAsyncResponeByID callback, int id) {
        String url = ServerInfo.SERVER_BASE + "/" + id;
        Log.d("API", "URL = " + url);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Song song = new Song();
                            song.setId(response.getInt("SO_ID"));
                            song.setName(response.getString("SO_NAME"));
                            song.setImg(response.getString("SO_IMG"));
                            song.setSrc(response.getString("SO_SRC"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    // Lấy danh sách bài hát có tên tương tự nhau
    public void getListSongSimilar(final SongListAsyncResponse callback, String word) {
        songList = new ArrayList<>();
        String url = ServerInfo.SERVER_BASE + "/" + ServerInfo.SONG_RELATE +"/" + word;
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
                                    if (artistObj.getString("AR_NAME") == null) {
                                        continue;
                                    }
                                    Artist artist = new Artist();

                                    artist.setId(artistObj.getInt("AR_ID"));
                                    artist.setName(artistObj.getString("AR_NAME"));

                                    song.addArtist(artist);
                                }

                                songList.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.processFinished(songList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.getMessage().toString());
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


}
