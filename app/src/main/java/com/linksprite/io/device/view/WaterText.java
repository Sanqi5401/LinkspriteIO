package com.linksprite.io.device.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linksprite.io.R;

/**
 * Created by Administrator on 2016/11/8.
 */
public class WaterText extends TextView {
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;
    private int colorStart;
    private int colorMiddle;
    private int colorEnd;


    public WaterText(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorStart = context.getResources().getColor(R.color.color_start);
        colorMiddle = context.getResources().getColor(R.color.color_middle);
        colorEnd = context.getResources().getColor(R.color.color_end);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0,
                        new int[]{colorStart, colorMiddle, colorEnd},
                        new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            mTranslate += mViewWidth / 10;
            if (mTranslate > 2 * mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(50);
        }
    }
}
