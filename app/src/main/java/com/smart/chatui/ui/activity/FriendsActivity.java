package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.ContactAdapter;
import com.smart.chatui.enity.ResBean;
import com.smart.chatui.https.OkHttpUtils;
import com.smart.chatui.ui.sort.PinyinComparator;
import com.smart.chatui.ui.sort.PinyinUtils;
import com.smart.chatui.ui.views.ClearEditText;
import com.smart.chatui.ui.views.TitleItemDecoration;
import com.smart.chatui.ui.views.WaveSideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.greendao.PersonDao;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 通讯录
 */
public class FriendsActivity extends BaseActivity implements ContactAdapter.onItemClickListener {

    @BindView(R.id.cantact_list)
    EasyRecyclerView mCantactList;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sideBar)
    WaveSideBar mSideBar;
    @BindView(R.id.filter_edit)
    ClearEditText mFilterEdit;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    private ContactAdapter mContactAdapter;
    private TitleItemDecoration mDecoration;
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;
    private List<Person> mPersonList;
    private UDPClientUtils mUDPClientUtils;
    private Person mPerson;
    private boolean isOnPause;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtnReflash.setVisibility(View.VISIBLE);
        mPersonList = new ArrayList<>();
        mUDPClientUtils = UDPClientUtils.getInstance(this);
        loadData();
    }

    /**
     * 加载通讯录
     */
    private void loadData() {
        if (null == DbAppliaction.getDaoSession()) return;
        if (null == mComparator)
            mComparator = new PinyinComparator();
        List<Person> personList = DbAppliaction.getDaoSession().getPersonDao().queryBuilder()
                .where(PersonDao.Properties.Type.notEq(-1)).build().list();
        personList = filledData(personList);
        initView(personList);
    }

    private void initView(List<Person> personList) {
        mPersonList.clear();
        mPersonList.addAll(personList);
        Collections.sort(personList, mComparator);
        if (null == mContactAdapter) {
            mContactAdapter = new ContactAdapter(this);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mCantactList.setLayoutManager(layoutManager);
            mCantactList.setAdapter(mContactAdapter);
            mContactAdapter.addItemClickListener(this);
            mDecoration = new TitleItemDecoration(FriendsActivity.this, personList);
            //如果add两个，那么按照先后顺序，依次渲染。
            mCantactList.addItemDecoration(mDecoration);
            mCantactList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
        mContactAdapter.addAll(personList);
        mContactAdapter.notifyDataSetChanged();

        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mContactAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
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
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<Person> filledData(List<Person> date) {
        List<Person> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            Person sortModel = date.get(i);
            String name = TextUtils.isEmpty(sortModel.getConRemark()) ? sortModel.getNickName() : sortModel.getConRemark();
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(name);
            if (pinyin.length() > 0) {
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setLetters(sortString.toUpperCase());
                } else {
                    sortModel.setLetters("#");
                }

            } else {
                sortModel.setLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Person> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList.addAll(mPersonList);
        } else {
            filterDateList.clear();
            for (Person sortModel : mPersonList) {
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
        Collections.sort(filterDateList, mComparator);
        mContactAdapter.clear();
        mContactAdapter.addAll(filterDateList);
        mContactAdapter.notifyDataSetChanged();
    }


    /**
     * 远程获取通讯录
     */
    private void loadContactDataForNet() {
        showProgressDialog();
        String shopId = PreferenceUtils.getString(this, "shopId", "");
        String url = PreferenceUtils.getString(this, "BASEURL", "") + "/api/webchat/" + shopId + "/contacts.json";
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
//                LogUtils.sysout(response);
                dismissProgressDialog();
                try {
                    Type type = new TypeToken<ResBean<List<Person>>>() {

                    }.getType();
                    ResBean<List<Person>> listResBean = JsonUtils.deserialize(response, type);
                    LogUtils.sysout("res contact:" + listResBean.getData().size());
                    if (null != DbAppliaction.getDaoSession()
                            && null != listResBean.getData() && listResBean.getData().size() > 0) {
                        DbAppliaction.getDaoSession().getPersonDao().deleteAll();
                        DbAppliaction.getDaoSession().getPersonDao().insertOrReplaceInTx(listResBean.getData());
                        mContactAdapter.clear();
                        loadData();
                        Toast.makeText(FriendsActivity.this, "刷新成功.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FriendsActivity.this, "刷新失败.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onHeaderClick(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
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
//        ButterKnife.unbind(this);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btnReflash)
    public void onViewClicked() {
        loadContactDataForNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (isOnPause) return;
        if (obj instanceof AckValue) {
            dismissProgressDialog();
            AckValue ackValue = (AckValue) obj;
            LogUtils.sysout("====onClaim==friendsActivity===" + ackValue.toString());
            if (ackValue.getTag().equals("-1")) {
                String username = TextUtils.isEmpty(mPerson.getConRemark())
                        ? mPerson.getNickName() : mPerson.getConRemark();
                String message = username + getString(R.string.hash_cliam);
                showErrorDialog(message);
            } else {
                String loginUserId = PreferenceUtils.getString(this, "userId", "");
                if (!ackValue.getTag().equals(loginUserId)) return;
                startActivity(new Intent(this, ChartActivity.class)
                        .putExtra("contact", mPerson));
                finish();
            }
        }

    }
}
