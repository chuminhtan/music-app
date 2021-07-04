package com.myteam.myapplication.data;

import java.util.Map;

public interface UserPlaylistAsycnResponse {
    void processFinished(Map<String, String> result);
}
