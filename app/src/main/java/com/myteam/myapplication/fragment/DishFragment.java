package com.myteam.myapplication.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DishFragment extends Fragment {
    View view;
    CircleImageView circleImageView;
    ObjectAnimator objectAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dish, container, false);
        circleImageView = view.findViewById(R.id.imageviewcircle_song);
        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(13000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String urlImage = bundle.getString("urlImage");
            Picasso.with(getContext()).load(urlImage).into(circleImageView);
        }

        return view;
    }

    public void resumeAnimator() {
        objectAnimator.resume();
    }

    public void pauseAnimator() {
        objectAnimator.pause();
    }


    public boolean isExistsViewComponent() {
         if (circleImageView == null) return false;
         return true;
    }
    public void  changeCircleImage(String urlImage) {
            Picasso.with(getContext()).load(urlImage).into(circleImageView);
    }

}
