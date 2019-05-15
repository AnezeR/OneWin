package com.example.onewin.map;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.onewin.Params;

public class Bullet {

    private static final int R = Params.bulletR;
    private Paint paint;
    private double sin;
    private double cos;
    private int speed = Params.bulletSpeed;
    float screenX;
    float screenY;
    private boolean updates = true;

    private double lastScreenX, lastScreenY;
    private boolean isSetLsX = true, isSetLsY = true;

    Bullet(float x, float y, float scX, float scY){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.screenX = scX / 2f;
        this.screenY = scY / 2f;

        double c = Math.sqrt((screenX - x) * (screenX - x) + (y - screenY) * (y - screenY));

        sin = (y - screenY) / c;
        cos = (x - screenX) / c;

        this.screenX += Params.R * cos;
        this.screenY += Params.R * sin;
    }

    void draw(Canvas canvas){
        try {
            canvas.drawCircle(screenX, screenY, R, paint);
        } catch (Exception ignored){
        }
    }

    void updateX(double allCos){
        if (updates) {
            screenX += cos * speed + allCos * Params.speed;
        }
    }

    void updateY(double allSin){
        if (updates) {
            screenY += sin * speed + allSin * Params.speed;
        }
    }

    void setUpdates(boolean updates) {
        this.updates = updates;
    }

    void setLastScreenX() {
        screenX = (float) lastScreenX;
    }

    void setLastScreenY() {
        screenY = (float) lastScreenY;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public double getWidth() {
        return Params.bulletR;
    }

    public double getHeight() {
        return Params.bulletR;
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
