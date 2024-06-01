package com.example.kevinsight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kevinsight.Sqlite.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {

    TextView fullname, username, password;
    Button btnEdit;
    ImageView ivDelete;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullname = findViewById(R.id.tv_fullname);
        username = findViewById(R.id.tv_username);
        password = findViewById(R.id.tv_password);
        btnEdit = findViewById(R.id.btn_edit);
        ivDelete = findViewById(R.id.btn_delete);
        dbHelper = new DatabaseHelper(this);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditUserActivity.class);
                startActivity(intent);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tampilkan dialog konfirmasi sebelum menghapus
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Are you sure you want to delete your account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Hapus data pengguna dan arahkan ke LoginActivity
                                deleteUserAndNavigateToLogin();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Batal penghapusan
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Ambil username pengguna yang login dari SharedPreferences
        String loggedInUsername = getLoggedInUsername();

        // Ambil data pengguna dari database
        if (loggedInUsername != null) {
            Cursor cursor = dbHelper.getUserData(loggedInUsername);
            if (cursor.moveToFirst()) {
                String fullnameStr = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
                String usernameStr = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String passwordStr = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                // Isi TextView dengan data pengguna
                fullname.setText(fullnameStr);
                username.setText(usernameStr);
                password.setText(passwordStr);
            }
            cursor.close();
        }
    }

    private String getLoggedInUsername() {
        SharedPreferences preferences = getSharedPreferences(DatabaseHelper.PREF_NAME, MODE_PRIVATE);
        return preferences.getString(DatabaseHelper.KEY_LOGGED_IN_USERNAME, null);
    }

    private void deleteUserAndNavigateToLogin() {
        String loggedInUsername = getLoggedInUsername();
        // Hapus data pengguna dari database
        boolean deleted = dbHelper.deleteUserData(loggedInUsername);
        if (deleted) {
            // Logout pengguna
            dbHelper.logoutUser();
            // Arahkan ke LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Menutup activity saat ini
            Toast.makeText(ProfileActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfileActivity.this, "Failed to delete your account", Toast.LENGTH_SHORT).show();
        }
    }
}
