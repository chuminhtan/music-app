package com.myteam.myapplication.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button btnToRegister;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.edittext_username_login);
        txtPassword = findViewById(R.id.edittext_password_login);
        txtResult = findViewById(R.id.textview_result_login);
        toolbar = findViewById(R.id.toolbar_login);
        btnToRegister = findViewById(R.id.button_to_register);
        btnLogin = (Button) findViewById(R.id.button_login);

        // Create Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this,  RegisterActivity.class);
                LoginActivity.this.startActivity(mainIntent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                boolean isValid = isValidEmail(email);
                if (!isValidEmail(email)) {
                    txtResult.setText("Email Không Hợp Lệ");
                    return;
                } else if (password.length() <6 || password.length() > 32) {
                    txtResult.setText("Mật khẩu chứa từ 6 - 32 ký tự");
                    return;
                }

                login(email, password, v);
            }
        });

    }


    public void login(String email, String password, final View v) {
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

                    Log.d("LOGIN","From MainActivity-LOGIN sharedPreferences : "
                            + sharedPreferences.getInt("user_id", 0) + " | "
                            + sharedPreferences.getString("user_name", "NULL")
                            + sharedPreferences.getString("user_email", "NULL"));

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_custom_success, viewGroup, false);
                    Button btn = dialogView.findViewById(R.id.button_success);
                    TextView txtTitleDialog = dialogView.findViewById(R.id.textview_title_custom_dialog_success);
                    TextView txtMessageDialog = dialogView.findViewById(R.id.textview_message_custom_dialog_success);

                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    txtTitleDialog.setText("Đã Đăng Nhập");
                    txtMessageDialog.setText("Chào Mừng Quay Trở Lại");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.RELOAD_MENU_TAB=true;
                            alertDialog.cancel();
                            finish();

                        }
                    });

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_custom_success, viewGroup, false);
                    Button btn = dialogView.findViewById(R.id.button_success);
                    TextView txtTitleDialog = dialogView.findViewById(R.id.textview_title_custom_dialog_success);
                    TextView txtMessageDialog = dialogView.findViewById(R.id.textview_message_custom_dialog_success);

                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    txtTitleDialog.setText("Đăng Nhập Không Thành Công");
                    txtMessageDialog.setText("Vui Lòng Thử Lại");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });
    }


    // VALID EMAIL
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }











}