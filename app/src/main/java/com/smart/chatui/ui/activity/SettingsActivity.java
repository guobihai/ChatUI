package com.smart.chatui.ui.activity;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.chatui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.utils.LogUtils;


public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.settings_container)
    FrameLayout mSettingsContainer;
    private SettingsFragment mSettingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTvTitle.setText(getString(R.string.title_setting));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * 当设置铃声之后的回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        // 得到我们选择的铃声
        Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        if (pickedUri != null) {
            LogUtils.sysout("=====ringpath:"+pickedUri.getPath());
            switch (requestCode) {
                case 1001:
                    // 将我们选择的铃声设置成为默认通知铃声
                    /**
                     * 敲黑板:黑代码解决Android 7.0 调用系统通知无法播放声音的问题
                     */
                    grantUriPermission("com.android.systemui", pickedUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_NOTIFICATION, pickedUri);
                    break;
            }
        }
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
            addPreferencesFromResource(R.xml.pref_setting);

//            Preference voice = findPreference("voice_msg");
//
//            voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    // 打开系统铃声设置
//                    Intent intent = new Intent(
//                            RingtoneManager.ACTION_RINGTONE_PICKER);
//                    // 设置铃声类型和title
//                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
//                            RingtoneManager.TYPE_NOTIFICATION);
//                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
//                            " 设置通知铃声");
//                    // 当设置完成之后返回到当前的Activity
//                    getActivity().startActivityForResult(intent, 1001);
//                    return false;
//                }
//            });
        }
    }


}
