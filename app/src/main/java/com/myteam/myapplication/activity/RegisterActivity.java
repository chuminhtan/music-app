package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserName.getText().toString().trim();
                String email = txtUserEmail.getText().toString().trim();
                String password = txtUserPassword.getText().toString().trim();
                String password2 = txtUserPassword2.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||password2.isEmpty()) {
                    txtResult.setText("Vui Lòng Điền Đầy Đủ Thông Tin");

                } else if (isValid(email)) {
                    txtResult.setText("Email Không Hợp Lệ ");

                } else if (!password.equals(password2)) {
                    txtResult.setText("Xác Nhận Mật Khẩu Không Khớp");
                }

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);

                register(user);

                txtResult.setText("Đăng Ký Thành Công");
                

            }
        });
    }


    public void register(User user) {
//        user.setEmail("nguyenvana@gmail.com");
//        user.setPassword("123456");
//        user.setName("Nguyen Van A");

        new RegisterLoginData().register(user, new RegisterLoginAsyncResponse() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                Log.d("REGISTER","From MainActivity Started");
                Log.d("REGISTER","From MainActivity - response : " + mapResponse.get("result") + " | " + mapResponse.get("message"));

            }
        });
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}