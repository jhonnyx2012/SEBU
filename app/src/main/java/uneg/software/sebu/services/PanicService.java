package uneg.software.sebu.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import uneg.software.sebu.utils.Constants;
import uneg.software.sebu.utils.PictureSaver;
import uneg.software.sebu.utils.UserSessionManager;
import uneg.software.sebu.utils.Utils;

/**
 * Created by Jhonny on 2/4/2016.
 *//** Takes a single photo on service start. */
public class PanicService extends Service {

    private Handler customHandler;
    private UserSessionManager session;

    @Override
    public void onCreate() {
        super.onCreate();
        session=new UserSessionManager(this);
        customHandler = new Handler();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customHandler=null;
    }

    private Runnable updateTimerThread = new Runnable()
    {
        public void run()
        {

            if(customHandler!=null)
            {
                showMessage("RUNNING");
                takePhoto(PanicService.this);
                customHandler.postDelayed(this, Utils.getInterval(session.getIntervalo()));
            }
        }
    };

    @SuppressWarnings("deprecation")
    private static void takePhoto(final Context context) {
        final SurfaceView preview = new SurfaceView(context);
        SurfaceHolder holder = preview.getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            //The preview must happen at or after this point or takePicture fails
            public void surfaceCreated(SurfaceHolder holder) {
                showMessage("Surface created");

                Camera camera = null;

                try {
                    camera = Camera.open();
                    showMessage("Opened camera");

                    try {
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        showMessage(e.getMessage());
                    }

                    camera.startPreview();
                    showMessage("Started preview");

                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            showMessage("Took picture");
                            PictureSaver.savePicture(data, Constants.FOLDER_IMAGES);
                            camera.release();
                        }
                    });
                } catch (Exception e) {
                    showMessage(e.getMessage());
                    if (camera != null)
                        camera.release();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

        WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1, 1, //Must be at least 1x1
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                //Don't know if this is a safe default
                PixelFormat.UNKNOWN);

        //Don't set the preview visibility to GONE or INVISIBLE
        wm.addView(preview, params);
    }

    private static void showMessage(String message) {
        Log.i("Camera", message);
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}