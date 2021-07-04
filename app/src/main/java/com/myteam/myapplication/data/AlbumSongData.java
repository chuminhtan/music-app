package com.myteam.myapplication.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myteam.myapplication.controller.AppController;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.AlbumSong;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumSongData {
    AlbumSong albumSong;
    Album album;
    ArrayList<Song> songArrayList;

    // Lấy danh sách bài hát của một album = Album_id
    // @albumId : ID của album
    // @callback

    public void getAlbumSongByAlbumId(int albumId, final AlbumSongAsyncRespone callback) {
        albumSong = new AlbumSong();
        album = new Album();
        songArrayList = new ArrayList<>();

        String url = ServerInfo.SERVER_BASE + "/album/song-album/" + albumId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Lấy object Album
                            JSONArray albumArr = response.getJSONArray("album");
                            JSONObject albumObj = albumArr.getJSONObject(0);

                            album.setId(albumObj.getInt("AL_ID"));
                            album.setName(albumObj.getString("AL_NAME"));
                            album.setImg(albumObj.getString("AL_IMG"));

                            albumSong.setAlbum(album);

                            // Lấy arrayObject bài hát
                            JSONArray songArrayObj = response.getJSONArray("songs");
                            for (int i = 0; i < songArrayObj.length(); i++) {
                                JSONObject songObj = songArrayObj.getJSONObject(i);

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

                            // Lấy Artist
                            JSONArray artistObj = response.getJSONArray("artists");

                            ArrayList<Artist> artists = new ArrayList<>();
                            int size2 = artistObj.length();
                            for (int i = 0; i < size2; i++) {
                                Artist artist = new Artist();
                                artist.setId(artistObj.getJSONObject(i).getInt("AR_ID"));
                                artist.setName(artistObj.getJSONObject(i).getString("AR_NAME"));
                                artist.setImg(artistObj.getJSONObject(i).getString("AR_IMG"));
                                artist.setStory(artistObj.getJSONObject(i).getString("AR_STORY"));
                                artists.add(artist);
                            }
                            albumSong.setSongs(songArrayList);
                            callback.processFinished(albumSong, artists);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });

        // Thêm vào hàng đợi request
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
