package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistData {
    private Artist artist;
    private ArrayList<Artist> artistList;
    private ArrayList<Album> albumArrayList;
    private ArrayList<Artist> artistSimilar;


    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(ArrayList<Artist> artistList) {
        this.artistList = artistList;
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

    // Lấy thông tin nghệ sĩ bằng ID
    public void getArtistInfoById(final ArtistAsyncRespone callback, int id) {
        String url = ServerInfo.SERVER_BASE + "/" + id;
        Log.d("API", "URL = " + url);


        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            artist.setName(response.getString("AR_NAME"));
                            artist.setImg(response.getString("AR_IMG"));
                            artist.setStory(response.getString("AR_STORY"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(artist);
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

    // Lấy thông tin nghệ sĩ bằng tên
    public void getArtistInfoById(final ArtistListAsyncRespone callback, String name) {
        artistList = new ArrayList<Artist>();
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
                                Artist artist_child = new Artist();
                                artist_child.setId(obj.getInt("AR_ID"));
                                artist_child.setName(obj.getString("AR_NAME"));
                                artist_child.setImg(obj.getString("AR_IMG"));
                                artist_child.setStory(obj.getString("AR_STORY"));
                                artistList.add(artist_child);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(artistList);
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

    // Lấy danh sách albums của nghệ sĩ
    public void getArtistAlbumbyId (int artistId, final ArtistAlbumsAsyncRespone callback) {
        albumArrayList = new ArrayList<>();
        String url = ServerInfo.SERVER_BASE + "/artist/artist-album/" + artistId;
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();

                        for (int i = 0; i< size; i++) {
                            try{
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
                Log.d("API", error.getMessage());
            }
        });
        // Access the RequestQueue through your AppController class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    // GET ARTIST WITH SIMILAR
    public void getArtistSimilar(final ArtistListAsyncRespone callback, String word) {
        artistSimilar = new ArrayList<Artist>();
        String url = ServerInfo.SERVER_BASE + "/" +ServerInfo.ARTIST_SIMILAR + "/" + word;
        Log.d("ARTISTDATA", "Started from Artist Data");
        Log.d("API", "URL = " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int size = response.length();
                            for(int i = 0; i < size; i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Artist artist_child = new Artist();
                                artist_child.setId(obj.getInt("AR_ID"));
                                artist_child.setName(obj.getString("AR_NAME"));
                                artist_child.setImg(obj.getString("AR_IMG"));
                                artist_child.setStory(obj.getString("AR_STORY"));
                                artistSimilar.add(artist_child);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.processFinished(artistSimilar);
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
