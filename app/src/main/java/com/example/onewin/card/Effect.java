package com.example.onewin.card;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.onewin.Params;

abstract class Effect {

    long timeRunning, timeStarted;
    int type;
    int id;
    int minHp;
    float screenX, screenY;
    private float lastScreenX, lastScreenY;
    float screenSizeX = Params.screenX, screenSizeY = Params.screenY;
    private boolean isSetLsX = true, isSetLsY = true;
    boolean called = false;
    Bitmap sprite;

    abstract boolean check();

    abstract void delete();

    abstract Effect getEffect();


    void draw(Canvas canvas){
        canvas.drawBitmap(sprite, screenX, screenY, null);
    }

    public int getType(){
        return type;
    }

    public float getScreenX(){
        return screenX;
    }

    public float getScreenY() {
        return screenY;
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

    void call(long time, double screenX, double screenY){
        called = true;
        timeStarted = time;
    }
}
