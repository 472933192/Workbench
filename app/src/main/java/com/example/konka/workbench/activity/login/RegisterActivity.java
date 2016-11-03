package com.example.konka.workbench.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.domain.User;

import cn.bmob.v3.exception.BmobException;

/**
 * @author ChenXiaotao
 * 注册页面
 */
public class RegisterActivity extends Activity implements IRegisterView{

    private UserPresenter mUserPresenter = new UserPresenter(this);

    private ImageButton register;//定义注册按钮
    private ImageButton back;//定义返回按钮
    private EditText etUserName;//定义用户名称可编辑文本
    private EditText etPassword;//定义密码可编辑文本
    private EditText etConfigPassword;//定义确认密码可编辑文本

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register );//调用注册界面
        initView();//初始化视图
        initListener();//监听方法

    }
    private void initView() {
        etUserName = (EditText)findViewById(R.id.new_user_id);//获取user编辑文本实例
        etPassword = (EditText) findViewById(R.id.new_password_id);//获取password编辑文本实例
        etConfigPassword = (EditText) findViewById(R.id.confirm_password);//获取confirm_password编辑实例
        register = (ImageButton) findViewById(R.id.register);//获取register按钮实例
        back = (ImageButton) findViewById(R.id.back);//获取back按钮实例

    }

    public void initListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 是否需要对密码加密后进行传入
                // todo...
                //
                if(isNullOrEmpty(getUserName()) || isNullOrEmpty(getPassword()) || isNullOrEmpty(getConfigPassword())) {
                    Toast.makeText(RegisterActivity.this, "请把信息填写完整", Toast.LENGTH_SHORT).show();
                    return;//用户名、密码、确认密码有为空的则返回“请把信息填写完整”
                }
                if(!checkPassword(getPassword(), getConfigPassword())) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;//比较两次输入的密码，如果不一样则返回“两次输入的密码不一样”
                }
               mUserPresenter.register();//注册用户

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });//若点击“返回”按钮则返回到登录界面
    }



    private boolean checkPassword(String password, String configPassword) {
        return password.equals(configPassword);//比较密码与确认的密码是否相等
    }

    private boolean isNullOrEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    @Override
    public String getUserName() {
        return etUserName.getText().toString();//取得用户输入的用户名;
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();//取得用户输入的密码
    }

    @Override
    public String getConfigPassword() {
        return etConfigPassword.getText().toString();//取得用户输入的确认密码;
    }

    @Override
    public void toLoginActivity(User user) {
        Toast.makeText(RegisterActivity.this, "恭喜"+user.getUsername()+"用户注册成功" ,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);//注册成功，跳转到登录页面
        finish();
    }

    @Override
    public void showFailedError(BmobException e) {
        Log.e("注册失败!",e.getMessage());
        Toast.makeText(RegisterActivity.this,"该用户名已注册，请重新输入用户名",Toast.LENGTH_SHORT).show();
        etUserName.requestFocus();
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(RegisterActivity.this,"无网络连接，请检查您的手机网络.",Toast.LENGTH_SHORT).show();
    }
}//比较是否为空
