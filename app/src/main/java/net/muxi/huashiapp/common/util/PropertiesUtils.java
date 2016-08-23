package net.muxi.huashiapp.common.util;

import android.content.res.AssetManager;

import net.muxi.huashiapp.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ybao on 16/8/22.
 */
public class PropertiesUtils {

    public static Properties loadProperties(String file) {
        Properties properties = new Properties();
        try {
            AssetManager assetManager = App.sContext.getAssets();
            InputStream is = assetManager.open(file);
            properties.load(is);
            Logger.d("properties has loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveProperties(String fileName, Properties properties) {
        try {
            File file = new File(fileName);
            if (!file.exists()){
                file.createNewFile();
            }
            Logger.d(file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
//            properties.store(App.sContext.getAssets().openFd(fileName).createOutputStream(), null);
            properties.store(fos,"");
            Logger.d("properties has saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
