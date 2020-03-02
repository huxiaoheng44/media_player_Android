package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username_edit;
    private EditText password_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_button = (Button)findViewById(R.id.login_button);
        Button exit_button = (Button)findViewById(R.id.exit_button);
        username_edit = (EditText)findViewById(R.id.login_username);
        password_edit = (EditText)findViewById(R.id.login_password);
        login_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);
        //记住密码复选框
        final CheckBox savenameChk=(CheckBox)findViewById(R.id.chk_savename);
        //保存密码

        SharedPreferences pref = getSharedPreferences("userinfo", MODE_PRIVATE);
        username_edit.setText(pref.getString("uname", ""));


    }


    private void login(){
        String password = password_edit.getText().toString();
        String username = username_edit.getText().toString();
        final CheckBox savenameChk=(CheckBox)findViewById(R.id.chk_savename);
        if(username_edit.getText().toString()==null||username_edit.getText().toString().equals("") || !password_edit.getText().toString().equals("123456")){
            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }else{
            //登录成功的话
            SharedPreferences.Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
            if(savenameChk.isChecked()){
                editor.putString("username", username_edit.getText().toString());
            }else{
                editor.putString("username", "");
            }
            editor.apply();

            //跳转界面
            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,ListActivity.class);
            intent.putExtra("username",username);
            finish();
            startActivity(intent);

        }
//        if(password.equals("123456")){
//            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this,ListActivity.class);
//            intent.putExtra("username",username);
//            finish();
//            startActivity(intent);
//        }else{
//            Toast.makeText(MainActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:{
                login();
                break;
            }
            case R.id.exit_button:{
                finish();
                break;
            }
        }
    }
}
