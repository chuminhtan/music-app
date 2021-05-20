package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.data.RegisterLoginAsyncResponse;
import com.myteam.myapplication.data.RegisterLoginData;
import com.myteam.myapplication.util.ServerInfo;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail;
    EditText txtPassword;
    TextView txtResult;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        txtEmail = findViewById(R.id.edittext_username_login);
        txtPassword = findViewById(R.id.edittext_password_login);
        txtResult = findViewById(R.id.textview_result_login);

        btnLogin = findViewById(R.id.button_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (isValid(email)) {
                    txtResult.setText("Email Không Hợp Lệ");
                    return;
                } else if (password.length() <6) {
                    txtPassword.setText("Mật Khẩu Không Hợp Lệ");
                }

                login(email, password);
            }
        });
    }

    public void login(String email, String password) {
//        String email = "nguyenvana@gmail.com";
//        String password="123456";

        new RegisterLoginData().login(email, password, new RegisterLoginAsyncResponse() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                String result = mapResponse.get("result");
                String message = mapResponse.get("message");

                Log.d("LOGIN","From MainActivity-LOGIN Started");
                Log.d("LOGIN","From MainActivity-LOGIN response : " + mapResponse.get("result") + " | " + mapResponse.get("message"));

                SharedPreferences sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

                if (mapResponse.containsKey("user_name") && mapResponse.get("user_name").length() > 1) {
                    Log.d("LOGIN","From MainActivity-LOGIN response : "
                            + mapResponse.get("user_id") + " | "
                            + mapResponse.get("user_email")
                            +mapResponse.get("user_name"));


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_name", mapResponse.get("user_name"));
                    editor.putString("user_email", mapResponse.get("user_email"));
                    editor.putInt("user_id", Integer.parseInt(mapResponse.get("user_id")));
                    editor.apply();
                }

                if (result.equalsIgnoreCase(ServerInfo.RESPONSE_SUCCESS)) {
                    sharedPreferences.getInt("user_id", 0);
                    sharedPreferences.getString("user_name", "NULL");
                    sharedPreferences.getString("user_email", "NULL");

                    Log.d("LOGIN","From MainActivity-LOGIN sharedPreferences : "
                            + sharedPreferences.getInt("user_id", 0) + " | "
                            + sharedPreferences.getString("user_name", "NULL")
                            + sharedPreferences.getString("user_email", "NULL"));
                }
            }
        });
    }


    // VALID EMAIL
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }











}