package com.letv.android.bezierutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tlinux on 17-1-13.
 */

public class BezierView extends View {

    private static final int POINT_NONE = 0;
    private static final int POINT_ONE = 1;
    private static final int POINT_TWO = 2;
    private static final int POINT_THREE = 3;
    private static final int POINT_FORE = 4;

    private Path mPath = new Path();

    private int currentPoint = 0;

    private PointF pointF1 = new PointF();
    private PointF pointF2 = new PointF();
    private PointF pointF3 = new PointF();
    private PointF pointF4 = new PointF();

    private Paint paint  = new Paint();
    private int current = POINT_NONE;

    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init(){
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    private boolean onMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:


                RectF rectF = new RectF(x-50,y-50,x+50,y+50);


                onMove = false;
                if (current == POINT_NONE) {
                    current = POINT_ONE;
                    pointF1.set(x,y);
                    currentPoint = 1;
                    invalidate();
                    return true;
                } else if (current == POINT_ONE) {
                    current = POINT_TWO;
                    currentPoint= 2;
                    pointF2.set(x,y);
                    invalidate();
                    return true;

                } else if (current == POINT_TWO) {

                    current = POINT_THREE;
                    currentPoint= 3;
                    pointF3.set(x,y);
                    invalidate();
                    return true;
                } else if (current == POINT_THREE) {
                    if (rectF.contains(pointF1.x,pointF1.y)){
                        pointF1.set(x,y);
                        currentPoint = 1;
                    }else if (rectF.contains(pointF2.x,pointF2.y)){
                        pointF2.set(x,y);
                        currentPoint = 2;
                    }else if (rectF.contains(pointF3.x,pointF3.y)){
                        pointF3.set(x,y);
                        currentPoint = 3;
                    }else{
                        current = POINT_FORE;
                        currentPoint= 4;
                        pointF4.set(x,y);
                        invalidate();
                    }
                    return true;
                }else if (current == POINT_FORE){
                    if (rectF.contains(pointF1.x,pointF1.y)){
                        pointF1.set(x,y);
                        currentPoint = 1;
                        return true;
                    }else if (rectF.contains(pointF2.x,pointF2.y)){
                        pointF2.set(x,y);
                        currentPoint = 2;
                        return true;
                    }else if (rectF.contains(pointF3.x,pointF3.y)){
                        pointF3.set(x,y);
                        currentPoint = 3;
                        return true;
                    }else if (rectF.contains(pointF4.x,pointF4.y)){
                        pointF4.set(x,y);
                        currentPoint = 4;
                        return true;
                    }
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                onMove = true;

                if (currentPoint == POINT_NONE) {
                    return false;
                } else if (currentPoint == POINT_ONE) {
                    if (current<=POINT_TWO){
                        current = POINT_TWO;
                        pointF2.set(x,y);
                    }else {
                        pointF1.set(x,y);
                    }

                    invalidate();
                    return true;
                } else if (currentPoint == POINT_TWO) {
                    if (current<=POINT_THREE){
                        current = POINT_THREE;
                        pointF3.set(x,y);
                    }else{
                        pointF2.set(x,y);
                    }
                    invalidate();
                    return true;
                } else if (currentPoint == POINT_THREE) {
                    pointF3.set(x,y);
                    invalidate();
                    return true;
                }else if (currentPoint == POINT_FORE){
                    pointF4.set(x,y);
                    invalidate();
                }
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentPoint = current;
                if (current == POINT_NONE) {

                } else if (current == POINT_ONE) {
                    if (onMove){
                    }
                } else if (current == POINT_TWO) {
                    if (onMove) {

                    }
                } else if (current == POINT_THREE) {

                }else if (current == POINT_FORE){

                }
                onMove = false;
                return false;
        }

        return super.onTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width =  getWidth();
        String text = "pStart　:"+pointF1.toString()
                + "  pEnd:"+pointF2.toString()+"  pC1:"+pointF3.toString()
                + "  pC2: "+pointF4.toString();

        char[] cText = text.toCharArray();
        paint.setTextSize(30);

        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        int ascent = metrics.ascent;
        int textHeight =metrics.bottom - metrics.top;

        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);

        int line = 0;
        int lineWidth = 0;
        int lineStart = 0;

        for (int i = 0; i < widths.length; i++) {
            lineWidth += widths[i];
            if (lineWidth> width-10){
                canvas.drawText(cText,lineStart,i-lineStart,10,10-ascent+textHeight*line,paint);
                line++;
                lineWidth = 0;
                lineStart=i;
            }

        }
        canvas.drawText(cText,lineStart,cText.length-lineStart,10-ascent,10-ascent+textHeight*line,paint);

        if (current == POINT_NONE) {
            super.onDraw(canvas);
            return;
        }

        mPath.reset();

        if (current == POINT_NONE) {

        } else if (current == POINT_ONE) {
            canvas.drawCircle(pointF1.x,pointF1.y,2,paint);


        } else if (current == POINT_TWO) {
            canvas.drawCircle(pointF1.x,pointF1.y,2,paint);
            canvas.drawCircle(pointF2.x,pointF2.y,2,paint);
            canvas.drawLine(pointF1.x,pointF1.y,pointF2.x,pointF2.y,paint);



        } else if (current == POINT_THREE) {

            canvas.drawCircle(pointF1.x,pointF1.y,2,paint);
            canvas.drawCircle(pointF2.x,pointF2.y,2,paint);
            canvas.drawCircle(pointF3.x,pointF3.y,2,paint);
            canvas.drawLine(pointF1.x,pointF1.y,pointF2.x,pointF2.y,paint);
            canvas.drawLine(pointF3.x,pointF3.y,pointF1.x,pointF1.y,paint);
            canvas.drawLine(pointF3.x,pointF3.y,pointF2.x,pointF2.y,paint);

            mPath.moveTo(pointF1.x,pointF1.y);
            mPath.quadTo(pointF3.x,pointF3.y,pointF2.x,pointF2.y);
            canvas.drawPath(mPath,paint);



        }else if (current == POINT_FORE){
            canvas.drawCircle(pointF1.x,pointF1.y,2,paint);
            canvas.drawCircle(pointF2.x,pointF2.y,2,paint);
            canvas.drawCircle(pointF3.x,pointF3.y,2,paint);
            canvas.drawCircle(pointF4.x,pointF4.y,2,paint);
            canvas.drawLine(pointF1.x,pointF1.y,pointF2.x,pointF2.y,paint);
            canvas.drawLine(pointF3.x,pointF3.y,pointF1.x,pointF1.y,paint);
            canvas.drawLine(pointF4.x,pointF4.y,pointF2.x,pointF2.y,paint);

            mPath.moveTo(pointF1.x,pointF1.y);
            mPath.cubicTo(pointF3.x,pointF3.y,pointF4.x,pointF4.y,pointF2.x,pointF2.y);
            canvas.drawPath(mPath,paint);

        }

    }


    public void clear(){
        current = POINT_NONE;
        currentPoint = 0;
        invalidate();

    }
}
