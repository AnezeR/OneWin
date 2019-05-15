package com.example.onewin.map;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.onewin.Params;

import java.util.Random;

public class Enemy {

    private Paint paint;
    private int speed = Params.enemySpeed;
    private double sin;
    private double cos;
    float screenX;
    float screenY;
    private int R = Params.enemyR;
    private double lastScreenX, lastScreenY;
    private boolean isSetLsX = true, isSetLsY = true;

    public Enemy(){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Random rd = new Random();

        int m = rd.nextInt(4);

        double zeroX = Map.getX(), zeroY = Map.getY();
        double FX = Map.getX() + Map.getWidth(), FY = Map.getY() + Map.getHeight();

        float midX = Params.screenX / 2;
        float midY = Params.screenY / 2;

        switch (m) {
            case 0:
                screenX = rd.nextInt((int) FX);
                screenY = (float) (FY + 31);
                break;
            case 1:
                screenX = (float) (FX + 31);
                screenY = rd.nextInt((int) FY);
                break;
            case 2:
                screenX = rd.nextInt((int) FX);
                screenY = (float) (zeroY - 31);
                break;
            case 3:
                screenX = (float) (zeroX - 31);
                screenY = rd.nextInt((int) FY);
                break;
        }

        double c=Math.sqrt((screenX - midX)*(screenX - midX)+(screenY - midY)*(screenY - midY));

        cos = (screenX - midX) / c;
        sin = (screenY - midY) / c;
    }

    void draw(Canvas canvas){
        canvas.drawCircle(screenX, screenY, R, paint);
    }

    void updateX(double allCos){
        if (isSetLsX)
            lastScreenX = screenX;
        screenX += -cos * speed + allCos * Params.speed;
    }

    void updateY(double allSin){
        if (isSetLsY)
            lastScreenY = screenY;
        screenY += -sin * speed + allSin * Params.speed;
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
        return R;
    }

    public double getHeight() {
        return R;
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