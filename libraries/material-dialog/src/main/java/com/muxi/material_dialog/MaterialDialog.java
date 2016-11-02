package com.muxi.material_dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ybao on 16/6/24.
 */
public class MaterialDialog extends Dialog {

    private TextView mTvTitle;
    private TextView mTvContent;
    private RelativeLayout mLayoutContent;
    private Button mBtnNegative;
    private Button mBtnPositive;
    private Context mContext;


    public MaterialDialog(Context context) {
        this(context,0);
    }

    public MaterialDialog(Context context, int themeResId) {
        super(context, R.style.DialogStyle);
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_dialog, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mLayoutContent = (RelativeLayout) view.findViewById(R.id.layout_content);
        mBtnNegative = (Button) view.findViewById(R.id.btn_negative);
        mBtnPositive = (Button) view.findViewById(R.id.btn_positive);
        this.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public MaterialDialog setTitle(String title){
        mTvTitle.setText(title);
        return this;
    }

    public MaterialDialog setTitleSize(int size){
        mTvTitle.setTextSize(size);
        return this;
    }

    public MaterialDialog setContent(String content){
        mLayoutContent.setVisibility(View.VISIBLE);
        if (mTvContent.getVisibility() == View.GONE){
            mTvContent.setVisibility(View.VISIBLE);
        }
        mTvContent.setText(content);
        return this;
    }

    public MaterialDialog setView(View view){
        mLayoutContent.setVisibility(View.VISIBLE);
        if (mTvContent.getVisibility() != View.GONE){
            mTvContent.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mLayoutContent.addView(view,params);
        return this;
    }

    public MaterialDialog setPositiveButton(String positiveButton, View.OnClickListener listener){
        mBtnPositive.setText(positiveButton);
        mBtnPositive.setOnClickListener(listener);
        return this;
    }

    public MaterialDialog setPositiveButtonVisible(boolean b){
        if (b){
            mBtnPositive.setVisibility(View.VISIBLE);
        }else {
            mBtnPositive.setVisibility(View.GONE);
        }
        return this;
    }

    public MaterialDialog setNegativeButtonVisible(boolean b){
        if (b){
            mBtnNegative.setVisibility(View.VISIBLE);
        }else {
            mBtnNegative.setVisibility(View.GONE);
        }
        return this;
    }

    public MaterialDialog setNegativeButton(String negativeButton, View.OnClickListener listener){
        mBtnNegative.setText(negativeButton);
        mBtnNegative.setOnClickListener(listener);
        return this;
    }

    public MaterialDialog setTitleColor(int color){
        mTvTitle.setTextColor(color);
        return this;
    }

    public MaterialDialog setContentColor(int color){
        mTvContent.setTextColor(color);
        return this;
    }

    public MaterialDialog setButtonColor(int color){
        Log.d("dialogcolor","dialogcolor");
        mBtnPositive.setTextColor(color);
        mBtnNegative.setTextColor(color);
        return this;
    }

    public MaterialDialog setPositiveButtonColor(int color){
        mBtnPositive.setTextColor(color);
        return this;
    }

    public MaterialDialog setNegativeButtonColor(int color){
        mBtnNegative.setTextColor(color);
        return this;
    }
    public MaterialDialog setBackground(Drawable drawable){
        this.setBackground(drawable);
        return this;
    }


    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void show() {
        super.show();
    }

    private class Builder{

        private TextView mTitle;
        private TextView mContent;

        private Builder(){

        }

    }

}
