package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.smart.chatui.R;
import com.smart.chatui.adapter.CommonFragmentPagerAdapter;
import com.smart.chatui.enity.UnReadMsgCount;
import com.smart.chatui.enity.Users;
import com.smart.chatui.ui.fragment.ChatFragment;
import com.smart.chatui.ui.fragment.HistoryFragment;
import com.smart.chatui.ui.fragment.VerbFragment;
import com.smart.chatui.ui.services.MsgServices;
import com.smart.chatui.util.BadgeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.utils.CacheUtils;
import trf.smt.com.netlibrary.utils.LogUtils;

public class TabActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.chartRoom)
    RadioButton mChartRoom;
    @BindView(R.id.nearMsg)
    RadioButton mNearMsg;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabViewpager)
    ViewPager mViewpager;
    @BindView(R.id.foward_do)
    RadioButton mFowardDo;

    private Users.DataBean dataBean;
    private UDPClientUtils mUDPClientUtils;

    private ArrayList<Fragment> fragments;

    private CommonFragmentPagerAdapter adapter;
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
            }
        });
        dataBean = (Users.DataBean) this.getIntent().getExtras().getSerializable("user");
        initUdp();
        mRadioGroup.setOnCheckedChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(ChatFragment.newInstance(String.valueOf(dataBean.getUSER_ID())));
        fragments.add(HistoryFragment.newInstance());
        fragments.add(VerbFragment.newInstance(String.valueOf(dataBean.getUSER_ID())));
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curFragment = fragments.get(position);
                switch (position) {
                    case 0:
                        mChartRoom.setChecked(true);
                        break;
                    case 1:
                        mNearMsg.setChecked(true);
                        break;
                    case 2:
                        mFowardDo.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chart:
                        startActivity(new Intent(TabActivity.this, GroupsActivity.class).putExtra("isEdit", false));
                        break;
                    case R.id.navigation_group:
                        startActivity(new Intent(TabActivity.this, GroupsActivity.class).putExtra("isEdit", true));
                        break;
                    case R.id.navigation_setting:
                        startActivity(new Intent(TabActivity.this, SettingsActivity.class));
                        break;
                    case R.id.sendFriendsMsg:
                        startActivity(new Intent(TabActivity.this, SendFriendsActivity.class));
                        break;
                    case R.id.navigation_exit:
                        BadgeUtil.resetBadgeCount(getApplicationContext());
                        mUDPClientUtils.logout(new UDPClientUtils.onLogoutInterface() {
                            @Override
                            public void onStartLogout() {
                                stopService(new Intent(TabActivity.this, MsgServices.class));
                                showProgressDialog("正在退出...");
                            }

                            @Override
                            public void onFinishLogout() {
                                dismissProgressDialog();
                                finish();
                                System.exit(0);
                            }
                        });
                        break;
                }
                return false;
            }
        });
//        BadgeUtil.setBadgeCount(getApplicationContext(), 99);
    }

    private void initUdp() {
        if (null == dataBean) return;
        mUDPClientUtils = UDPClientUtils.getInstance(this);
        mUDPClientUtils.setToDevicesId(dataBean.getDEVICE_ID());
        mUDPClientUtils.login(String.valueOf(dataBean.getUSER_ID()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (null != curFragment && curFragment instanceof ChatFragment && !((ChatFragment) curFragment).onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.sysout("=======羿聊系统退出======");
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (obj instanceof HisContact)
            mViewpager.setCurrentItem(0);
        else if (obj instanceof UnReadMsgCount) {
            BadgeUtil.setBadgeCount(getApplicationContext(), Integer.parseInt(CacheUtils.getMsgCount()));
            mChartRoom.setText(CacheUtils.getMsgCount().equals("0") ? getString(R.string.chat_room) :
                    String.format(getString(R.string.chat_room) + "(%s)", CacheUtils.getMsgCount()));
            mFowardDo.setText(CacheUtils.getMsgCount().equals("0") ? getString(R.string.foword) :
                    String.format(getString(R.string.foword) + "(%s)", CacheUtils.getMsgCount()));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int id = radioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.chartRoom:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.nearMsg:
                mViewpager.setCurrentItem(1);
                break;
            case R.id.foward_do:
                mViewpager.setCurrentItem(2);
                break;
        }
    }


}
