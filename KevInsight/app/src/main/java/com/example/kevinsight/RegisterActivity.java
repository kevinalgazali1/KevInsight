package com.example.kevinsight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kevinsight.Sqlite.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this); // Inisialisasi dbHelper

        TextInputEditText regUsernameInput = findViewById(R.id.username2);
        TextInputEditText regPassInput = findViewById(R.id.password2);
        TextInputEditText regRetypePassInput = findViewById(R.id.password3);
        Button buttonReg = findViewById(R.id.btnRegister);
        TextView tvLog = findViewById(R.id.login);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = regUsernameInput.getText().toString().trim();
                String pass = regPassInput.getText().toString().trim();
                String rePass = regRetypePassInput.getText().toString().trim();

                if (username.isEmpty() || pass.isEmpty() || rePass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(rePass)) {
                        if (dbHelper.checkUsername(username)) {
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean registeredSuccess = dbHelper.insertData(username, pass);
                            if (registeredSuccess) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLog.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
