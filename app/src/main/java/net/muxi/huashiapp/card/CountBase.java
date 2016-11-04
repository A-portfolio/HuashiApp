package net.muxi.huashiapp.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by december on 16/11/2.
 */

public class CountBase extends FragmentActivity {
    protected String[] mMonths = new String[]{
            "11.1","11.2","11.3","11.4","11.5","11.6","11.7"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

}
