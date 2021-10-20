package com.rikkathewrold.rikkamusic.login.mvp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.login.mvp.contract.LoginContract;
import com.rikkathewrold.rikkamusic.login.mvp.presenter.LoginPresenter;
import com.rikkathewrold.rikkamusic.util.ActivityStarter;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.InputUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.ScreenUtils;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    String phoneNumber;
    String password;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected LoginPresenter onCreatePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorBlack));
        setLeftTitleText(R.string.login_phone_number);
        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(this).getAccountNum())) {
            phoneNumber = SharePreferenceUtil.getInstance(this).getAccountNum();
            etPhone.setText(phoneNumber);
        }
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, "onResume");
        super.onResume();
        //设置状态栏为白底黑字
        ScreenUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        ScreenUtils.setStatusBarDarkFont(this, true);
    }

    @Override
    @OnClick({R.id.btn_login, R.id.register, R.id.forget_pwd})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
//        InputUtil.dismissInputMethod(this);
        switch (v.getId()) {
            case R.id.btn_login:
                phoneNumber = etPhone.getText().toString().trim();
                password = etPwd.getText().toString().trim();
                if (InputUtil.checkMobileLegal(phoneNumber) && InputUtil.checkPasswordLegal(password)) {
                    showDialog();
                    LogUtil.d(TAG, "login : " + phoneNumber + " ," + password);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                String TOKEN = "";
//                                //创建okhttp对象
//                                OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                                        .connectTimeout(2, TimeUnit.MINUTES)
//                                        .readTimeout(2, TimeUnit.MINUTES)
//                                        .build();
//                                //创建请求体
//                                String urlStr = "http://10.0.2.2:3000/login/cellphone?password=32107251zyr&phone=18987415349";
//                                Request request = new Request.Builder().addHeader("Authorization", TOKEN)
//                                        .url(urlStr)
//                                        .build();
//                                //创建响应体
//                                Response response = okHttpClient.newCall(request).execute();
//                                //                        response = okHttpClient.newCall(request).execute();
//                                String responseStr = response.body().string();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Log.e("ri",e.getMessage());
//                            }
//                        }
//                    }).start();
                    mPresenter.login(phoneNumber, password);
                }
                break;
            case R.id.register:
                ToastUtils.show(R.string.in_developing);
                break;
            case R.id.forget_pwd:
                ToastUtils.show(R.string.in_developing);
                break;
        }
    }

    @Override
    public void onLoginSuccess(LoginBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onLoginSuccess : " + bean);
        SharePreferenceUtil.getInstance(this).saveUserInfo(bean, phoneNumber);
        ActivityStarter.getInstance().startMainActivity(this);
        this.finish();
    }

    @Override
    public void onLoginFail(String error) {
        hideDialog();
        LogUtil.w(TAG, "bean : " + error);
        if (error.equals("HTTP 502 Bad Gateway")) {
            ToastUtils.show(R.string.enter_correct_password);
        } else {
            ToastUtils.show(error);
        }
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }
}
