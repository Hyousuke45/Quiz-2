package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DiceView extends View {

    private Paint paint;
    private int number = 1;

    public DiceView(Context context) {
        super(context);
        init();
    }

    public DiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float size = 130;

        // Draw white dice
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(
                12,
                0,
                6,
                Color.GRAY
        );

        canvas.drawRoundRect(
                centerX - size / 2,
                centerY - size / 2,
                centerX + size / 2,
                centerY + size / 2,
                20,
                20,
                paint
        );

        paint.clearShadowLayer();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawRoundRect(
                centerX - size / 2,
                centerY - size / 2,
                centerX + size / 2,
                centerY + size / 2,
                20,
                20,
                paint
        );

        paint.setStyle(Paint.Style.FILL);

        // Dot positions
        float left = centerX - 40;
        float right = centerX + 40;
        float top = centerY - 40;
        float middle = centerY;
        float bottom = centerY + 40;

        // Draw dots depending on number
        switch (number) {

            case 1:
                drawDot(canvas, centerX, middle);
                break;

            case 2:
                drawDot(canvas, left, top);
                drawDot(canvas, right, bottom);
                break;

            case 3:
                drawDot(canvas, left, top);
                drawDot(canvas, centerX, middle);
                drawDot(canvas, right, bottom);
                break;

            case 4:
                drawDot(canvas, left, top);
                drawDot(canvas, right, top);
                drawDot(canvas, left, bottom);
                drawDot(canvas, right, bottom);
                break;

            case 5:
                drawDot(canvas, left, top);
                drawDot(canvas, right, top);
                drawDot(canvas, centerX, middle);
                drawDot(canvas, left, bottom);
                drawDot(canvas, right, bottom);
                break;

            case 6:
                drawDot(canvas, left, top);
                drawDot(canvas, right, top);
                drawDot(canvas, left, middle);
                drawDot(canvas, right, middle);
                drawDot(canvas, left, bottom);
                drawDot(canvas, right, bottom);
                break;
        }
    }

    private void drawDot(Canvas canvas, float x, float y) {

        paint.setColor(Color.BLACK);

        canvas.drawCircle(
                x,
                y,
                10,
                paint
        );
    }

    public void setNumber(int number) {

        this.number = number;

        invalidate();
    }
}