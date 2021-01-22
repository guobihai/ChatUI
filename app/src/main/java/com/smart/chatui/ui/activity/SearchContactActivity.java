package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.SearchContactAdapter;

import trf.smt.com.netlibrary.enity.Contacts;

import com.smart.chatui.ui.sort.PinyinUtils;
import com.smart.chatui.ui.views.ClearEditText;
import com.smart.chatui.ui.views.RvDividerItemDecoration;

import trf.smt.com.netlibrary.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.greendao.HisContactDao;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 搜索通讯录
 */
public class SearchContactActivity extends BaseActivity implements SearchContactAdapter.onItemClickListener {


    @BindView(R.id.filter_edit)
    ClearEditText mFilterEdit;
    @BindView(R.id.tvCacle)
    TextView mTvCacle;
    @BindView(R.id.cantact_list)
    EasyRecyclerView mCantactList;

    private SearchContactAdapter mContactAdapter;
    private RvDividerItemDecoration mRvDividerItemDecoration;
    private LinearLayoutManager layoutManager;
    private Contacts mPerson;
    private boolean isOnPause;
    private boolean isEdit;
    private UDPClientUtils mUDPClientUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        ButterKnife.bind(this);
        isEdit = this.getIntent().getExtras().getBoolean("isEdit");
        EventBus.getDefault().register(this);
        mUDPClientUtils = UDPClientUtils.getInstance(this);
        mContactAdapter = new SearchContactAdapter(this, isEdit);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCantactList.setLayoutManager(layoutManager);
        mCantactList.setAdapter(mContactAdapter);
        mContactAdapter.addItemClickListener(this);
        mRvDividerItemDecoration = new RvDividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //如果add两个，那么按照先后顺序，依次渲染。
        mCantactList.addItemDecoration(mRvDividerItemDecoration);

        mFilterEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Contacts> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
//            filterDateList.addAll(mPersonList);
            mTvCacle.setText("取消");
        } else {
            filterDateList.clear();
            for (Contacts sortModel : CacheUtils.getContactsList()) {
                String name = TextUtils.isEmpty(sortModel.getConRemark()) ? sortModel.getNickName() : sortModel.getConRemark();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        || name.contains(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        mContactAdapter.clear();
        mContactAdapter.addAll(filterDateList);
        mContactAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tvCacle)
    public void onViewClicked() {
        ArrayList<String> stringList = mContactAdapter.getSelectListUserName();
        if (stringList.size() > 0) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("stringListUserName", stringList);
            setResult(1, intent);
        }
        finish();
    }

    @Override
    public void onHeaderClick(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (isEdit) {
            Contacts contacts = mContactAdapter.getAllData().get(position);
            contacts.setSelect(!contacts.isSelect());
            mContactAdapter.notifyItemChanged(position);
            ArrayList<String> stringList = mContactAdapter.getSelectListUserName();
            if (stringList.size() > 0) {
                mTvCacle.setText("确认");
            } else
                mTvCacle.setText("取消");
            return;
        }
        mPerson = mContactAdapter.getAllData().get(position);
        showProgressDialog(getString(R.string.cliam_loading));
        mUDPClientUtils.sendClaim(PreferenceUtils.getString(this, "userId", ""), mPerson.getUserName());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (isOnPause) return;
        if (obj instanceof AckValue) {
            dismissProgressDialog();
            AckValue ackValue = (AckValue) obj;
            LogUtils.sysout("====onClaim==SearchContactActivity===" + ackValue.toString());
            if (ackValue.getTag().equals("-1")) {
                String username = TextUtils.isEmpty(mPerson.getConRemark())
                        ? mPerson.getNickName() : mPerson.getConRemark();
                String message = username + "已被其他导购认领。";
                showErrorDialog(message);
            } else {
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
                            .insertOrReplace(HisContact.personToHisContact(mPerson.convertToPerson(mPerson),
                                    PreferenceUtils.getString(this, "userId", "")));
                    LogUtils.sysout("insert id:" + id);
                }
                startActivity(new Intent(this, ChartActivity.class)
                        .putExtra("contact", mPerson.convertToPerson(mPerson)));
                finish();
            }
        }

    }
}
