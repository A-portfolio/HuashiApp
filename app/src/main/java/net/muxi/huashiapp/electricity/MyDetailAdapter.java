package net.muxi.huashiapp.electricity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by december on 16/7/7.
 */
public class MyDetailAdapter extends FragmentPagerAdapter {


    public final int COUNT = 2;
    private String[] titles = new String[]{"照明", "空调"};
    private Context context;

    public MyDetailAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
       return ElectricityDetailFragment.newInstance(position+6);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  titles[position];
    }
}
