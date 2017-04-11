package com.appzoneltd.lastmile.customer.deprecated;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import com.appzoneltd.lastmile.customer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wafaa on 8/1/2016.
 */
@Deprecated
public class RadarDrawable extends Drawable implements Animatable {

    private static final int DURATION = 3500;
    private int mMinRadius;
    private int mMaxRadius;

    private AnimatorSet mAnimator;
    private boolean mAnimating;
    private final List<Circle> mCircles = new ArrayList<>();

    Context context;

    public RadarDrawable(Context context) {
        this.context = context;
    }

    public void setMinRadius(int startRadius) {
        mMinRadius = startRadius;
        if (mAnimating) {
            initAnimator();
        }
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        mMaxRadius = Math.min(bounds.width(), bounds.height()) >> 1;
        if (mAnimating) {
            initAnimator();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        if (isRunning()) {
            for (Circle circle : mCircles) {
                circle.draw(canvas, rect);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }


    @Override
    public void start() {
        mAnimating = true;
        if (!isRunning()) {
            initAnimator();
        }
    }

    @Override
    public void stop() {
        mAnimating = false;
        if (isRunning()) {
            mAnimator.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimator != null && mAnimator.isRunning();
    }

    private void initAnimator() {
        if (isRunning()) {
            mAnimator.cancel();
            mCircles.clear();
        }

        if (mMaxRadius <= mMinRadius) {
            return;
        }

        mAnimator = new AnimatorSet();
        mAnimator.playTogether(createCircleAnimation(0), createCircleAnimation(5000),
                createCircleAnimation(1000), createCircleAnimation(1500));

        if (mAnimating) {
            mAnimator.start();
        }

    }

    private Animator createCircleAnimation(int startDelay) {
        Circle circle = new Circle();
        mCircles.add(circle);
        return circle.getAnimator(startDelay, mMinRadius, mMaxRadius);
    }

    private class Circle implements ValueAnimator.AnimatorUpdateListener {

        private static final String RADIUS = "radius";
        private static final String ALPHA_STROKE = "alphaStroke";
        private static final String ALPHA_FILL = "alphaFill";

        private final Paint mStrokePaint;
        //        private final Paint mFillPaint;
        private int mRadius;
        private ValueAnimator mAnimator;

        public Circle() {
            mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setColor(context.getResources().getColor(R.color.belize_hole));
            mStrokePaint.setStrokeWidth(3);
        }

        public Animator getAnimator(int startDelay, int minRadius, int maxRadius) {
            mAnimator = new ValueAnimator();
            mAnimator.setValues(
                    PropertyValuesHolder.ofInt(RADIUS, minRadius, maxRadius),
                    PropertyValuesHolder.ofInt(ALPHA_STROKE, 200, 0),
                    PropertyValuesHolder.ofInt(ALPHA_FILL, 30, 0)
            );
            mAnimator.setStartDelay(startDelay);
            mAnimator.setDuration(DURATION);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.addUpdateListener(this);
            return mAnimator;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mRadius = (int) animation.getAnimatedValue(RADIUS);
            mStrokePaint.setAlpha((int) animation.getAnimatedValue(ALPHA_STROKE));
            invalidateSelf();
        }

        public void draw(Canvas canvas, Rect rect) {
            canvas.drawCircle(rect.centerX(), rect.centerY(), mRadius, mStrokePaint);
        }
    }
}
