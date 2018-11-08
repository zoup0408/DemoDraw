package com.zoup.android.demo.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by zoup on 2018/11/8
 * E-Mail：2479008771@qq.com
 */
public class DrawBoard extends View {
    /**
     * 定义画板的长和宽
     */
    private int boardWidth = 480;
    private int boardHeight = 800;

    /**
     * move事件响应的最小距离
     */
    private int touchSlop;

    /**
     * 定义画笔
     */
    private Paint drawPaint;
    private Paint eraserPaint;

    /**
     * 画布
     */
    private Canvas canvas;

    /**
     * 记录触摸点的X,Y坐标
     */
    private float xPosition;
    private float yPosition;

    /**
     * 画布的bitmap对象
     */
    private Bitmap dstBitmap;

    /**
     * 画笔路径
     */
    private Path path;

    /**
     * 定义画笔模式
     */
    public static final int DRAW_MODE = 1;
    public static final int ERASER_MODE = 2;
    private int paintMode;


    public DrawBoard(Context context) {
        super(context);
        init(context);
    }

    public DrawBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledPagingTouchSlop();

        initPaint();
        dstBitmap = Bitmap.createBitmap(boardWidth, boardHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(dstBitmap);
        path = new Path();

        setPaintMode(DRAW_MODE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (dstBitmap != null) {
            canvas.drawBitmap(dstBitmap, 0, 0, drawPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                drawMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawUp();
                invalidate();
                break;
        }
        return true;
    }

    private void drawStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        xPosition = x;
        yPosition = y;
        if (paintMode == DRAW_MODE) {
            canvas.drawPath(path, drawPaint);
        }
        if (paintMode == ERASER_MODE) {
            canvas.drawPath(path, eraserPaint);
        }
    }

    private void drawMove(float x, float y) {
        float dx = Math.abs(x - xPosition);
        float dy = Math.abs(y - yPosition);
        if (dx >= touchSlop || dy >= touchSlop) {
            path.quadTo(xPosition, yPosition, (x + xPosition) / 2, (y + yPosition) / 2);
            xPosition = x;
            yPosition = y;
            if (paintMode == DRAW_MODE) {
                canvas.drawPath(path, drawPaint);
            }
            if (paintMode == ERASER_MODE) {
                canvas.drawPath(path, eraserPaint);
            }
        }
    }

    private void drawUp() {
        path.lineTo(xPosition, yPosition);
        if (paintMode == DRAW_MODE) {
            canvas.drawPath(path, drawPaint);
        }
        if (paintMode == ERASER_MODE) {
            canvas.drawPath(path, eraserPaint);
        }
    }

    private void initPaint() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);

        eraserPaint = new Paint();
        eraserPaint.setAlpha(0);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        eraserPaint.setAntiAlias(true);
        eraserPaint.setDither(true);
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeJoin(Paint.Join.ROUND);
        eraserPaint.setStrokeWidth(30);
    }

    public void setPaintMode(int paintMode) {
        this.paintMode = paintMode;
    }
}
