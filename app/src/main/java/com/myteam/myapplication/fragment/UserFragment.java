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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.EditUserInfoActivity;
import com.myteam.myapplication.activity.LikedSongListActivity;
import com.myteam.myapplication.activity.LoginActivity;
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
    Button btnEnterLogin;
    RelativeLayout relativeLayout;
    ImageView imageView;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        init();
        getUser();
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
            }
        });

        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LikedSongListActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
        changeUI();
    }

    private void init() {
        txtUserName = view.findViewById(R.id.textview_user_name);
        btnSettings = view.findViewById(R.id.btn_user_settings);
        btnEnterLogin = view.findViewById(R.id.button_enter_login);
        relativeLayout = view.findViewById(R.id.layout_likedsong);
        imageView = view.findViewById(R.id.img_arrow);


    }

    private void getUser() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
            user = new User();
            user.setId(sharedPref.getInt("user_id", 0));
            user.setName(sharedPref.getString("user_name", ""));
            user.setEmail(sharedPref.getString("user_email", ""));
    }

    private void changeUI() {
        if (user.getId() == 0) {
            txtUserName.setText("Chưa đăng nhập");
            btnEnterLogin.setVisibility(View.VISIBLE);
            btnSettings.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        } else {
            txtUserName.setText(user.getName());
            btnEnterLogin.setVisibility(View.GONE);
            btnSettings.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}