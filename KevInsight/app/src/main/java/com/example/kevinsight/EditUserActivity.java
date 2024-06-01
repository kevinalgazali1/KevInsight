package com.example.kevinsight;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kevinsight.Sqlite.DatabaseHelper;

public class EditUserActivity extends AppCompatActivity {

    EditText etFullname, etUsername, etPassword, etRePass;
    Button btnSimpan;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etFullname = findViewById(R.id.et_fullname);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.editTextPassword);
        etRePass = findViewById(R.id.editTextRePassword);
        btnSimpan = findViewById(R.id.btn_simpan);
        dbHelper = new DatabaseHelper(this);

        // Ambil username pengguna yang login dari SharedPreferences
        String loggedInUsername = getLoggedInUsername();

        // Ambil data pengguna dari database
        if (loggedInUsername != null) {
            Cursor cursor = dbHelper.getUserData(loggedInUsername);
            if (cursor.moveToFirst()) {
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                // Tampilkan data pengguna di EditText
                etFullname.setText(fullname);
                etUsername.setText(username);

                // Password tidak ditampilkan untuk alasan keamanan
            }
            cursor.close();
        }

        btnSimpan.setOnClickListener(v -> {
            // Ambil data yang dimasukkan oleh pengguna
            String fullname = etFullname.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String rePass = etRePass.getText().toString();

            // Validasi input
            if (fullname.isEmpty() || username.isEmpty() || password.isEmpty() || rePass.isEmpty()) {
                Toast.makeText(EditUserActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                if (!password.equals(rePass)) {
                    Toast.makeText(EditUserActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Update data pengguna di database
                    boolean updated = dbHelper.updateUserData(loggedInUsername, fullname, username, password);
                    if (updated) {
                        Toast.makeText(EditUserActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Tutup aktivitas setelah pembaruan data
                    } else {
                        Toast.makeText(EditUserActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String getLoggedInUsername() {
        SharedPreferences preferences = getSharedPreferences(DatabaseHelper.PREF_NAME, MODE_PRIVATE);
        return preferences.getString(DatabaseHelper.KEY_LOGGED_IN_USERNAME, null);
    }
}
