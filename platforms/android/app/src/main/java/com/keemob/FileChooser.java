package com.keemob;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.createChooser;

public class FileChooser extends CordovaPlugin {

    public static final int CODE_CHOOSE_FILE = 100;

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("choose".equals(action)) {
            openFileChooser();
            this.callbackContext = callbackContext;
            return true;
        }
        return false;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
                    .addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("application/octet-stream");

        cordova.startActivityForResult(this, createChooser(intent, "File Browser"), CODE_CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == CODE_CHOOSE_FILE) {
            Uri uri = intent.getData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final int takeFlags = intent.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cordova.getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            }

            callbackContext.success(asFile(uri));
            callbackContext = null;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private JSONObject asFile(Uri uri) {
        Cursor cursor = cordova.getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

        JSONObject result = new JSONObject();

        try {
            result.put("name", name);
            result.put("uri", uri.toString());
        } catch (Exception e) {
            //
        }

        cursor.close();
        return result;
    }
}
