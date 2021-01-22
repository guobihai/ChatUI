package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.smart.chatui.R;
import com.smart.chatui.adapter.groups.RecyclerAdapter;
import com.smart.chatui.adapter.groups.SecondaryListAdapter;
import com.smart.chatui.enity.ContactsInfo;
import com.smart.chatui.enity.GroupContacts;
import com.smart.chatui.enity.ResBean;
import com.smart.chatui.https.OkHttpUtils;
import com.smart.chatui.ui.views.RvDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.enity.Contacts;
import trf.smt.com.netlibrary.utils.CacheUtils;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 分组通讯录
 */

public class GroupsActivity extends BaseActivity implements RecyclerAdapter.onItemClickListener {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.search_layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.btnSelectContact)
    Button mBtnSelectContact;
    @BindView(R.id.btnSelectCondition)
    Button mBtnSelectCondition;
    @BindView(R.id.funBottomLayout)
    LinearLayout mFunBottomLayout;
    private RecyclerAdapter adapter;
    private List<SecondaryListAdapter.DataTree<GroupContacts, Contacts>> datas = new ArrayList<>();
    private UDPClientUtils mUDPClientUtils;
    private Contacts mPerson;
    private boolean isOnPause;
    private boolean isEdit;
    private Set<String> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        isEdit = this.getIntent().getExtras().getBoolean("isEdit");
        mToolbar.setTitle("");
        mTvTitle.setText(getString(R.string.title_contact));
        setSupportActionBar(mToolbar);
        mBtnReflash.setVisibility(View.VISIBLE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUDPClientUtils = UDPClientUtils.getInstance(this);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new RvDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new RecyclerAdapter(this, isEdit);
        adapter.addItemClickListener(this);
        mRecycleView.setAdapter(adapter);
        loadContactInfo();
        mFunBottomLayout.setVisibility(isEdit ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载通讯录信息
     */
    private void loadContactInfo() {
        showProgressDialog();
        String userId = PreferenceUtils.getString(this, "userId", "");
        String url = PreferenceUtils.getString(this, "BASEURL", "")
                + "/api/webchat/group/queryGroupList.json?userId=" + userId;
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
//                LogUtils.sysout("====group info :" + response);
                try {
                    dismissProgressDialog();
                    Type type = new TypeToken<ResBean<List<GroupContacts>>>() {
                    }.getType();

                    ResBean<List<GroupContacts>> listResBean = JsonUtils.deserialize(response, type);
//                    LogUtils.sysout(JsonUtils.serialize(listResBean));
//                    ContactsInfo contactsInfo = JsonUtils.deserialize(response, ContactsInfo.class);
                    if (null == listResBean || !listResBean.isSuccess()) return;
                    datas.clear();
                    for (GroupContacts groupContacts : listResBean.getData()) {
                        if (!groupContacts.isVerbMenber())
                            datas.add(new SecondaryListAdapter.DataTree<GroupContacts, Contacts>(groupContacts, groupContacts.getChildren()));
                    }
                    adapter.addAll(datas);
                    CacheUtils.putAllContacts(adapter.getAllItemConcacts());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    showErrorDialog("获取通讯录服务异常");
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Exception e) {
                showErrorDialog("获取通讯录服务异常");
                dismissProgressDialog();
            }
        });
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
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHeaderClick(int position) {

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder view, int groupItemIndex, int subItemIndex) {
        if (isEdit) return;
        mPerson = datas.get(groupItemIndex).getSubItems().get(subItemIndex);
        if (mPerson.getType() == -1) return;
        showProgressDialog(getString(R.string.cliam_loading));
        mUDPClientUtils.sendClaim(PreferenceUtils.getString(this, "userId", ""), mPerson.getUserName());
    }

    @Override
    public void onItemSelectContacts(RecyclerView.ViewHolder view, int position, int subItemIndex) {
        contactsList = adapter.getSelectListContact();
        mBtnSelectContact.setText(String.format(getString(R.string.select_group_contacts) + "(%s)", contactsList.size()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (isOnPause) return;
        if (obj instanceof AckValue) {
            dismissProgressDialog();
            AckValue ackValue = (AckValue) obj;
            LogUtils.sysout("====onClaim==GroupsActivity===" + ackValue.toString());
            if (ackValue.getTag().equals("-1")) {
                String username = TextUtils.isEmpty(mPerson.getConRemark())
                        ? mPerson.getNickName() : mPerson.getConRemark();
                String message = username + getString(R.string.hash_cliam);
                showErrorDialog(message);
            } else {
                String loginUserId = PreferenceUtils.getString(this, "userId", "");
                //判断是否是自己认领的，如果不是则返回
                if (!ackValue.getTag().equals(loginUserId)) return;
                startActivity(new Intent(this, ChartActivity.class)
                        .putExtra("contact", mPerson.convertToPerson(mPerson)));
                finish();
            }
        }

    }

    @OnClick(R.id.btnReflash)
    public void onViewClicked() {
        loadContactInfo();
    }

    @OnClick({R.id.btnSelectContact, R.id.btnSelectCondition, R.id.search_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectContact:
                if (null == contactsList || contactsList.size() == 0) {
                    showErrorDialog("您还没选择群发会员");
                    return;
                }
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(contactsList);
                startActivity(new Intent(this, SendGroupsActivity.class)
                        .putStringArrayListExtra("listContact", arrayList));
                break;
            case R.id.btnSelectCondition:
                break;
            case R.id.search_layout:
                startActivityForResult(new Intent(this, SearchContactActivity.class)
                        .putExtra("isEdit", isEdit), 1);
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            if (null == data || data.getExtras() == null) return;
            ArrayList<String> stringArrayList = data.getExtras().getStringArrayList("stringListUserName");
            contactsList = adapter.getSelectListContact();
            contactsList.addAll(stringArrayList);
            mBtnSelectContact.setText(String.format(getString(R.string.select_group_contacts) + "(%s)", contactsList.size()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
