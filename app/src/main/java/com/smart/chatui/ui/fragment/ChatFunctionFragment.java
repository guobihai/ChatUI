package com.smart.chatui.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.smart.chatui.R;
import com.smart.chatui.base.BaseFragment;
import com.smart.chatui.util.MsgType;
import com.smart.chatui.util.UploadFileToNet;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;
import trf.smt.com.netlibrary.utils.SystemUitls;

/**
 * 作者：Rance on 2016/12/13 16:01
 * 邮箱：rance935@163.com
 */
public class ChatFunctionFragment extends BaseFragment {
    private View rootView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private File output;
    private Uri imageUri;
    private TakePhoto takePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat_function, container, false);
            ButterKnife.bind(this, rootView);
            takePhoto = getTakePhoto();
        }
        return rootView;
    }

    @OnClick({R.id.chat_function_photo, R.id.chat_function_photograph})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_function_photograph:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                } else {
                    takePhoto();
                }
                break;
            case R.id.chat_function_photo:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                } else {
                    choosePhoto();
                }
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (null == result || result.getImages() == null) return;
        for (final TImage tImage : result.getImages()) {
            LogUtils.sysout("==========path", tImage.getOriginalPath());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setImageUrl(tImage.getOriginalPath());
                    messageInfo.setMsgType(MsgType.SEND_TYPE_IMG);
                    EventBus.getDefault().post(messageInfo);
                    String baseUrl = PreferenceUtils.getString(getActivity(), "filePath", "http://192.168.5.251:5050/");
                    UploadFileToNet.uploadFile(baseUrl, tImage.getOriginalPath(), SystemUitls.initPhoneIMEI(getActivity()),
                            MsgType.SEND_TYPE_IMG);
                }
            }, 1000);


        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        LogUtils.sysout("==err==msg======path", msg);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output = new File(file, System.currentTimeMillis() + ".jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
//        imageUri = Uri.fromFile(output);
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, CROP_PHOTO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(),
                    getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                    output);
        } else {
            imageUri = Uri.fromFile(output);
        }
        takePhoto.onPickFromCapture(imageUri);
    }

    /**
     * 从相册选取图片
     */
    private void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        takePhoto.onPickFromGallery();
    }

//    public void onActivityResult(int req, int res, Intent data) {
//        switch (req) {
//            case CROP_PHOTO:
//                if (res == Activity.RESULT_OK) {
//                    try {
//                        String baseUrl = PreferenceUtils.getString(getActivity(), "filePath", "http://192.168.5.251:5050/");
//                        MessageInfo messageInfo = new MessageInfo();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            Uri inputUri = FileProvider.getUriForFile(getActivity(),
//                                    getActivity().getApplicationContext().getPackageName() + ".fileprovider", output);
//                            //通过FileProvider创建一个content类型的Uri
//                            messageInfo.setImageUrl(inputUri.getPath());
//                            messageInfo.setMsgType(MsgType.SEND_TYPE_IMG);
//                            EventBus.getDefault().post(messageInfo);
//                            UploadFileToNet.uploadFile(baseUrl, inputUri.getPath(), SystemUitls.initPhoneIMEI(getActivity()),
//                                    MsgType.SEND_TYPE_IMG);
//                        } else {
//                            messageInfo.setImageUrl(imageUri.getPath());
//                            messageInfo.setMsgType(MsgType.SEND_TYPE_IMG);
//                            EventBus.getDefault().post(messageInfo);
//                            UploadFileToNet.uploadFile(baseUrl, imageUri.getPath(), SystemUitls.initPhoneIMEI(getActivity()),
//                                    MsgType.SEND_TYPE_IMG);
//                        }
//
//                    } catch (Exception e) {
//                    }
//                } else {
//                    Log.d(Constants.TAG, "失败");
//                }
//
//                break;
//            case REQUEST_CODE_PICK_IMAGE:
//                if (res == Activity.RESULT_OK) {
//                    try {
//                        Uri uri = data.getData();
//                        MessageInfo messageInfo = new MessageInfo();
//                        messageInfo.setImageUrl(getRealPathFromURI(uri));
//                        messageInfo.setMsgType(MsgType.SEND_TYPE_IMG);
//                        EventBus.getDefault().post(messageInfo);
//                        String baseUrl = PreferenceUtils.getString(getActivity(), "filePath", "http://192.168.5.251:5050/");
//                        UploadFileToNet.uploadFile(baseUrl, getRealPathFromURI(uri), SystemUitls.initPhoneIMEI(getActivity()),
//                                MsgType.SEND_TYPE_IMG);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.d(Constants.TAG, e.getMessage());
//                    }
//                } else {
//                    Log.d(Constants.TAG, "失败");
//                }
//
//                break;
//
//            default:
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


}
