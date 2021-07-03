package com.myteam.myapplication.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.User;

public class EditUserInfoActivity extends AppCompatActivity {
    EditText txtUserName, txtUserEmail, txtUserPassword, txtUserPassword2;
    Button btnEdit;
    TextView txtResult;
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        getUser();

        txtUserName = findViewById(R.id.edittext_username_info);
        txtUserEmail = findViewById(R.id.edittext_useremail_info);
        txtUserPassword = findViewById(R.id.edittext_password_info);
        txtUserPassword2 = findViewById(R.id.edittext_password2_info);
        btnEdit = findViewById(R.id.button_edit_user_info);
        txtResult = findViewById(R.id.textview_result_edit);
        toolbar = findViewById(R.id.toolbar_edit_user);

        txtUserName.setText(user.getName());
        txtUserEmail.setText(user.getEmail());

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserName.getText().toString().trim();
                String email = txtUserEmail.getText().toString().trim();
                String password = txtUserPassword.getText().toString().trim();
                String password2 = txtUserPassword2.getText().toString().trim();

                if (password.isEmpty()) {
                    txtResult.setText("Vui lòng nhập đầy đủ 2 trường mật khẩu");
                    return;
                } else if (password2.isEmpty()) {
                    txtResult.setText("Vui lòng nhập đầy đủ 2 trường mật khẩu");
                    return;
                } else if (!password.equals(password2)) {
                    txtResult.setText("Xác Nhận Mật Khẩu Không Khớp");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfoActivity.this);
                builder.setTitle("Xác nhận đổi mật khẩu");
                builder.setMessage("Mật khẩu của bạn sẽ được thay đổi");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hàm đổi mật khẩu ở đây

                        Toast.makeText(getApplicationContext(), "Mật khẩu đã được cập nhật",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Hủy đổi mật khẩu",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Hiển thị dialog
                dialog.show();

            }
        });
    }

    private void getUser() {
        user = new User();
        SharedPreferences sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE);
        user.setId(sharedPref.getInt("user_id", -1));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }



}
