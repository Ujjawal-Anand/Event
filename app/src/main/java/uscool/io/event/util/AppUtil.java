package uscool.io.event.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by andy1729 on 19/01/18.
 */

public class AppUtil {
    public static Bitmap getBitmapFromFilePath(String filepath) {
        return BitmapFactory.decodeFile(filepath);
    }
}
