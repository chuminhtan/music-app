package com.myteam.myapplication.data;

import java.util.Map;

public interface RegisterLoginAsyncResponse {
    void processFinished(Map<String, String> mapResponse);
}
