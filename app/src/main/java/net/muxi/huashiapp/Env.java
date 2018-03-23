package net.muxi.huashiapp;

/**
 * Created by 2bab on 19/03/2018.
 */

public class Env {

    public static boolean isDebug() {
        return BuildConfig.DEBUG_MODE;
    }

    public static boolean isRelease() {
        // todo: if it should add a special build config param
        return !BuildConfig.DEBUG_MODE;
    }

}
