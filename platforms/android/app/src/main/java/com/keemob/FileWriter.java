package com.keemob;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import org.apache.cordova.file.Filesystem;
import org.apache.cordova.file.LocalFilesystemURL;
import org.apache.cordova.file.NoModificationAllowedException;

import java.io.OutputStream;
import java.nio.charset.Charset;

public class FileWriter {

    public static int writeToUri(Context context, Filesystem filesystem, LocalFilesystemURL inputURL, String data, int offset, boolean isBinary) throws NoModificationAllowedException {
        Uri uri = filesystem.toNativeUri(inputURL);
        OutputStream outputStream = null;

        try {
            outputStream = context.getContentResolver().openOutputStream(uri);

            byte[] rawData;
            if (isBinary) {
                rawData = Base64.decode(data, Base64.DEFAULT);
            } else {
                rawData = data.getBytes(Charset.defaultCharset());
            }

            outputStream.write(rawData);
            outputStream.flush();
            outputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(intent);

            return rawData.length;

        } catch (Exception e) {
            NoModificationAllowedException exception = new NoModificationAllowedException("Couldn't write to file given its content URI");
            exception.initCause(e);
            throw exception;
        }
    }
}
