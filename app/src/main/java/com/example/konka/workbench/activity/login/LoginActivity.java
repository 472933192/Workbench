package com.example.konka.workbench.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.konka.workbench.activity.main.MainActivity;
import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.message.ListenerControlPanel;
import com.example.konka.workbench.activity.message.LogListener;
import com.example.konka.workbench.activity.message.MessageDBHelper;
import com.example.konka.workbench.activity.message.MessageDownload;


/**@author Chenxiaotao
 * 登陆页面
 */
public class LoginActivity extends Activity implements View.OnClickListener,ILoginView {

    private Button login;
    private Button register;
    private EditText userName;
    private EditText password;
    private UserPresenter mUserPresenter=new UserPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        login= (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        userName = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.password_id);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                mUserPresenter.login();
                break;
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public String getUserName() {
        return userName.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    /**
     * 登陆成功，跳转到主页面
     */
    @Override
    public void toMainActivity() {
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        MessageDownload messageDownload = new MessageDownload();
        messageDownload.messageDownload(); // 启动消息下载
        finish();//销毁activity
    }

    @Override
    public void showFailedError() {
        Toast.makeText(LoginActivity.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(LoginActivity.this,"无网络连接，请检查您的手机网络.",Toast.LENGTH_SHORT).show();
    }
}
