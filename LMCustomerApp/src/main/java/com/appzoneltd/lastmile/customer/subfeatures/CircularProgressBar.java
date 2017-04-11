package com.appzoneltd.lastmile.customer.subfeatures;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Simple single android view component that can be used to showing a round progress bar.
 * It can be customized with size, stroke size, colors and text etc.
 * Progress change will be animated.
 * Created by Kristoffer, http://kmdev.se
 */
public class CircularProgressBar extends View {

    private int mViewWidth;
    private int mViewHeight;

    private final String ETA = "ETA";
    private final float startAngle = -90;      // Always start from top (default is: "3 o'clock on a watch.")
    private float sweepAngle = 0;              // How long to sweep from startAngle
    private float maxSweepAngle = 360;         // Max degrees to sweep = full circle
    private int strokeWidth = 3;              // Width of outline
    private int animationDuration = 400;       // Animation duration for progress change
    private int maxProgress = 100;             // Max progress to use
    private boolean drawText = true;           // Set to true if progress text should be drawn
    private boolean roundedCorners = true;     // Set to true if rounded corners should be applied to outline ends
    private int progressColor = Color.BLACK;   // Outline color
    private int textColor = Color.BLACK;       // Progress text color

    private final Paint mPaint;                 // Allocate paint outside onDraw to avoid unnecessary object creation
    private long remainingTime = 17;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasurments();
        drawOutlineArc(canvas);
        if (drawText) {
            drawText(canvas);
        }
    }

    private void initMeasurments() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    private void drawOutlineArc(Canvas canvas) {

        final int diameter = Math.min(mViewWidth, mViewHeight) - (strokeWidth * 2);

        final RectF outerOval = new RectF(strokeWidth, strokeWidth, diameter, diameter);

        mPaint.setColor(progressColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(roundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setColor(Color.WHITE);
        canvas.drawArc(outerOval, sweepAngle, sweepAngle, false, mPaint);

        mPaint.setColor(Color.BLACK);
        canvas.drawArc(outerOval, startAngle, sweepAngle, false, mPaint);

    }

    private void drawText(Canvas canvas) {
        mPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 5f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);

        // Center text
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2)) ;

        canvas.drawText(ETA + " " + remainingTime , xPos, yPos, mPaint);
    }

    private float calcSweepAngleFromProgress(int progress) {
        return (maxSweepAngle / maxProgress) * progress;
    }


    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    public void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofFloat(sweepAngle,
                calcSweepAngleFromProgress(progress));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                sweepAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    public void useRoundedCorners(boolean roundedCorners) {
        this.roundedCorners = roundedCorners;
        invalidate();
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
