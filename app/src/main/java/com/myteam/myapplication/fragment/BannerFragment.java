
/**
 * FRAGMENT BANNER HIỂN THỊ TRÊN TRANG HOME
 * */


package com.myteam.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myteam.myapplication.R;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.data.SongListAsyncResponse;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public class BannerFragment extends Fragment {
    View view;
    private ArrayList<Song> songList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banner, container, false);

        // Lấy dữ liệu banner - bài hát mới upload từ server
        getBanner();

        return view;
    }

    private void getBanner() {
        songList = new ArrayList<>();
        songList = new SongData().getNewUpload(new SongListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Song> songList) {

                Log.d("SONGLIST", "RESULT: " + songList.toString());
            }
        });
    }
}
