package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.CliamsAdapter;
import com.smart.chatui.base.MyApplicationLike;
import com.smart.chatui.enity.UnReadMsgCount;
import com.smart.chatui.https.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.enity.Cliams;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.greendao.HisContactDao;
import trf.smt.com.netlibrary.utils.CacheUtils;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 未领会员消息
 */
public class UnCliamsActivity extends BaseActivity implements CliamsAdapter.onItemClickListener {


    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cantact_list)
    EasyRecyclerView mCantactList;
    private UDPClientUtils mUDPClientUtils;
    private Person mPerson;
    private boolean isOnPause;
    private List<Cliams.DataBean> mBeanList;
    private List<Person> mPersonList;

    private CliamsAdapter mCliamsAdapter;
    private String loginUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncliams_layout);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        mTvTitle.setText(getString(R.string.cliam_answer));
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        mBtnReflash.setVisibility(View.GONE);
        loginUserId = PreferenceUtils.getString(this, "userId", "");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new UnReadMsgCount());
                finish();
            }
        });
        mUDPClientUtils = UDPClientUtils.getInstance(this);
        mBeanList = new ArrayList<>();
        mPersonList = new ArrayList<>();

        mCliamsAdapter = new CliamsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCantactList.setLayoutManager(layoutManager);
        mCantactList.setAdapter(mCliamsAdapter);
        mCliamsAdapter.addItemClickListener(this);
        loadDbPersonInfo();
        loadCliamsDataForNet();
    }


    /**
     * 加载本地待回复人员信息
     */
    private void loadDbPersonInfo() {
        if (!CacheUtils.isTrue()) return;
        String sql = "select * from PERSON where USER_NAME in(" + CacheUtils.getGroupDbWxUserName() + ")";
        Cursor cur = MyApplicationLike.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cur.moveToNext()) {
            Person p = new Person();
            p.setUserName(cur.getString(cur.getColumnIndex("USER_NAME")));
            p.setNickName(cur.getString(cur.getColumnIndex("NICK_NAME")));
            p.setConRemark(cur.getString(cur.getColumnIndex("CON_REMARK")));
            p.setIconUrl(cur.getString(cur.getColumnIndex("ICON_URL")));
            p.setType(cur.getInt(cur.getColumnIndex("TYPE")));
            mPersonList.add(p);
        }
        cur.close();
    }

    /**
     * 远程获取消息记录
     */
    private void loadCliamsDataForNet() {
        showProgressDialog();
        String shopId = PreferenceUtils.getString(this, "shopId", "");
        String loginId = PreferenceUtils.getString(this, "loginId", "");
        String url = PreferenceUtils.getString(this, "BASEURL", "")
                + "/api/query/webchat/lastchatrecord/list.json?loginId=" + loginId + "";
        Map<String, String> map = new HashMap<>();
        map.put("int_storeId", shopId);
        map.put("str_deviceId", mUDPClientUtils.getToDeviceId());
        map.put("strs_weixinIds", CacheUtils.getGroupWxUserName());
        OkHttpUtils.post(url, JsonUtils.serialize(map), new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout(response);
                dismissProgressDialog();
                try {
                    Cliams cliams = JsonUtils.deserialize(response, Cliams.class);
                    if (cliams.getCode().equals("0000")) {
                        mBeanList.clear();
                        if (null == cliams.getData()) return;
                        for (Cliams.DataBean bean : cliams.getData()) {
                            bean.setMsgCount(CacheUtils.getMsgCount(bean.getFromuser()));
                            for (Person person : mPersonList) {
                                if (person.getUserName().equals(bean.getFromuser())) {
                                    bean.setIconUrl(person.getIconUrl());
                                    bean.setNikeName(TextUtils.isEmpty(person.getNickName()) ? person.getConRemark() : person.getNickName());
                                }
                            }
                            mBeanList.add(bean);
                        }
                        mCliamsAdapter.addAll(mBeanList);
                        mCliamsAdapter.notifyDataSetChanged();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onHeaderClick(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        showProgressDialog();
        Cliams.DataBean dataBean = mCliamsAdapter.getAllData().get(position);
        mPerson = dataBean.dataToPerson(dataBean);
        mUDPClientUtils.sendClaim(PreferenceUtils.getString(this, "userId", ""), dataBean.getFromuser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        dismissProgressDialog();
        if (isOnPause) return;
        if (obj instanceof AckValue) {
            AckValue ackValue = (AckValue) obj;
            LogUtils.sysout("====onClaim==UnCliamActivity===" + ackValue.toString());
            if (ackValue.getTag().equals("-1")) {
                String username = TextUtils.isEmpty(mPerson.getConRemark())
                        ? mPerson.getNickName() : mPerson.getConRemark();
                String message = username + "已被其他导购认领。";
                showErrorDialog(message);
            } else {
                //判断是否是自己认领的，如果不是则返回
                if (!ackValue.getTag().equals(loginUserId)) return;
                String loginUserId = PreferenceUtils.getString(this, "userId", "");
                List<HisContact> list = DbAppliaction.getDaoSession().getHisContactDao().queryBuilder()
                        .where(HisContactDao.Properties.UserName.eq(mPerson.getUserName()))
                        .where(HisContactDao.Properties.LoginUserId.eq(loginUserId))
                        .build().list();
                if (list.size() > 0) {
                    HisContact hisContact = list.get(0);
                    hisContact.setCreateTime(HisContact.getTime());
                    DbAppliaction.getDaoSession().getHisContactDao().update(hisContact);
                } else {
                    long id = DbAppliaction.getDaoSession().getHisContactDao()
                            .insertOrReplace(HisContact.personToHisContact(mPerson,
                                    PreferenceUtils.getString(this, "userId", "")));
                    LogUtils.sysout("insert id:" + id);
                }
                startActivity(new Intent(this, ChartActivity.class).putExtra("contact", mPerson));
                finish();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new UnReadMsgCount());
        }
        return super.onKeyDown(keyCode, event);
    }
}
