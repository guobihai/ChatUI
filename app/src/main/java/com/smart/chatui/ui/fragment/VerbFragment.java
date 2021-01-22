package com.smart.chatui.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.verbs.VerbInfoAdapter;
import com.smart.chatui.enity.UnReadMsgCount;
import com.smart.chatui.enity.VerbInfo;
import com.smart.chatui.ui.activity.FunActionActivity;
import com.smart.chatui.ui.activity.UnCliamsActivity;
import com.smart.chatui.ui.views.RvDividerItemDecoration;
import trf.smt.com.netlibrary.utils.CacheUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import trf.smt.com.netlibrary.utils.LogUtils;

/**
 * 代办消息
 */
public class VerbFragment extends BaseFragment implements VerbInfoAdapter.onItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.verb_list)
    EasyRecyclerView mVerbList;
    Unbinder unbinder;

    private String mUserId;
    private VerbInfoAdapter mVerbInfoAdapter;


    public VerbFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static VerbFragment newInstance(String param1) {
        VerbFragment fragment = new VerbFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verb_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initWiget();
        return view;
    }

    private void initWiget() {
        mVerbInfoAdapter = new VerbInfoAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVerbList.setLayoutManager(layoutManager);
        mVerbList.setAdapter(mVerbInfoAdapter);
        mVerbList.addItemDecoration(new RvDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mVerbInfoAdapter.addItemClickListener(this);
        mVerbInfoAdapter.addAll(VerbInfo.initListVerbInfo());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        LogUtils.sysout("======update chatRoom count===");
        if (obj instanceof UnReadMsgCount) {
            String count = CacheUtils.getMsgCount();
            for (VerbInfo info : mVerbInfoAdapter.getAllData()) {
                switch (info.getType()) {
                    case VerbInfo.ACTION_MENBER:
                        info.setMsgCount(Integer.parseInt(count));
                        mVerbInfoAdapter.notifyDataSetChanged();
                        return;
                    case VerbInfo.ACTION_BIRTHDAY:
                    case VerbInfo.ACTION_90:
                    case VerbInfo.ACTION_VERB:
                        startActivity(new Intent(getActivity(), FunActionActivity.class)
                                .putExtra("title", info.getName())
                                .putExtra("type", info.getType()));
                        return;
                    case VerbInfo.ACTION_HOLIDAY:
                    case VerbInfo.ACTION_SHLEEP:
                        return;
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        VerbInfo info = mVerbInfoAdapter.getAllData().get(position);
        switch (info.getType()) {
            case VerbInfo.ACTION_MENBER:
                if (!CacheUtils.isTrue()) {
                    showErrorDialog("没有待回复的会员消息");
                    return;
                }
                startActivity(new Intent(getActivity(), UnCliamsActivity.class));
                break;
            case VerbInfo.ACTION_BIRTHDAY:
            case VerbInfo.ACTION_90:
            case VerbInfo.ACTION_VERB:
                startActivity(new Intent(getActivity(), FunActionActivity.class)
                        .putExtra("title", info.getName())
                        .putExtra("type", info.getType()));
                break;
            case VerbInfo.ACTION_HOLIDAY:
            case VerbInfo.ACTION_SHLEEP:
                break;
        }

    }

    @Override
    public void onLongItemClick(View view, int position) {

    }
}
