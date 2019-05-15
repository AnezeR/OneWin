package com.example.onewin.card;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.onewin.Params;

public class Card {

    float screenX, screenY;
    private float lastScreenX, lastScreenY;
    private boolean isSetLsX = true, isSetLsY = true;
    Bitmap sprite;

    public Card(Bitmap sprite, float screenX, float screenY){
        this.sprite = sprite;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(sprite, screenX, screenY, null);
    }

    public float getScreenX(){
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public void updateX(double cos) {
        if (isSetLsX)
            lastScreenX = screenX;
        screenX += cos * Params.speed;
    }

    public void updateY(double sin) {
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

    public void setSetLsX(boolean setLsX) {
        isSetLsX = setLsX;
    }

    public void setSetLsY(boolean setLsY) {
        isSetLsY = setLsY;
    }
}
