package smt.qrcode.zxing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import smt.qrcode.R;
import smt.qrcode.zxing.camera.CameraManager;
import smt.qrcode.zxing.decode.CaptureActivityHandler;
import smt.qrcode.zxing.decode.InactivityTimer;
import smt.qrcode.zxing.decode.MessageIDs;
import smt.qrcode.zxing.view.ViewfinderView;

/**
 * 二维码扫描界面
 *
 * @author admin
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    public static final int QR_CODE = 10001;
    public static final String QR_RESULT = "RESULT";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private ImageView btnOpen;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private CameraManager cameraManager;
    private Context mContext;
    private boolean isOpen;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnOpen = (ImageView) toolbar.findViewById(R.id.btnOpen);
        btnOpen.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = isOpen ? Camera.Parameters.FLASH_MODE_OFF : Camera.Parameters.FLASH_MODE_TORCH;
                openLight(type);
                if (isOpen) {
                    isOpen = false;
                    btnOpen.setImageResource(R.drawable.light_close);
                } else {
                    isOpen = true;
                    btnOpen.setImageResource(R.drawable.light_open);
                }
                PreferenceUtils.putBoolean(CaptureActivity.this, "isCameraOpen", isOpen);
            }
        });
    }

    /**
     * 开启闪灯光
     *
     * @param type
     */
    private void openLight(String type) {
        Camera camera = cameraManager.getCamera();
        if (null == camera) return;
        Camera.Parameters mParameters = camera.getParameters();
        mParameters.setFlashMode(type);//打开Camera.Parameters.FLASH_MODE_OFF则为关闭
        camera.setParameters(mParameters);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // CameraManager.init(getApplication());
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            // CameraManager.get().openDriver(surfaceHolder);
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        if (!TextUtils.isEmpty(obj.getText())) {
//            System.out.println("扫描信息========" + obj.getText());
            playBeepSoundAndVibrate();
            Intent intent = new Intent();
            intent.putExtra(QR_RESULT, obj.getText());
            this.setResult(QR_CODE, intent);
            this.finish();
        } else {
            reset();
        }
    }


    public void reset() {
        restartPreviewAfterDelay(0L);
    }


    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            try {
                AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(
                        R.raw.beep);
                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                fileDescriptor.close();
                this.mediaPlayer.setVolume(0.1F, 0.1F);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}