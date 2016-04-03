package uneg.software.sebu.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.orm.SugarContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sebu.R;
import uneg.software.sebu.interfaces.ApiRestInterface;
import uneg.software.sebu.models.Alarma;
import uneg.software.sebu.models.Area;
import uneg.software.sebu.models.BaseResponse;
import uneg.software.sebu.models.bd.Telefono;
import uneg.software.sebu.utils.Constants;
import uneg.software.sebu.utils.GPSManager;
import uneg.software.sebu.utils.PictureSaver;
import uneg.software.sebu.utils.UserSessionManager;
import uneg.software.sebu.utils.Utils;

/**
 * Created by Jhonny on 2/4/2016.
 *//** Takes a single photo on service start. */
public class PanicService extends Service implements LocationListener {

    private Handler customHandler;
    private UserSessionManager session;
    private GPSManager gpsManager;
    private boolean delayed=false;
    private Alarma alarma;

    @Override
    public void onCreate() {
        super.onCreate();
        session=new UserSessionManager(this);
        gpsManager=new GPSManager(this);
        customHandler = new Handler();
        if(gpsManager.obtainLastKnowLocation()!=null)
        {
            onLocationChanged(gpsManager.getLocation());
            enviarMensajes();
            enviarAlerta();
        }else
        {
            delayed=true;
        }
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
                takePhoto(PanicService.this);
                customHandler.postDelayed(this, Utils.getInterval(session.getIntervalo()));
            }
        }
    };

    private static void showMessage(String message) {
        Log.i("Camera", message);
    }

    @Override public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onLocationChanged(Location location) {
        gpsManager.setLocation(location);
        showMessage("Ubicacion=" + location.toString());
        if(delayed)
        {
            delayed=false;
            enviarMensajes();
            enviarAlerta();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void enviarAlerta()
    {
        UserSessionManager session=new UserSessionManager(this);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.url)).build();
        restAdapter.create(ApiRestInterface.class)
                .sendAlarma(
                        session.getIdUser(),
                        "Se ha presionado el botón de emergencias en el Bus "+session.getUsername(),
                        String.valueOf(gpsManager.getLocation().getLatitude()),
                        String.valueOf(gpsManager.getLocation().getLongitude()),
                        Constants.AREA_TRANSPORTE,
                        null,
                        3,
                        mAlarmaCallBack);
    }

    private Callback<Alarma> mAlarmaCallBack=new Callback<Alarma>() {
        @Override
        public void success(Alarma r, Response response) {
            Log.i("AlertarActivity", "Alerta enviada " + r.getId());
            alarma=r;
            if(customHandler!=null)
            {
                customHandler.postDelayed(updateTimerThread, 0);
                Toast.makeText(PanicService.this, "ALARMA ENVIADA", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("AlertarActivity", "error enviando alerta " + error.getMessage());
            Toast.makeText(PanicService.this,"PROBLEMA AL ENVIAR ALARMA",Toast.LENGTH_SHORT).show();
        }
    };

    private void uploadPhoto(File file) {
        showMessage("Subiendo imagen");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.url)).build();
        restAdapter.create(ApiRestInterface.class).subirFotoAlarma(
                Utils.fileToBase64(file),
                alarma.getId(),
                String.valueOf(gpsManager.getLocation().getLatitude()),
                String.valueOf(gpsManager.getLocation().getLongitude()),
                        mUploadCallBack);
    }

    private Callback<BaseResponse> mUploadCallBack=new Callback<BaseResponse>() {
        @Override
        public void success(final BaseResponse baseResponse, Response response) {
            if(baseResponse.isStatus())
            {
                Log.i("subi imagen",baseResponse.getMessage());
                Toast.makeText(PanicService.this,"SUBIDA "+baseResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("subiendo imagen error ", error.getMessage());
            Toast.makeText(PanicService.this,"PROBLEMA AL SUBIR IMAGEN",Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressWarnings("deprecation")
    private void takePhoto(final Context context) {
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
                            uploadPhoto(PictureSaver.savePicture(data, Constants.FOLDER_IMAGES));
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


    private void enviarMensajes(){

        try {
            SugarContext.init(this);
            List<Telefono> telefonos = Telefono.listAll(Telefono.class);
            SugarContext.terminate();
            for (Telefono telefono:telefonos) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(telefono.getTelefono(), null,
                        session.getMensaje()+" en las coordenadas: "+
                                gpsManager.getLocation().getLatitude()+", "+
                                gpsManager.getLocation().getLongitude(), null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            showMessage("error sms "+e.getMessage());
        }
    }
}