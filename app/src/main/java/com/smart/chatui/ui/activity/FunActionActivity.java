package com.smart.chatui.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.verbs.VerbInfoAdapter;
import com.smart.chatui.enity.VerbInfo;
import com.smart.chatui.ui.views.RvDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动处理
 */
public class FunActionActivity extends BaseActivity implements VerbInfoAdapter.onItemClickListener {


    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cantact_list)
    EasyRecyclerView mCantactList;
    private VerbInfoAdapter mVerbInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncliams_layout);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        String title = getIntent().getExtras().getString("title");
        int type = getIntent().getExtras().getInt("type");
        mTvTitle.setText(title);
        setSupportActionBar(mToolbar);
        mBtnReflash.setVisibility(View.GONE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mVerbInfoAdapter = new VerbInfoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCantactList.setLayoutManager(layoutManager);
        mCantactList.setAdapter(mVerbInfoAdapter);
        mCantactList.addItemDecoration(new RvDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mVerbInfoAdapter.addItemClickListener(this);
        //模拟数据
        switch (type) {
            case VerbInfo.ACTION_BIRTHDAY:
                mVerbInfoAdapter.addAll(VerbInfo.initListBirthdayInfo());
                break;
            case VerbInfo.ACTION_90:
                mVerbInfoAdapter.addAll(VerbInfo.initList90dayInfo());
                break;
            case VerbInfo.ACTION_HOLIDAY:
                break;
            case VerbInfo.ACTION_VERB:
                mVerbInfoAdapter.addAll(VerbInfo.initListVerbUpInfo());
                break;
            case VerbInfo.ACTION_SHLEEP:
                break;

        }
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, int position) {
    }


}
