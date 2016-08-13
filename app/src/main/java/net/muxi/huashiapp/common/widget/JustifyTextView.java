package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ybao on 16/8/13.
 */

public class JustifyTextView extends TextView {
    private int mLineY;
    private int mViewWidth;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        TextPaint paint = this.getPaint();
        paint.setColor(this.getCurrentTextColor());
        paint.drawableState = this.getDrawableState();
        this.mViewWidth = this.getMeasuredWidth();
        String text = (String)this.getText();
        this.mLineY = 0;
        this.mLineY = (int)((float)this.mLineY + this.getTextSize());
        Layout layout = this.getLayout();

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());

        for(int i = 0; i < layout.getLineCount(); ++i) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);

            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, this.getPaint());
//            if(this.needScale(line)) {
//                this.drawScaledText(canvas, lineStart, line, width);
//            } else {
//                canvas.drawText(line, 0.0F, (float)this.mLineY, paint);
//            }
//
//            this.mLineY += this.getLineHeight();

            if(i < layout.getLineCount() - 1) {
                if (needScale(line)) {
                    drawScaledText(canvas, lineStart, line, width);
                } else {
                    canvas.drawText(line, 0, mLineY, paint);
                }
            } else {
                canvas.drawText(line, 0, mLineY, paint);
            }
            mLineY += textHeight;
        }

    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0.0F;
        if(this.isFirstLineOfParagraph(lineStart, line)) {
            String d = "  ";
            canvas.drawText(d, x, (float)this.mLineY, this.getPaint());
            float i = StaticLayout.getDesiredWidth(d, this.getPaint());
            x += i;
            line = line.substring(3);
        }

        float var10 = ((float)this.mViewWidth - lineWidth) / (float)line.length() - 1.0F;

        for(int var11 = 0; var11 < line.length(); ++var11) {
            String c = String.valueOf(line.charAt(var11));
            float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
            canvas.drawText(c, x, (float)this.mLineY, this.getPaint());
            x += cw + var10;
        }

    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == 32 && line.charAt(1) == 32;
    }

    private boolean needScale(String line) {
        return line.length() == 0?false:line.charAt(line.length() - 1) != 10;
    }
}
