package uneg.software.sebu.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jhonny on 2/4/2016.
 */
public class PictureSaver {

    private static final String TAG = "PictureSaver";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** null if unable to save the file */
    public static File savePicture(byte[] data, String folder_name){
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE, folder_name);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions!");
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }

    /** Create a File for saving an image or video
     *  null if unable to create the file */
    private static File getOutputMediaFile(int type, String folder_name){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folder_name);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "Unable to create directory!");
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +"VID_"+ timeStamp + ".mp4");
        } else {
            Log.d(TAG, "Unkknown media type!");
        }
        Log.d(TAG,mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }}