package net.muxi.huashiapp.widget.HorPickerPager;

import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/28
 */

public class HorPickerPagerAdapter extends PagerAdapter {

    private List<String> mStringList;
    private List<TextView> mTextViewList = new ArrayList<>();

    public HorPickerPagerAdapter(List<String> stringList) {
        mStringList = stringList;
    }

    public void swap(List<String> stringList){
        mStringList = stringList;
        mTextViewList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        View view = LayoutInflater.from(container.getContext()).inflate(R.layout)
        TextView textView = new TextView(container.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
//        textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setText(mStringList.get(position));
        container.addView(textView,params);
        mTextViewList.add(textView);
        return textView;
    }

    public View getViewAt(int position){
        return mTextViewList.get(position);
    }
}
