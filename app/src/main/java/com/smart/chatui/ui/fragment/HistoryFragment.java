package com.smart.chatui.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.HisContactAdapter;
import com.smart.chatui.ui.activity.ChartActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.greendao.HisContactDao;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 最近消息
 */
public class HistoryFragment extends BaseFragment implements HisContactAdapter.onItemClickListener {

    EasyRecyclerView mCantactList;
    private View rootView;
    private HisContactAdapter mContactAdapter;
    private UDPClientUtils mUDPClientUtils;
    private String loginUserId;
    private HisContact mHisContact;
    private boolean isOnPause;
    private static final String[] items = {"标为已读", "删除该聊天"};

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUDPClientUtils = UDPClientUtils.getInstance(getActivity());
        loginUserId = PreferenceUtils.getString(getActivity(), "userId", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        mCantactList = rootView.findViewById(R.id.cantact_list);
        mContactAdapter = new HisContactAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCantactList.setLayoutManager(layoutManager);
        mCantactList.setAdapter(mContactAdapter);
        mContactAdapter.addItemClickListener(this);
        loadData();
        return rootView;
    }

    /**
     * 获取最近联系人
     */
    private void loadData() {
        mContactAdapter.clear();
        List<HisContact> list = mUDPClientUtils.loadNearMessageInfo();
        if (list.size() > 0) {
            mContactAdapter.addAll(list);
            mContactAdapter.notifyDataSetChanged();
        }

    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            loadData();
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
        LogUtils.sysout("chart fragment onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (isOnPause) return;
        if (obj instanceof AckValue) {
            dismissProgressDialog();
            AckValue ackValue = (AckValue) obj;
            LogUtils.sysout("====onClaim==ackValueSend===" + ackValue.toString());
            if (ackValue.getTag().equals("-1")) {
                String username = TextUtils.isEmpty(mHisContact.getConRemark())
                        ? mHisContact.getNickName() : mHisContact.getConRemark();
                String message = username + getString(R.string.hash_cliam);
                showErrorDialog(message);
            } else {
                //判断是否是自己认领的，如果不是则返回
                if (!ackValue.getTag().equals(loginUserId)) return;
                startActivity(new Intent(getActivity(), ChartActivity.class)
                        .putExtra("contact", HisContact.hisContactToPerson(mHisContact)));
            }
        } else if (obj instanceof String) {
            if (obj.equals("onResultHisContactListForJson"))
                loadData();
        }

    }

    @Override
    public void onHeaderClick(int position) {

    }


    @Override
    public void onItemClick(View view, int position) {
        mHisContact = mContactAdapter.getAllData().get(position);
        if (mHisContact.getUserName().equals("@chartRoom")) {
            EventBus.getDefault().post(mHisContact);
            return;
        }
        showProgressDialog(getString(R.string.cliam_loading));
        mUDPClientUtils.sendClaim(loginUserId, mHisContact.getUserName());
    }

    @Override
    public void onLongItemClick(View view, final int position) {
        mHisContact = mContactAdapter.getAllData().get(position);
        if (mHisContact.getUserName().equals("@chartRoom")) {
            EventBus.getDefault().post(mHisContact);
            return;
        }
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        loadData();
                        break;
                    case 1:
                        new android.app.AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                                .setMessage("删除后，将从最近消息列表中移除")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        List<HisContact> list = DbAppliaction.getDaoSession().getHisContactDao().queryBuilder()
                                                .where(HisContactDao.Properties.UserName.eq(mHisContact.getUserName())).build().list();
                                        if (list.size() > 0) {
                                            DbAppliaction.getDaoSession().getHisContactDao().delete(list.get(0));
                                            mContactAdapter.remove(position);
                                            mContactAdapter.notifyItemChanged(position);
                                        }
                                    }
                                }).show();

                        break;
                }
            }
        });
        listDialog.show();
    }
}
