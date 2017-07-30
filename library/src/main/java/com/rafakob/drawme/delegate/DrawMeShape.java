package com.rafakob.drawme.delegate;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rafakob.drawme.R;
import com.rafakob.drawme.util.Coloring;

import java.util.Arrays;


/**
 * Base delegate that holds all shape attributes and accessors.
 */
public class DrawMeShape implements DrawMe {
    /* Widget */
    protected final View mView;
    /* Background color */
    protected int backColor;
    protected int backColorPressed;
    protected int backColorDisabled;
    /* Stroke */
    protected int stroke;
    protected int strokeColor;
    protected int strokeColorPressed;
    protected int strokeColorDisabled;
    /* Corner radius */
    protected int radius;
    protected int radiusBottomLeft;
    protected int radiusBottomRight;
    protected int radiusTopLeft;
    protected int radiusTopRight;
    /* Mask */
    protected float maskBrightnessThreshold;
    protected int maskColorPressed;
    protected int maskColorPressedInverse;
    protected int maskColorDisabled;
    /* Params */
    protected boolean rippleEffect;
    protected boolean rippleUseControlHighlight;
    protected boolean statePressed;
    protected boolean stateDisabled;
    protected boolean shapeEqualWidthHeight;
    protected boolean shapeRadiusHalfHeight;
    /* 2D Shadow */
    protected int shadowX;
    protected int shadowY;
    protected int shadowColor;

    public DrawMeShape(Context context, View view) {
        this(context, view, null);
    }

    public DrawMeShape(Context context, View view, AttributeSet attrs) {
        this(context, view, attrs, 0);
    }

    public DrawMeShape(Context context, View view, AttributeSet attrs, @AttrRes int defStyleAttr) {
        this.mView = view;
        obtainAttributes(context, attrs, defStyleAttr);
    }

    public void obtainAttributes(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawMe, defStyleAttr, 0);
        obtainShapeAttributes(typedArray);
        typedArray.recycle();
    }

    protected void obtainShapeAttributes(TypedArray a) {
        rippleEffect = a.getBoolean(R.styleable.DrawMe_dm_rippleEffect, true);
        rippleUseControlHighlight = a.getBoolean(R.styleable.DrawMe_dm_rippleUseControlHighlight, true);
        statePressed = a.getBoolean(R.styleable.DrawMe_dm_statePressed, true);
        stateDisabled = a.getBoolean(R.styleable.DrawMe_dm_stateDisabled, true);
        shapeEqualWidthHeight = a.getBoolean(R.styleable.DrawMe_dm_shapeEqualWidthHeight, false);
        shapeRadiusHalfHeight = a.getBoolean(R.styleable.DrawMe_dm_shapeRadiusHalfHeight, false);

        maskBrightnessThreshold = a.getFloat(R.styleable.DrawMe_dm_maskBrightnessThreshold, 0);
        maskColorPressed = a.getColor(R.styleable.DrawMe_dm_maskColorPressed, Color.parseColor("#1F000000"));
        maskColorPressedInverse = a.getColor(R.styleable.DrawMe_dm_maskColorPressedInverse, Color.parseColor("#1DFFFFFF"));
        maskColorDisabled = a.getColor(R.styleable.DrawMe_dm_maskColorDisabled, Color.parseColor("#6DFFFFFF"));

        stroke = a.getDimensionPixelSize(R.styleable.DrawMe_dm_stroke, 0);
        radius = a.getDimensionPixelSize(R.styleable.DrawMe_dm_radius, 0);
        radiusBottomLeft = a.getDimensionPixelSize(R.styleable.DrawMe_dm_radiusBottomLeft, -1);
        radiusBottomRight = a.getDimensionPixelSize(R.styleable.DrawMe_dm_radiusBottomRight, -1);
        radiusTopLeft = a.getDimensionPixelSize(R.styleable.DrawMe_dm_radiusTopLeft, -1);
        radiusTopRight = a.getDimensionPixelSize(R.styleable.DrawMe_dm_radiusTopRight, -1);

        backColor = a.getColor(R.styleable.DrawMe_dm_backColor, Color.TRANSPARENT);
        backColorPressed = a.getColor(R.styleable.DrawMe_dm_backColorPressed, defaultPressedColor(backColor));
        backColorDisabled = a.getColor(R.styleable.DrawMe_dm_backColorDisabled, defaultDisabledColor(backColor));

        strokeColor = a.getColor(R.styleable.DrawMe_dm_strokeColor, Color.GRAY);
        strokeColorPressed = a.getColor(R.styleable.DrawMe_dm_strokeColorPressed, defaultPressedColor(strokeColor));
        strokeColorDisabled = a.getColor(R.styleable.DrawMe_dm_strokeColorDisabled, defaultDisabledColor(strokeColor));

        shadowColor = a.getColor(R.styleable.DrawMe_dm_shadowColor, Color.GRAY);
        shadowX = a.getDimensionPixelSize(R.styleable.DrawMe_dm_shadowX, 0);
        shadowY = a.getDimensionPixelSize(R.styleable.DrawMe_dm_shadowY, 0);

    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (shapeRadiusHalfHeight) {
            radius = mView.getHeight() / 2;
        }
        updateLayout();
    }

    @Override
    public int[] onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] size = new int[2];
        if (shapeEqualWidthHeight && mView.getWidth() > 0 && mView.getHeight() > 0) {
            int max = Math.max(mView.getWidth(), mView.getHeight());
            int measureSpec = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.EXACTLY);
            size[0] = measureSpec;
            size[1] = measureSpec;
            return size;
        }
        size[0] = widthMeasureSpec;
        size[1] = heightMeasureSpec;
        return size;
    }

    @Override
    public void updateLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackground(createBackground());
        } else {
            mView.setBackgroundDrawable(createBackground());
        }
    }

    private Drawable createBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rippleEffect) {
            return createRippleDrawable();
        } else {
            return createStateListDrawable();
        }
    }

    /**
     * Creates background shape - setups background, stroke and radius.
     *
     * @param backgroundColor Background color.
     * @param strokeColor     Stroke color.
     * @return Shape drawable.
     */
    private Drawable createShape(int backgroundColor, int strokeColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(backgroundColor);

        final float[] radiusArray = new float[8];
        Arrays.fill(radiusArray, radius);

        if (radiusTopLeft >= 0) {
            radiusArray[0] = radiusTopLeft;
            radiusArray[1] = radiusTopLeft;
        }

        if (radiusTopRight >= 0) {
            radiusArray[2] = radiusTopRight;
            radiusArray[3] = radiusTopRight;
        }

        if (radiusBottomRight >= 0) {
            radiusArray[4] = radiusBottomRight;
            radiusArray[5] = radiusBottomRight;
        }

        if (radiusBottomLeft >= 0) {
            radiusArray[6] = radiusBottomLeft;
            radiusArray[7] = radiusBottomLeft;
        }
        shape.setCornerRadii(radiusArray);
        shape.setStroke(stroke, strokeColor);
        return shape;
    }

    private Drawable createShadowShape()
    {
        if (shadowX != 0 || shadowY !=0)
        {
            GradientDrawable shadow = (GradientDrawable) createShape(shadowColor,  0);
            shadow.setStroke(0 , 0);
            return shadow;
        } else {
            return null;
        }
    }

    private StateListDrawable createStateListDrawable() {
        StateListDrawable states = new StateListDrawable();

        Drawable shadow = createShadowShape();

        if (shadow == null)
        {
            if (stateDisabled)
            {
                states.addState(new int[]{-android.R.attr.state_enabled}, createShape(backColorDisabled, strokeColorDisabled));
            }
            if (statePressed)
            {
                states.addState(new int[]{android.R.attr.state_pressed}, createShape(backColorPressed, strokeColorPressed));
            }
            states.addState(new int[]{}, createShape(backColor, strokeColor));
            return states;
        } else {

            if (stateDisabled)
            {
                states.addState(new int[]{-android.R.attr.state_enabled},
                        addShadow(createShape(backColorDisabled, strokeColorDisabled) , shadow));
            }
            if (statePressed)
            {
                states.addState(new int[]{android.R.attr.state_pressed},
                        addShadow(createShape(backColorPressed, strokeColorPressed) , shadow));
            }
            states.addState(new int[]{},
                    addShadow(createShape(backColor, strokeColor) , shadow));
            return states;
        }
    }

    private LayerDrawable addShadow(Drawable background, Drawable shadow)
    {
        Drawable[] layers = new Drawable[2];
        layers[0] = shadow;
        layers[1] = background;

        int l = shadowX > 0 ? shadowX : 0;
        int t = shadowY > 0 ? shadowY : 0;
        int r = shadowX < 0 ? -shadowX : 0;
        int b = shadowY < 0 ? -shadowY : 0;

        LayerDrawable layerList = new LayerDrawable(layers);
        layerList.setLayerInset(0, l, t, r, b);
        layerList.setLayerInset(1, r , b , l , t);
        return layerList;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable createRippleDrawable() {
        if (!statePressed) {
            return getRippleContentDrawable();
        } else if (backColorPressed == Color.TRANSPARENT && strokeColorPressed != Color.TRANSPARENT) {
            // highlight only stroke
            return new RippleDrawable(ColorStateList.valueOf(strokeColorPressed), getRippleContentDrawable(), createShape(Color.TRANSPARENT, Color.WHITE));

        } else {
            return new RippleDrawable(ColorStateList.valueOf(backColorPressed), getRippleContentDrawable(), createShape(Color.WHITE, Color.WHITE));
        }
    }

    /**
     * Creates content drawable for a RippleDrawable.
     *
     * @return
     */
    private Drawable getRippleContentDrawable() {
        if (!stateDisabled) {
            return createShape(backColor, strokeColor);
        } else {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{-android.R.attr.state_enabled}, createShape(backColorDisabled, strokeColorDisabled));
            states.addState(new int[]{}, createShape(backColor, strokeColor));
            return states;
        }
    }

    /**
     * Calculates default color value for pressed color.
     * On PreL mixes normal state color with a "shadow mask", on L uses theme attribute colorControlHighlight.
     *
     * @param normalColor
     * @return
     */
    @ColorInt
    private int defaultPressedColor(int normalColor) {
        if (maskBrightnessThreshold > 0 && Coloring.getColorBrightness(normalColor) < maskBrightnessThreshold) {
            return Coloring.mix(maskColorPressedInverse, normalColor);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rippleUseControlHighlight) {
            return Coloring.getThemeColor(mView.getContext(), R.attr.colorControlHighlight);
        } else {
            return Coloring.mix(maskColorPressed, normalColor);
        }
    }

    /**
     * Calculates default color value for disabled color.
     * Mixes state color with a "lighter mask".
     *
     * @param normalColor
     * @return
     */
    @ColorInt
    private int defaultDisabledColor(int normalColor) {
        return Coloring.mix(maskColorDisabled, normalColor);
    }


    public int getBackColor()
    {
        return backColor;
    }

    public void setBackColor(int backColor)
    {
        this.backColor = backColor;
    }

    public int getBackColorPressed()
    {
        return backColorPressed;
    }

    public void setBackColorPressed(int backColorPressed)
    {
        this.backColorPressed = backColorPressed;
    }

    public int getBackColorDisabled()
    {
        return backColorDisabled;
    }

    public void setBackColorDisabled(int backColorDisabled)
    {
        this.backColorDisabled = backColorDisabled;
    }

    public int getStroke()
    {
        return stroke;
    }

    public void setStroke(int stroke)
    {
        this.stroke = stroke;
    }

    public int getStrokeColor()
    {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor)
    {
        this.strokeColor = strokeColor;
    }

    public int getStrokeColorPressed()
    {
        return strokeColorPressed;
    }

    public void setStrokeColorPressed(int strokeColorPressed)
    {
        this.strokeColorPressed = strokeColorPressed;
    }

    public int getStrokeColorDisabled()
    {
        return strokeColorDisabled;
    }

    public void setStrokeColorDisabled(int strokeColorDisabled)
    {
        this.strokeColorDisabled = strokeColorDisabled;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public int getRadiusBottomLeft()
    {
        return radiusBottomLeft;
    }

    public void setRadiusBottomLeft(int radiusBottomLeft)
    {
        this.radiusBottomLeft = radiusBottomLeft;
    }

    public int getRadiusBottomRight()
    {
        return radiusBottomRight;
    }

    public void setRadiusBottomRight(int radiusBottomRight)
    {
        this.radiusBottomRight = radiusBottomRight;
    }

    public int getRadiusTopLeft()
    {
        return radiusTopLeft;
    }

    public void setRadiusTopLeft(int radiusTopLeft)
    {
        this.radiusTopLeft = radiusTopLeft;
    }

    public int getRadiusTopRight()
    {
        return radiusTopRight;
    }

    public void setRadiusTopRight(int radiusTopRight)
    {
        this.radiusTopRight = radiusTopRight;
    }

    public float getMaskBrightnessThreshold()
    {
        return maskBrightnessThreshold;
    }

    public void setMaskBrightnessThreshold(float maskBrightnessThreshold)
    {
        this.maskBrightnessThreshold = maskBrightnessThreshold;
    }

    public int getMaskColorPressed()
    {
        return maskColorPressed;
    }

    public void setMaskColorPressed(int maskColorPressed)
    {
        this.maskColorPressed = maskColorPressed;
    }

    public int getMaskColorPressedInverse()
    {
        return maskColorPressedInverse;
    }

    public void setMaskColorPressedInverse(int maskColorPressedInverse)
    {
        this.maskColorPressedInverse = maskColorPressedInverse;
    }

    public int getMaskColorDisabled()
    {
        return maskColorDisabled;
    }

    public void setMaskColorDisabled(int maskColorDisabled)
    {
        this.maskColorDisabled = maskColorDisabled;
    }

    public boolean isRippleEffect()
    {
        return rippleEffect;
    }

    public void setRippleEffect(boolean rippleEffect)
    {
        this.rippleEffect = rippleEffect;
    }

    public boolean isRippleUseControlHighlight()
    {
        return rippleUseControlHighlight;
    }

    public void setRippleUseControlHighlight(boolean rippleUseControlHighlight)
    {
        this.rippleUseControlHighlight = rippleUseControlHighlight;
    }

    public boolean isStatePressed()
    {
        return statePressed;
    }

    public void setStatePressed(boolean statePressed)
    {
        this.statePressed = statePressed;
    }

    public boolean isStateDisabled()
    {
        return stateDisabled;
    }

    public void setStateDisabled(boolean stateDisabled)
    {
        this.stateDisabled = stateDisabled;
    }

    public boolean isShapeEqualWidthHeight()
    {
        return shapeEqualWidthHeight;
    }

    public void setShapeEqualWidthHeight(boolean shapeEqualWidthHeight)
    {
        this.shapeEqualWidthHeight = shapeEqualWidthHeight;
    }

    public boolean isShapeRadiusHalfHeight()
    {
        return shapeRadiusHalfHeight;
    }

    public void setShapeRadiusHalfHeight(boolean shapeRadiusHalfHeight)
    {
        this.shapeRadiusHalfHeight = shapeRadiusHalfHeight;
    }
}