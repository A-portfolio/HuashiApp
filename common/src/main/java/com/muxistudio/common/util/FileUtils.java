package com.muxistudio.common.util;

import java.io.File;

/**
 * Created by ybao on 16/8/12.
 */
public class FileUtils {

    public static final void deleteFile(String path){
        File file = new File(path);
        if (file.exists()){
            try {
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
