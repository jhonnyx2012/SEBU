package uneg.software.sebu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.CheckBox;

import uneg.software.sebu.R;


/**
 * Created by Jhonny on 02-08-2015.
 */
public class GPSManager {

    private static final int REQUEST_CODE = 0;
    private static final String TAG = GPSManager.class.getSimpleName();
    private final LocationListener listener;
    private final Context context;
    protected LocationManager locationManager;
    Location location = null;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double lat;
    double lon;



    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private Activity activity;

    public GPSManager(Activity activity)
    {
        this.context=activity;
        this.activity=activity;
        this.listener=(LocationListener)activity;
        location=obtainLastKnowLocation();
        getMyLocation(activity);

    }

    public GPSManager(Context activity)
    {
        this.context=activity;
        this.listener=(LocationListener)activity;
        location=obtainLastKnowLocation();
        getMyLocationSimple();
    }



    public void getMyLocationSimple()
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
           return;
        }
        if (isNetworkEnabled)
        {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
            location=obtainLastKnowLocation();
        }
        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
            location=obtainLastKnowLocation();
        }
    }


    public void getMyLocation(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled)
        {
            showWarning("Ubicacion desactivada","El Sistema de Emergencias de Buses UNEG necesita de la ubicación para informar al centro de control con mas detalle");
        }
        if (isNetworkEnabled)
        {
             locationManager.requestLocationUpdates(
                     LocationManager.NETWORK_PROVIDER,
                     MIN_TIME_BW_UPDATES,
                     MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
            location=obtainLastKnowLocation();
         }
         if (isGPSEnabled) {
             locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
             location=obtainLastKnowLocation();
         }
    }

    public Location obtainLastKnowLocation()
    {
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
        }

        return location;
    }

    public void showWarning(String title, String message)
    {
        AlertDialog mAlertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Si",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        encenderGPS();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //activity.finish();
                //getMyLocation();
            }
        });
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
    }

    public void showWarningGPS(String title, String message)
    {
        AlertDialog mAlertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        encenderGPS();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
    }


    private void encenderGPS()
    {
        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(settingsIntent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
           getMyLocation(context);
    }

    public void stopListening() {
         locationManager.removeUpdates(listener);
    }

    public void setLocation(Location location) {
        this.location=location;
    }

    public Location getLocation() {
        return location;
    }
}
