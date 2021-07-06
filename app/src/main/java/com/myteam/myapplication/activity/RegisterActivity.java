package com.myteam.myapplication.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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

import com.myteam.myapplication.R;
import com.myteam.myapplication.data.RegisterLoginAsyncResponse;
import com.myteam.myapplication.data.RegisterLoginData;
import com.myteam.myapplication.model.User;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText txtUserName, txtUserEmail, txtUserPassword, txtUserPassword2;
    Button btnRegister;
    TextView txtResult;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUserName = findViewById(R.id.edittext_username_register);
        txtUserEmail = findViewById(R.id.edittext_useremail_register);
        txtUserPassword = findViewById(R.id.edittext_password_register);
        txtUserPassword2 = findViewById(R.id.edittext_password2_register);
        btnRegister = findViewById(R.id.button_register);
        txtResult = findViewById(R.id.textview_result_register);
        toolbar = findViewById(R.id.toolbar_register);


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


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserName.getText().toString().trim();
                String email = txtUserEmail.getText().toString().trim();
                String password = txtUserPassword.getText().toString().trim();
                String password2 = txtUserPassword2.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    txtResult.setText("Vui Lòng Điền Đầy Đủ Thông Tin");
                    return;

                } else if (!isValidEmail(email)) {
                    txtResult.setText("Email Không Hợp Lệ ");
                    return;
                } else if (password.length() < 6 || password.length() > 32) {
                    txtResult.setText("Mật khẩu chứa từ 6 - 32 ký tự");
                    return;
                } else if (!password.equals(password2)) {
                    txtResult.setText("Xác Nhận Mật Khẩu Không Khớp");
                    return;
                }

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);

                register(user);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);

                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_custom_success, viewGroup, false);
                Button btn = dialogView.findViewById(R.id.button_success);
                TextView txtTitleDialog = dialogView.findViewById(R.id.textview_title_custom_dialog_success);
                TextView txtMessageDialog = dialogView.findViewById(R.id.textview_message_custom_dialog_success);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                txtTitleDialog.setText("Đã Đăng Ký");
                txtMessageDialog.setText("Bạn Có Thể Đăng Nhập");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });
    }

    // SET CUSTOM DIALOG
    private void showCustomDialog() {

    }


    public void register(User user) {
//        user.setEmail("nguyenvana@gmail.com");
//        user.setPassword("123456");
//        user.setName("Nguyen Van A");

        new RegisterLoginData().register(user, new RegisterLoginAsyncResponse() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                Log.d("REGISTER", "From MainActivity Started");
                Log.d("REGISTER", "From MainActivity - response : " + mapResponse.get("result") + " | " + mapResponse.get("message"));

            }
        });
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}