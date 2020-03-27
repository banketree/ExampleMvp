package com.example.base_fun.log

import android.content.Context
import android.util.Log
import com.thinkcore.kandroid.toTimeForYYMMDDHHMMSS
import com.thinkcore.storage.kandroid.getExternalAppDir
import com.thinkcore.storage.kandroid.getInterAppDir
import com.thinkcore.storage.kandroid.hasExternalStoragePermission
import timber.log.Timber
import java.io.File

class FileLoggingTree(val context: Context) : Timber.Tree() {

    /**
     * 使用Timber自定义吧log信息存储到文件中
     * 其中String CacheDiaPath = context.getCacheDir().toString();
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val filePath =
            if (context.hasExternalStoragePermission()) context.getExternalAppDir() else context.getInterAppDir()
        val file = File(filePath + "/${System.currentTimeMillis().toTimeForYYMMDDHHMMSS()}")
        Log.i("FileLoggingTree", "file path:${file.path}")
    }

//@Override
//protected void log(int priority, String tag, String message, Throwable t) {
//    if (TextUtils.isEmpty(CacheDiaPath)) {
//        return;
//    }
//    File file = new File(CacheDiaPath + "/log.txt");
//    Log.v("日志存储路径", "file.path:" + file.getAbsolutePath() + ",message:" + message);
//    FileWriter writer = null;
//    BufferedWriter bufferedWriter = null;
//
//    try {
//        writer = new FileWriter(file);
//        bufferedWriter = new BufferedWriter(writer);
//        bufferedWriter.write(message);
//        bufferedWriter.flush();
//    }catch (IOException e) {
//        Log.v("dyp", "存储文件失败");
//        e.printStackTrace();
//    }finally {
//        if (bufferedWriter != null) {
//            try {
//                bufferedWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
}