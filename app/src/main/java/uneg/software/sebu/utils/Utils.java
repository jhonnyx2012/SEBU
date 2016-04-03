package uneg.software.sebu.utils;

import android.net.Uri;
import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Jhonny on 13/3/2016.
 */
public class Utils {
    public static String fileToBase64(File file) {
        String strFile = null;
        InputStream is;
        try {
            is = new FileInputStream(file);
            byte[] data = IOUtils.toByteArray(is);//Convert any file, image or video into byte array
            is.close();
            strFile = Base64.encodeToString(data, Base64.NO_WRAP);//Convert byte array into string
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strFile;
    }

    public static File getFileByN(int i, ArrayList<Uri> mMedia) {
        Uri uri=mMedia.get(i);
        if (!uri.toString().contains("content://")) {
           return new File(uri.toString());
        }
        return new File(uri.getPath());
    }
}
