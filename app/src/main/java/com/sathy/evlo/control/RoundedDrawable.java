package com.sathy.evlo.control;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by sathy on 28/6/15.
 */
public class RoundedDrawable extends ShapeDrawable {

    private final Paint paint;
    private final String text;

    public RoundedDrawable(String text, int background) {
        super(new OvalShape());

        // text and color
        this.text = text.toUpperCase();

        // text paint settings
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

        // set background paint
        getPaint().setColor(background);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = r.width();
        int height = r.height();
        int fontSize = (Math.min(width, height) / 2);
        if(text.length() == 1)
            fontSize += 2;
        paint.setTextSize(fontSize);
        canvas.drawText(text, width / 2, height / 2 - ((paint.descent() + paint.ascent()) / 2), paint);
        canvas.restoreToCount(count);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return -1;
    }
}
