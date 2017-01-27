package com.blogspot.gm4s1.tmgm4s.utility;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by GloryMaker on 12/18/2016.
 */

public class FileOp {

    static class Internal {
        public static File createFile(Context context, String filename) {
            return new File(context.getFilesDir(), filename);
        }

        public static void writeToFile(Context context, String filename, String content) {
            FileOutputStream outputStream;

            try {
                outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static File getTempFile(Context context, String url) {
            File file = null;
            try {
                String fileName = Uri.parse(url).getLastPathSegment();
                file = File.createTempFile(fileName, null, context.getCacheDir());
            } catch (IOException e) {
                // Error while creating file
            }
            return file;
        }
    }

    static class External {
        /* Checks if external storage is available for read and write */
        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public static boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        public File getPublicAlbumStorageDir(String albumName) {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
            if (!file.mkdirs()) {
                //Log.e(LOG_TAG, "Directory not created");
            }
            return file;
        }

        public File getPrivateAlbumStorageDir(Context context, String albumName) {
            // Get the directory for the app's private pictures directory.
            File file = new File(context.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), albumName);
            if (!file.mkdirs()) {
                //Log.e(LOG_TAG, "Directory not created");
            }
            return file;
        }
    }
}
