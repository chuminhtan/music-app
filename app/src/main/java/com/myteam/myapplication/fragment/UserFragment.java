/**
 * TRANG USER
 * */

package com.myteam.myapplication.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.EditUserInfoActivity;
import com.myteam.myapplication.activity.LikedSongListActivity;
import com.myteam.myapplication.activity.MainActivity;
import com.myteam.myapplication.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    View view;
    private TextView txtUserName;
    private ImageButton btnSettings;
    private ImageButton btnLikedSongs;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false );
        init();
        getUser();
        txtUserName.setText(user.getName());

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
            }
        });

        btnLikedSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LikedSongListActivity.class));
            }
        });

        return view;
    }

    private void init() {
        txtUserName = view.findViewById(R.id.textview_user_name);
        btnSettings = view.findViewById(R.id.btn_user_settings);
        btnLikedSongs = view.findViewById(R.id.btn_list_liked_song);
    }

    private void getUser() {
        user = new User();
        SharedPreferences sharedPref = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        user.setId(sharedPref.getInt("user_id", -1));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }
}