package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Collection;

public interface CollectionAsyncResponse {
    void processFinished(Collection collection);
}
