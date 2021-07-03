package com.myteam.myapplication.util;

// Chứa thông tin kết nối tới server

public class ServerInfo {
    // Base url
    public static final String SERVER_BASE = "http://192.168.1.8:8000";

    // Song
    public static final String SONG = "song";
    public static final String STORAGE_SONG_MP3 ="storage/song";
    public static final String STORAGE_SONG_IMG ="storage/song-image";
    public static final String SONG_RELATE = "song/songrelate";

    // Collection
    public static final String COLLECTION = "collection";



    // Album
    public static final String ALBUM = "album";
    public static final String ALBUM_IMG = "storage/album-image";

    // Artist

    // Genre

    // Playlist
    public static final String PLAYLIST = "playlist";
    public static final String PLAYLIST_NEWEST = PLAYLIST + "/newest";
    public static final String PLAYLIST_TYPE = PLAYLIST + "/type";
    public static final String PLAYLIST_IMG = "storage/playlist-image";

    // User
    public static final String USER = "user";
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_FAIL = "fail";
}
