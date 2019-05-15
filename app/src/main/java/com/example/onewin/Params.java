package com.example.onewin;

import android.graphics.Bitmap;

import java.util.ArrayList;

final public class Params {

    public static int speed = 10;
    public static int R = 30;
    static double joystickR = 100, JR = 20;
    static int startFloor = 1;
    public static int bulletR = 10;
    public static int bulletSpeed = 20;
    public static int enemyR = 30;
    public static int enemySpeed = 30;
    public static float screenX, screenY;

    public static ArrayList<Bitmap> resources;
    static ArrayList<Bitmap> effectsArrayList;
    public static ArrayList<ArrayList<Bitmap>> cardsArrayList;

    static void create(){
        resources = new ArrayList<>();
        effectsArrayList = new ArrayList<>();
        cardsArrayList = new ArrayList<>();
    }
}

final class BulletJoystickParams{
    private static volatile double midX, midY, x, y;
    private static volatile boolean enabled = false;

    public static void setMidXY(double midX, double midY){
        BulletJoystickParams.midX = midX;
        BulletJoystickParams.midY = midY;
    }

    public static void setXY(double x, double y){
        BulletJoystickParams.x = x;
        BulletJoystickParams.y = y;
    }

    public static Double getMidX() {
        return midX;
    }

    public static Double getMidY() {
        return midY;
    }

    public static Double getX() {
        return x;
    }

    public static Double getY() {
        return y;
    }

    public static Boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(Boolean enabled) {
        BulletJoystickParams.enabled = enabled;
    }
}