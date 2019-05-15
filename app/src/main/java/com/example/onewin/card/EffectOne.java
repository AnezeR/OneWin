package com.example.onewin.card;

import android.graphics.Bitmap;

import com.example.onewin.Params;

class EffectOne extends Effect{

    EffectOne(Bitmap sprite){
        this.sprite = sprite;
        id = 0;
    }


    @Override
    boolean check() {

        boolean right = (screenX + sprite.getWidth() <= screenSizeX / 2 - Params.R);
        boolean left = (screenX >= screenSizeX / 2 + Params.R);
        boolean bottom = (screenY + sprite.getHeight() <= screenSizeY / 2 - Params.R);
        boolean top = (screenY >= screenSizeY / 2 + Params.R);

        return !(top || bottom || right || left);
    }

    @Override
    void delete() {

    }

    @Override
    Effect getEffect() {
        EffectOne effectOne = new EffectOne(sprite);

        effectOne.call(timeStarted, screenX, screenY);

        return effectOne;
    }


    @Override
    void call(long time, double screenX, double screenY) {
        minHp = 5;

        type = 0;

        if (screenX == -999999)
            screenX = -100;
        if (screenY == -999999)
            screenY = -100;

        this.screenX = (float) screenX;
        this.screenY = (float) screenY;

        if (time == -1)
            timeStarted = System.currentTimeMillis();
        else
            timeStarted = time;

        timeRunning = 100000;
    }
}
