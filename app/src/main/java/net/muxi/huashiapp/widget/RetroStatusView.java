package net.muxi.huashiapp.widget;


import android.content.Context;
import android.util.AttributeSet;

import com.muxistudio.multistatusview.MultiStatusView;

import java.lang.reflect.Field;

/**
 * @author: kolibreath
 */

//todo 刷新做的更加人性化！
public class RetroStatusView extends MultiStatusView {

    public RetroStatusView(Context context) {
        super(context);
    }

    public RetroStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RetroStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 一般来说不需要使用 一般使用BaseActivity 中 showLoading()方法
     * loadingView 只有在show的时候才会被实例化
     */
    public void showLoading(){
        super.showLoading();
    }

    /**
     * 建议当数据加载成功的时候code == 200的时候调用
     * contentView 只有在show的时候才会被实例化
     */
    public void showContent(){
        super.showContent();
    }


    /**
     *
     * errorView 只有在show的时候才会被实例化
     */
    public void showError(){
        super.showError();
    }

    /**
     * 在没有数据的时候使用
     * emptyView 只有在show的时候才会被实例化
     */
    public void showEmptyView(){
        super.showEmpty();
    }

    /**
     *
     * NetErrorView 只有在show的时候才会被初始化
     */
    public void showNetErrorView(String reason){
        super.showNetError();
    }

}
