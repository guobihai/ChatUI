package com.smart.chatui.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smart.chatui.R;
import com.smart.chatui.enity.ErrMsg;
import com.smart.chatui.enity.Users;
import com.smart.chatui.https.OkHttpUtils;
import com.smart.chatui.ui.services.LoadDataServices;
import com.smart.chatui.ui.services.MsgServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;
import trf.smt.com.netlibrary.utils.SystemUitls;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.editText2)
    EditText mEditText2;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.icon)
    CircleImageView mIcon;
    private long exitTime = 0;
    private int clickCount;


    private Users.DataBean mDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mEtUserName.setText(PreferenceUtils.getString(this, "username", ""));
        mEditText2.setText(PreferenceUtils.getString(this, "pass", ""));
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setTitle("正在登录中...");
//        mLoadingDialog = new LoadingDialog(this,"正在登录中...");

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((System.currentTimeMillis() - exitTime) < 5000) {
                    clickCount++;
                    exitTime = System.currentTimeMillis();
                    if (clickCount == 10) {
                        clickCount=0;
                        String baseUrl = PreferenceUtils.getString(LoginActivity.this, "indexUrl",
                                "http://sos.csosm.com:9001");
                        final EditText et = new EditText(LoginActivity.this);
                        et.setText(baseUrl);
                        et.setSelection(baseUrl.length());
                        new AlertDialog.Builder(LoginActivity.this).setTitle("设置域名")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setView(et)
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String input = et.getText().toString();
                                        if (TextUtils.isEmpty(input)) {
                                            PreferenceUtils.putString(LoginActivity.this, "indexUrl", BASEURL);
                                        }
                                        if (input.endsWith("\\")) {
                                            Toast.makeText(LoginActivity.this, "不能以\\结尾", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        PreferenceUtils.putString(LoginActivity.this, "indexUrl", input);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                } else {
                    clickCount = 0;
                    exitTime = System.currentTimeMillis();
                }
            }
        });
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        if (!SystemUitls.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        final String pass = mEditText2.getText().toString();
        if (TextUtils.isEmpty(mEtUserName.getText().toString()) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "请输入用户名与密码", Toast.LENGTH_SHORT).show();
            return;
        }
        permissionCheck(this, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (!bln) {
                            permissionRequests(LoginActivity.this, Manifest.permission.READ_PHONE_STATE, new OnBooleanListener() {
                                @Override
                                public void onClick(boolean bln) {
                                    if (!bln) {
                                        showErrorDialog("拒绝权限可能影响系统部分功能使用。");
                                    } else {
                                        login(pass);
//                                        loadNetInfo(mEtUserName.getText().toString(), pass);
                                    }
                                }
                            });
                        } else {
                            login(pass);
//                            loadNetInfo(mEtUserName.getText().toString(), pass);
                        }
                    }
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO);


    }


    /**
     * 登录
     *
     * @param pass
     */
    private void login(final String pass) {
        showProgressDialog("正在登录中...");
        final String url = PreferenceUtils.getString(this, "indexUrl", BASEURL) + "/api/webchat/login.json";
        Map<String, String> map = new HashMap<>();
        map.put("username", mEtUserName.getText().toString());
        map.put("password", SystemUitls.md5(pass));
        OkHttpUtils.post(url, JsonUtils.serialize(map), new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("response:" + response);
                try {
                    Users users = JsonUtils.deserialize(response, Users.class);
                    if (users.getCode().equals("0000")) {
                        PreferenceUtils.putString(LoginActivity.this, "BASEURL", users.getData().getIP_TABLE().getBaseUrl());
                        PreferenceUtils.putString(LoginActivity.this, "username", mEtUserName.getText().toString());
                        PreferenceUtils.putString(LoginActivity.this, "pass", pass);
                        PreferenceUtils.putString(LoginActivity.this, "loginId", String.valueOf(users.getData().getLOGIN_ID()));
                        PreferenceUtils.putString(LoginActivity.this, "userId", String.valueOf(users.getData().getUSER_ID()));
                        PreferenceUtils.putString(LoginActivity.this, "shopId", String.valueOf(users.getData().getUSER_STORE_ID()));
                        PreferenceUtils.putString(LoginActivity.this, "devicesId", String.valueOf(users.getData().getDEVICE_ID()));
                        mDataBean = users.getData();
                        startService(new Intent(LoginActivity.this, LoadDataServices.class));
                        initUdp(mDataBean);
                    } else {
                        dismissProgressDialog();
                        ErrMsg errMsg = JsonUtils.deserialize(response, ErrMsg.class);
                        Toast.makeText(LoginActivity.this, errMsg.getDetail(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    try {
                        ErrMsg errMsg = JsonUtils.deserialize(response, ErrMsg.class);
                        Toast.makeText(LoginActivity.this, errMsg.getDetail(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
//                        e1.printStackTrace();
                        Toast.makeText(LoginActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Exception e) {
                dismissProgressDialog();
                showErrorDialog("连接服务异常");
                LogUtils.sysout("e:" + e.toString());
            }
        });
    }

    private void initUdp(Users.DataBean dataBean) {
        if (null == dataBean) return;
        Intent it = new Intent(this, MsgServices.class);
        it.putExtra("dataBean", dataBean);
        startService(it);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (obj instanceof String) {
            if (!obj.equals("LoginSuccess")) return;
            dismissProgressDialog();
            startActivity(new Intent(LoginActivity.this, TabActivity.class)
                    .putExtra("user", mDataBean));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }


}
