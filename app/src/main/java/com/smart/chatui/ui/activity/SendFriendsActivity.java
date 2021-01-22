package com.smart.chatui.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.chatui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trf.smt.com.netlibrary.UDPClientUtils;

/**
 * 发朋友圈
 */
public class SendFriendsActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btnSend)
    Button mBtnSend;
    @BindView(R.id.etValue)
    EditText mEtValue;

    private UDPClientUtils mUDPClientUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friends);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        mTvTitle.setText(getString(R.string.send_friends_msg));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUDPClientUtils = UDPClientUtils.getInstance(this);
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        String content = mEtValue.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showErrorDialog("发送内容不能为空");
            return;
        }
        mUDPClientUtils.sendFriendsMsg(content, "");
        mEtValue.setText("");
        Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
        finish();
    }

}
