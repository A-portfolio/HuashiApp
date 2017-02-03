package net.muxi.huashiapp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 16/4/18.
 */
public class LoginEditText extends EditText {

    private Drawable mDrawable;

    public LoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawable = getResources().getDrawable(R.drawable.ic_add_black_24dp);
        mDrawable.setBounds(0,0,mDrawable.getIntrinsicWidth(),mDrawable.getIntrinsicHeight());

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                displayDrawable(s.length() > 0);
            }
        });
    }

    private void displayDrawable(boolean b) {
        if (b){
            setCompoundDrawables(null,null,mDrawable,null);
        }else{
            setCompoundDrawables(null,null,null,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (getCompoundDrawables()[2] != null){
                boolean isTouch = event.getX() > (getWidth() - getTotalPaddingRight())
                        && event.getX() < (getWidth() - getPaddingRight());
                if (isTouch){
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
