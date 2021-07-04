package com.myteam.myapplication.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.myteam.myapplication.R;

import org.jetbrains.annotations.NotNull;

public class CreateUserPlaylistDialog extends AppCompatDialogFragment {
    private EditText ediTextCreateUserPlaylist;
    private CreateUserPlaylistDialogListener listener;
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_user_playlist_diaglog,null);

        ediTextCreateUserPlaylist = view.findViewById(R.id.edittext_create_user_playlist);
        builder.setView(view)
                .setTitle("Tạo Playlist")
                .setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userPlaylistName = ediTextCreateUserPlaylist.getText().toString();
                        listener.applyText(userPlaylistName);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CreateUserPlaylistDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CreateUserPlaylistDialogListener");
        }

    }

    public interface CreateUserPlaylistDialogListener{
        void applyText(String playListName);
    }
}
