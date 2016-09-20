package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import butterknife.BindView;

/**
 * Created by ybao on 16/9/20.
 */
public class UpdateView extends LinearLayout {

    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.checkbox)
    AppCompatCheckBox mCheckbox;

    public UpdateView(Context context) {
        this(context, null);
    }

    public UpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_remind_update, this, false);
        this.addView(view);
    }

    public boolean isRemindClose(){
        return mCheckbox.isChecked();
    }

    public void setContentText(String s){
        mTvContent.setText(s);
    }
}
