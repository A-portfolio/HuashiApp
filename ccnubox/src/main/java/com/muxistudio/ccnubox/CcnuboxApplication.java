package com.muxistudio.ccnubox;

import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.ui.main.FetchPatchHandler;

/**
 * Created by fengminchao on 18/3/19
 */

public class CcnuboxApplication extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            ApplicationLike tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true);

            new FetchPatchHandler().fetchPatchWithInterval(3);
        }
    }
}
