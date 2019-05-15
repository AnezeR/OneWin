package com.example.onewin.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.onewin.Params;

public class MapStruct {

    private static final String TAG = "MyHuntTag";

    private double screenX;
    private double screenY;
    private double lastScreenX, lastScreenY;
    private boolean isSetLsX = true, isSetLsY = true;
    private int floor;
    private Bitmap sprite;

    MapStruct(double screenX, double screenY, Bitmap bmp, int floor) {

        sprite = bmp;
        sprite.prepareToDraw();

        lastScreenX = screenX;
        lastScreenY = screenY;

        this.screenX = screenX;
        this.screenY = screenY;

        this.floor = floor;
    }


    void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, (float) screenX, (float) screenY, null);
    }

    void updateX(double cos) {

        if (isSetLsX)
            lastScreenX = screenX;
        screenX += cos * Params.speed;
    }

    void updateY(double sin) {
        if (isSetLsY)
            lastScreenY = screenY;
        screenY += sin * Params.speed;
    }

    void setLastScreenX() {
        screenX = lastScreenX;
    }

    void setLastScreenY() {
        screenY = lastScreenY;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public int getFloor() {
        return floor;
    }

    public double getWidth() {
        return sprite.getWidth();
    }

    public double getHeight() {
        return sprite.getHeight();
    }

    public double getLastScreenX() {
        return lastScreenX;
    }

    public double getLastScreenY() {
        return lastScreenY;
    }

    void setSetLsX(boolean setLsX) {
        isSetLsX = setLsX;
    }

    void setSetLsY(boolean setLsY) {
        isSetLsY = setLsY;
    }
}