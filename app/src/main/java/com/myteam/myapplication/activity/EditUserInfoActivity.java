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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.myteam.myapplication.R;
import com.myteam.myapplication.data.UserAsyncResponse;
import com.myteam.myapplication.data.UserData;
import com.myteam.myapplication.fragment.NoUserFragment;
import com.myteam.myapplication.model.User;

public class EditUserInfoActivity extends AppCompatActivity {
    EditText txtUserName, txtCurrentPassword, txtNewPassword, txtNewPassword2;
    Button btnEdit, btnLogout;
    TextView txtResult;
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        getUser();

        txtUserName = findViewById(R.id.edittext_username_info);
        txtCurrentPassword = findViewById(R.id.edittext_password_current);
        txtNewPassword = findViewById(R.id.edittext_password_info);
        txtNewPassword2 = findViewById(R.id.edittext_password2_info);
        btnEdit = findViewById(R.id.button_edit_user_info);
        txtResult = findViewById(R.id.textview_result_edit);
        toolbar = findViewById(R.id.toolbar_edit_user);
        txtUserName.setText(user.getName());
        btnLogout = findViewById(R.id.button_logout);

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

        // Event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
                removeUser();
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = txtUserName.getText().toString().trim();
                final String currentPassword = txtCurrentPassword.getText().toString().trim();
                final String newPassword = txtNewPassword.getText().toString().trim();
                String newPassword2 = txtNewPassword2.getText().toString().trim();

                if (name.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()) {
                    txtResult.setText("Vui lòng nhập đủ thông tin");
                    return;
                } else if (!newPassword.equals(newPassword2)) {
                    txtResult.setText("Xác Nhận Mật Khẩu Không Khớp");
                    clearText();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfoActivity.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Thông tin của bạn sẽ được thay đổi");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hàm đổi thông tin ở đây
                        changeUser(name, currentPassword, newPassword);
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Đã Hủy", Toast.LENGTH_SHORT).show();
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

    private void changeUser(String name, String currentPassword, String newPassword) {
        new UserData().changeInfo(user.getId(), name, currentPassword, newPassword, new UserAsyncResponse() {
            @Override
            public void processFinished(String result) {
                if (result.equalsIgnoreCase("success")) {
                    txtResult.setText("Đã Cập Nhật Thông Tin");
                    clearText();
                } else {
                    txtResult.setText("Cập Nhật Không Thành Công");
                }
            }
        });
    }

    private void clearText() {
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtNewPassword2.setText("");
    }

    private void removeUser() {
        SharedPreferences sharedPreferences = EditUserInfoActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        MainActivity.RELOAD_MENU_TAB = true;
        Toast.makeText(EditUserInfoActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();
    }

}
