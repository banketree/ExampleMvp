package me.jessyan.autosize;

import android.content.Context;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 通过声明 {@link ContentProvider} 自动完成初始化
 */
public class InitProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Context application = getContext().getApplicationContext();
        if (application == null) {
            application = AutoSizeUtils.getApplicationByReflect();
        }
        AutoSizeConfig.getInstance()
                .setLog(true)
                .init((Application) application)
                .setUseDeviceSize(false);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
