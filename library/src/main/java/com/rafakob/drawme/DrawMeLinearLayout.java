package com.rafakob.drawme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.rafakob.drawme.delegate.DrawMe;
import com.rafakob.drawme.delegate.DrawMeShape;

public class DrawMeLinearLayout extends LinearLayout {
    private final DrawMeShape drawMe;

    public DrawMeLinearLayout(Context context) {
        super(context);
        drawMe = new DrawMeShape(context, this, null);
    }

    public DrawMeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawMe = new DrawMeShape(context, this, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] size = drawMe.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(size[0], size[1]);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        drawMe.onLayout(changed, left, top, right, bottom);
    }

    public int getBackColor()
    {
        return this.drawMe.getBackColor();
    }

    public void setBackColor(int backColor)
    {
        this.drawMe.setBackColor(backColor);
    }

    public int getBackColorPressed()
    {
        return this.drawMe.getBackColorPressed();
    }

    public void setBackColorPressed(int backColorPressed)
    {
        this.drawMe.setBackColorPressed(backColorPressed);
    }

    public int getBackColorDisabled()
    {
        return this.drawMe.getBackColorDisabled();
    }

    public void setBackColorDisabled(int backColorDisabled)
    {
        this.drawMe.setBackColorDisabled(backColorDisabled);
    }

    public int getStroke()
    {
        return this.drawMe.getStroke();
    }

    public void setStroke(int stroke)
    {
        this.drawMe.setStroke(stroke);
    }

    public int getStrokeColor()
    {
        return this.drawMe.getStrokeColor();
    }

    public void setStrokeColor(int strokeColor)
    {
        this.drawMe.setStrokeColor(strokeColor);
    }

    public int getStrokeColorPressed()
    {
        return this.drawMe.getStrokeColorPressed();
    }

    public void setStrokeColorPressed(int strokeColorPressed)
    {
        this.drawMe.setBackColorPressed(strokeColorPressed);
    }

    public int getStrokeColorDisabled()
    {
        return this.drawMe.getStrokeColorDisabled();
    }

    public void setStrokeColorDisabled(int strokeColorDisabled)
    {
        this.drawMe.setStrokeColorDisabled(strokeColorDisabled);
    }

    public int getRadius()
    {
        return this.drawMe.getRadius();
    }

    public void setRadius(int radius)
    {
        this.drawMe.setRadius(radius);
    }

    public int getRadiusBottomLeft()
    {
        return this.drawMe.getRadiusBottomLeft();
    }

    public void setRadiusBottomLeft(int radiusBottomLeft)
    {
        this.drawMe.setRadiusBottomLeft(radiusBottomLeft);
    }

    public int getRadiusBottomRight()
    {
        return this.drawMe.getRadiusBottomRight();
    }

    public void setRadiusBottomRight(int radiusBottomRight)
    {
        this.drawMe.setRadiusBottomRight(radiusBottomRight);
    }

    public int getRadiusTopLeft()
    {
        return this.drawMe.getRadiusTopLeft();
    }

    public void setRadiusTopLeft(int radiusTopLeft)
    {
        this.drawMe.setRadiusTopLeft(radiusTopLeft);
    }

    public int getRadiusTopRight()
    {
        return this.drawMe.getRadiusTopRight();
    }

    public void setRadiusTopRight(int radiusTopRight)
    {
        this.drawMe.setRadiusTopRight(radiusTopRight);
    }

    public float getMaskBrightnessThreshold()
    {
        return this.drawMe.getMaskBrightnessThreshold();
    }

    public void setMaskBrightnessThreshold(float maskBrightnessThreshold)
    {
        this.drawMe.setMaskBrightnessThreshold(maskBrightnessThreshold);
    }

    public int getMaskColorPressed()
    {
        return this.drawMe.getMaskColorPressed();
    }

    public void setMaskColorPressed(int maskColorPressed)
    {
        this.drawMe.setMaskColorPressed(maskColorPressed);
    }

    public int getMaskColorPressedInverse()
    {
        return this.drawMe.getMaskColorPressedInverse();
    }

    public void setMaskColorPressedInverse(int maskColorPressedInverse)
    {
        this.drawMe.setMaskColorPressedInverse(maskColorPressedInverse);
    }

    public int getMaskColorDisabled()
    {
        return this.drawMe.getMaskColorDisabled();
    }

    public void setMaskColorDisabled(int maskColorDisabled)
    {
        this.drawMe.setMaskColorDisabled(maskColorDisabled);
    }

    public boolean isRippleEffect()
    {
        return this.drawMe.isRippleEffect();
    }

    public void setRippleEffect(boolean rippleEffect)
    {
        this.drawMe.setRippleEffect(rippleEffect);
    }

    public boolean isRippleUseControlHighlight()
    {
        return this.drawMe.isRippleUseControlHighlight();
    }

    public void setRippleUseControlHighlight(boolean rippleUseControlHighlight)
    {
        this.drawMe.setRippleUseControlHighlight(rippleUseControlHighlight);
    }

    public boolean isStatePressed()
    {
        return this.drawMe.isStatePressed();
    }

    public void setStatePressed(boolean statePressed)
    {
        this.drawMe.setStatePressed(statePressed);
    }

    public boolean isStateDisabled()
    {
        return this.drawMe.isStateDisabled();
    }

    public void setStateDisabled(boolean stateDisabled)
    {
        this.drawMe.setStateDisabled(stateDisabled);
    }

    public boolean isShapeEqualWidthHeight()
    {
        return this.drawMe.isShapeEqualWidthHeight();
    }

    public void setShapeEqualWidthHeight(boolean shapeEqualWidthHeight)
    {
        this.drawMe.setShapeEqualWidthHeight(shapeEqualWidthHeight);
    }

    public boolean isShapeRadiusHalfHeight()
    {
        return this.drawMe.isShapeRadiusHalfHeight();
    }

    public void setShapeRadiusHalfHeight(boolean shapeRadiusHalfHeight)
    {
        this.drawMe.setShapeRadiusHalfHeight(shapeRadiusHalfHeight);
    }
}
