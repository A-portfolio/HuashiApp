package com.muxistudio.appcommon.utils;

import com.muxistudio.common.base.Global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by ybao on 16/8/6.
 */
public class DownloadUtils {

    public static boolean writeResponseBodyToDisk(ResponseBody body,String fileName) {
        try {
            File futureStudioIconFileDir = new File(Global.getApplication().getExternalCacheDir().getAbsolutePath());
            if (!futureStudioIconFileDir.exists()){
                futureStudioIconFileDir.mkdir();
            }
            File futureStudioIconFile = new File(futureStudioIconFileDir,fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isFileExists(String path){
        File file = new File(path);
      return file.exists();
    }
}
