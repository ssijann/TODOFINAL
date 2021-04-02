package com.sij.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin, btnCancel;

    AlertDialog.Builder mAlterDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername=findViewById(R.id.splash_txt_username);
        txtPassword= findViewById(R.id.splash_txt_password);

        btnLogin=findViewById(R.id.splash_btn_login);
        btnCancel=findViewById(R.id.splash_btn_cancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=txtUsername.getText().toString();
                String password=txtPassword.getText().toString();

                if(userName.equals("")){
                    txtUsername.setError(getString(R.string.login_username_required));
                    txtUsername.requestFocus();
                }
                else if(password.equals("")){
                    txtPassword.setError(getString(R.string.login_password_required));
                    txtPassword.requestFocus();
                }
                else{
                    if(userName.equals("admin")&&password.equals("admin")){
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("authentication", true);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        txtUsername.setError(getString(R.string.login_invalid_login));
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlterDialog = new AlertDialog.Builder(LoginActivity.this);
                mAlterDialog.setMessage(getString(R.string.quit_application))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);
                mAlterDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlterDialog.show();
            }
        });

    }
}
