package com.dji.sdk.sample.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.BaseThreeBtnView;
import com.dji.sdk.sample.common.DJISampleApplication;
import com.dji.sdk.sample.utils.DJIModuleVerificationUtil;

import java.io.File;
import java.util.ArrayList;

import dji.sdk.Camera.DJICameraSettingsDef;
import dji.sdk.Camera.DJIMedia;
import dji.sdk.Camera.DJIMediaManager;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJICameraError;
import dji.sdk.base.DJIError;

/**
 * Class for featching the media.
 */
public class FetchMediaView extends BaseThreeBtnView {

    private DJIMedia mMedia;

    public Handler messageHandler;

    public FetchMediaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Looper looper=Looper.myLooper();
        messageHandler = new MessageHandler(looper);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (DJIModuleVerificationUtil.isCameraModuleValid()) {
            if (DJIModuleVerificationUtil.isMediaManagerValid()) {
                DJISampleApplication.getProductInstance().getCamera().setCameraMode(
                        DJICameraSettingsDef.CameraMode.MediaDownload,
                        new DJIBaseComponent.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                if (null == djiError)
                                    fetchMediaList();
                            }
                        }
                );
            } else {
                mTexInfo.setText(R.string.not_support_mediadownload);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (DJIModuleVerificationUtil.isCameraModuleValid()) {
            DJISampleApplication.getProductInstance().getCamera().setCameraMode(
                    DJICameraSettingsDef.CameraMode.ShootPhoto,
                    new DJIBaseComponent.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    }
            );
        }
    }

    @Override
    protected int getBtn1TextResourceId() {
        return R.string.fetch_media_view_fetch_thumbnail;
    }

    @Override
    protected int getBtn2TextResourceId() {
        return R.string.fetch_media_view_fetch_preview;
    }

    @Override
    protected int getBtn3TextResourceId() {
        return R.string.fetch_media_view_fetch_media;
    }

    @Override
    protected int getInfoResourceId() {
        if(!DJIModuleVerificationUtil.isMediaManagerValid()){
            return R.string.not_support_mediadownload;
        }else {
            return R.string.support_mediadownload;
        }

    }

    @Override
    protected void getBtn1Method() {
        // Fetch Thumbnail Button
        if (DJIModuleVerificationUtil.isMediaManagerValid() && mMedia != null) {
            mMedia.fetchThumbnail(new DJIMediaManager.CameraDownloadListener<Bitmap>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onRateUpdate(long l, long l1, long l2) {

                }

                @Override
                public void onProgress(long l, long l1) {

                }

                @Override
                public void onSuccess(Bitmap bitmap) {

                }

                @Override
                public void onFailure(DJICameraError djiCameraError) {

                }
            });
        }
    }

    @Override
    protected void getBtn2Method() {
        // Fetch Preview Button
        if (DJIModuleVerificationUtil.isMediaManagerValid() && mMedia != null) {
            mMedia.fetchPreviewImage(new DJIMediaManager.CameraDownloadListener<Bitmap>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onRateUpdate(long l, long l1, long l2) {

                }

                @Override
                public void onProgress(long l, long l1) {

                }

                @Override
                public void onSuccess(Bitmap bitmap) {

                }

                @Override
                public void onFailure(DJICameraError djiCameraError) {

                }
            });
        }
    }

    @Override
    protected void getBtn3Method() {
        // Fetch Media Data Button
        if (DJIModuleVerificationUtil.isCameraModuleValid() && mMedia != null) {
            File destDir = new File(Environment.getExternalStorageDirectory().
                                    getPath() + "/Dji_Sdk_Test/");
            mMedia.fetchMediaData(destDir, new DJIMediaManager.CameraDownloadListener<Boolean>() {
                String str;
                @Override
                public void onStart() {

                }

                @Override
                public void onRateUpdate(long total, long current, long persize) {
                    Message message = Message.obtain();
                    str = "Downlaoding file 1 progress: ";
                    message.obj = str;
                    messageHandler.sendMessage(message);
                }

                @Override
                public void onProgress(long l, long l1) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onFailure(DJICameraError djiCameraError) {

                }
            });
        }
    }

    // Initialize the view with getting a media file.
    private void fetchMediaList() {
        if (DJIModuleVerificationUtil.isMediaManagerValid()) {
            DJISampleApplication.getProductInstance().getCamera().getMediaManager().fetchMediaList(
                    new DJIMediaManager.CameraDownloadListener<ArrayList<DJIMedia>>() {
                        String str;
                        @Override
                        public void onStart() {
                            Message message = Message.obtain();
                            message.obj = "Start fetch media list";
                            messageHandler.sendMessage(message);
                        }

                        @Override
                        public void onRateUpdate(long total, long current, long persize) {
                            Message message = Message.obtain();
                            str = "in progress";
                            message.obj = str;
                            messageHandler.sendMessage(message);
                        }

                        @Override
                        public void onProgress(long l, long l1) {

                        }

                        @Override
                        public void onSuccess(ArrayList<DJIMedia> djiMedias) {
                            if (null != djiMedias) {
                                if (!djiMedias.isEmpty()){
                                    mMedia = djiMedias.get(0);
                                    Message message = Message.obtain();
                                    str = "Total Media files:"+djiMedias.size() +"\n"+ "Media 1: "+djiMedias.get(0).getFileName();
                                    message.obj = str;
                                    messageHandler.sendMessage(message);
                                }else {
                                    Message message = Message.obtain();
                                    str = "No Media in SD Card";
                                    message.obj = str;
                                    messageHandler.sendMessage(message);
                                }

                            }
                        }

                        @Override
                        public void onFailure(DJICameraError djiCameraError) {
                           Message message = Message.obtain();
                            message.obj = djiCameraError.getDescription();
                            messageHandler.sendMessage(message);
                        }
                    }
            );
        }
    }


    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            mTexInfo.setText((String)msg.obj);
        }
    }
}