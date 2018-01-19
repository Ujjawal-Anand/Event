package uscool.io.event.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

import uscool.io.event.R;

/**
 * Created by andy1729 on 19/01/18.
 */

public class AppUtil {
    public static Bitmap getBitmapFromFilePath(String filepath) {
        return BitmapFactory.decodeFile(filepath);
    }

    public static String getURLForResource (String imageName) {

        return "file:///android_asset/image/"+imageName;
       }

}
