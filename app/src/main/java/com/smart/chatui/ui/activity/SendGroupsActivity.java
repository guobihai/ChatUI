package com.smart.chatui.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.smart.chatui.R;
import com.smart.chatui.adapter.AddImageAdapt;
import com.smart.chatui.enity.ImageBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群发界面
 */
public class SendGroupsActivity extends TakePhotoActivity implements AddImageAdapt.onItemClickListener {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.btnReflash)
    ImageView mBtnReflash;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvCount)
    TextView mTvCount;
    @BindView(R.id.checkMsg)
    CheckBox mCheckMsg;
    @BindView(R.id.checkWx)
    CheckBox mCheckWx;
    @BindView(R.id.recycleViewGallery)
    RecyclerView mRecycleViewGallery;
    @BindView(R.id.btnSelectModel)
    Button mBtnSelectModel;
    @BindView(R.id.btnComfirmSendGroup)
    Button mBtnComfirmSendGroup;
    private ArrayList<String> arrayList;
    private TakePhoto mTakePhoto;

    private AddImageAdapt mAddImageAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sen_groups);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        arrayList = this.getIntent().getExtras().getStringArrayList("listContact");
        mTakePhoto = getTakePhoto();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitle.setText(getString(R.string.send_group));
        mTvCount.setText(String.format("%s人", arrayList.size()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecycleViewGallery.setLayoutManager(gridLayoutManager);

        mAddImageAdapt = new AddImageAdapt(this);
        mAddImageAdapt.addItemClickListener(this);
        mAddImageAdapt.add(new ImageBean(ImageBean.TAKEPHOTO, R.drawable.ic_image_loading));
        mRecycleViewGallery.setAdapter(mAddImageAdapt);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        clearAddImage();
        for (TImage tImage : result.getImages()) {
            mAddImageAdapt.add(new ImageBean(ImageBean.PHOTO, tImage.getOriginalPath(), ImageBean.PHOTO));
        }
        if (mAddImageAdapt.getImageCount() < 3)
            mAddImageAdapt.add(new ImageBean(ImageBean.TAKEPHOTO, R.drawable.ic_image_loading));
        mAddImageAdapt.notifyDataSetChanged();
    }

    private void clearAddImage() {
        for (ImageBean bean : mAddImageAdapt.getAllData()) {
            if (bean.getType() == ImageBean.TAKEPHOTO)
                mAddImageAdapt.remove(bean);
        }
    }

    @OnClick({R.id.btnSelectModel, R.id.btnComfirmSendGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectModel:
                break;
            case R.id.btnComfirmSendGroup:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageBean imageBean = mAddImageAdapt.getAllData().get(position);
        if (imageBean.getType() == ImageBean.PHOTO) return;
        mTakePhoto.onPickFromGallery();
    }

    @Override
    public void onDeleteItemImage(View view, int position) {
        clearAddImage();
        mAddImageAdapt.remove(position);
        if (mAddImageAdapt.getImageCount() < 3)
            mAddImageAdapt.add(new ImageBean(ImageBean.TAKEPHOTO, R.drawable.ic_image_loading));
        mAddImageAdapt.notifyDataSetChanged();

    }
}
