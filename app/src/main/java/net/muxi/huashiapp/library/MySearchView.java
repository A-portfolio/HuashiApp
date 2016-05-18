package net.muxi.huashiapp.library;

import android.content.Context;
import android.util.AttributeSet;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by ybao on 16/5/16.
 */
public class MySearchView extends MaterialSearchView{

    public MySearchView(Context context) {
        this(context,null);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSuggestionIcon();
    }

    @Override
    public void setSuggestions(String[] suggestions) {
        super.setSuggestions(suggestions);

    }

//    @Override
//    public void setAdapter(ListAdapter adapter) {
//        super.setAdapter(adapter);
//    }

    @Override
    public void onFilterComplete(int count) {
        showSuggestions();
    }
}
