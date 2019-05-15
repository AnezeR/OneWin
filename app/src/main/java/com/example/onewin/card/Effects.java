package com.example.onewin.card;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.onewin.Params;

import java.util.ArrayList;

public final class Effects {

    private static final String TAG = "MyHuntTag";

    private static volatile ArrayList<Effect> effects;
    private static volatile ArrayList<Effect> calledEffects;
    private static volatile ArrayList<Effect> calledEnemyEffects;

    public static void create(){
        effects = new ArrayList<>();
        calledEffects = new ArrayList<>();
        calledEnemyEffects = new ArrayList<>();

        Bitmap sprite = Params.resources.get(1);
        sprite = Bitmap.createScaledBitmap(sprite, sprite.getWidth() / 4, sprite.getHeight() / 4, true);
        int w = sprite.getWidth() / 2, h = sprite.getHeight() / 2;

        sprite = Bitmap.createScaledBitmap(sprite, w , h , true);

        effects.add(new EffectOne(sprite));
    }

    public static void draw(Canvas canvas){
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).draw(canvas);
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).draw(canvas);
        }
    }

    public static boolean check(){
        for (int i = 0; i < calledEffects.size(); i++) {
            Effect effect = calledEffects.get(i);

            if (System.currentTimeMillis() - effect.timeRunning > effect.timeStarted){
                calledEffects.remove(i);
                i--;
                continue;
            }

            if (effect.called){
                if (effect.check()) {
                    calledEffects.remove(i);
                    return true;
                }
            }
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            Effect effect = calledEnemyEffects.get(i);

            if (System.currentTimeMillis() - effect.timeRunning > effect.timeStarted){
                calledEnemyEffects.remove(i);
                i--;
                continue;
            }

            if (effect.called){
                if (effect.check()) {
                    calledEnemyEffects.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static StringBuilder getInfo(){

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < calledEffects.size(); i++) {
            out.append(calledEffects.get(i).id).append('\n').append(calledEffects.get(i).timeStarted)
                    .append('\n')
                    .append(calledEffects.get(i).screenX).append('\n').append(calledEffects.get(i).screenY)
                    .append('\n');
        }

        return out;
    }

    public static void callEffect(int id){
        calledEffects.add(effects.get(id).getEffect());
        calledEffects.get(calledEffects.size() - 1).call(-1, -999999, -999999);
    }

    private static ArrayList<Long> getAllTimeStarted(){
        ArrayList<Long> out = new ArrayList<>();
        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            out.add(calledEnemyEffects.get(i).timeStarted);
        }

        return out;
    }

    public static void callEffect(int id, long time, double screenX, double screenY){
        if (!getAllTimeStarted().contains(time)) {
            calledEnemyEffects.add(effects.get(id).getEffect());
            calledEnemyEffects.get(calledEffects.size() - 1).call(time, screenX, screenY);

            Log.d(TAG, "Вызван эффект " + calledEnemyEffects.size());
        }
    }

    public static void updateX(double cos){
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).updateX(cos);
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).updateX(cos);
        }
    }

    public static void updateY(double sin){
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).updateY(sin);
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).updateY(sin);
        }
    }

    public static void setLastScreenX(){
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).setLastScreenX();
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).setLastScreenX();
        }
    }

    public static void setLastScreenY(){
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).setLastScreenY();
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).setLastScreenY();
        }
    }

    public static void setSetLsX(boolean setLsX) {
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).setSetLsX(setLsX);
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).setSetLsX(setLsX);
        }
    }

    public static void setSetLsY(boolean setLsY) {
        for (int i = 0; i < calledEffects.size(); i++) {
            calledEffects.get(i).setSetLsY(setLsY);
        }

        for (int i = 0; i < calledEnemyEffects.size(); i++) {
            calledEnemyEffects.get(i).setSetLsY(setLsY);
        }
    }
}