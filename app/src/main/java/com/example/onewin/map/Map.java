package com.example.onewin.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.onewin.Params;
import com.example.onewin.card.Card;
import com.example.onewin.card.Effects;

import java.util.ArrayList;

public final class Map {

    static private double cos, sin;
    static private boolean updateX, updateY;
    static private double height, width;

    static private volatile ArrayList<ArrayList<MapStruct>> mapStructs;
    public static final ArrayList<MapStruct> drawingMapStructs = new ArrayList<>();
    private static volatile ArrayList<Bullet> bullets;
    private static volatile ArrayList<Enemy> enemies;
    private static volatile ArrayList<Card> cards;

    public static void create() {

        Bitmap bitmap = Params.resources.get(0);

        double screenX = -bitmap.getWidth() / 2f + Params.screenX / 2f;
        double screenY = -bitmap.getHeight() / 2f + Params.screenX / 2f;

        cards = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        mapStructs = new ArrayList<>();
        ArrayList<MapStruct> room = new ArrayList<>();

        room.add(new MapStruct(screenX, screenY, bitmap, 1));

        height += bitmap.getHeight();
        width += bitmap.getWidth();

        bitmap = Params.resources.get(3);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 8, bitmap.getHeight() / 8, true);

        cards.add(new Card(bitmap, 1000, 1000));
        cards.add(new Card(bitmap, 0, -100));

        mapStructs.add(room);

        room = new ArrayList<>();

        room.add(new MapStruct(screenX, screenY, bitmap, 1));

        room.add(new MapStruct(-500, -600, Params.resources.get(2), 2));

        mapStructs.add(room);

        drawingMapStructs.addAll(mapStructs.get(0));
    }

    public static void addEnemy(){
        enemies.add(new Enemy());
    }

    public static void draw(Canvas canvas) {

        for (int i = 0; i < drawingMapStructs.size(); i++) {
            drawingMapStructs.get(i).draw(canvas);
        }

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).draw(canvas);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(canvas);
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(canvas);
        }

        Effects.draw(canvas);

        update();
    }

    private static void update() {

        if (updateX) {
            for (int i = 0; i < drawingMapStructs.size(); i++) {
                drawingMapStructs.get(i).updateX(cos);
            }

            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).updateX(cos);
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).updateX(cos);
            }

            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).updateX(cos);
            }

            Effects.updateX(cos);
        }
        if (updateY) {
            for (int i = 0; i < drawingMapStructs.size(); i++) {
                drawingMapStructs.get(i).updateY(sin);
            }

            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).updateY(sin);
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).updateY(sin);
            }

            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).updateY(sin);
            }

            Effects.updateY(sin);
        }
    }

    public static void setPM(boolean xP, boolean yP, boolean xM, boolean yM) {

        if (xP) {
            if (yP) {
                cos = Math.sqrt(2) / 2;
                sin = cos;
            } else if (yM) {
                cos = Math.sqrt(2) / 2;
                sin = -cos;
            } else {
                cos = 1;
                sin = 0;
            }
        } else if (xM) {
            if (yP) {
                cos = -Math.sqrt(2) / 2;
                sin = -cos;
            } else if (yM) {
                cos = -Math.sqrt(2) / 2;
                sin = cos;
            } else {
                cos = -1;
                sin = 0;
            }
        } else if (yP) {
            cos = 0;
            sin = 1;
        } else if (yM) {
            cos = 0;
            sin = -1;
        } else {
            cos = 0;
            sin = 0;
        }
    }

    public static double getX() {
        try {
            return drawingMapStructs.get(0).getScreenX();
        } catch (Exception ignored){
            return -1;
        }
    }

    public static double getY() {
        try {
            return drawingMapStructs.get(0).getScreenY();
        } catch (Exception ignored){
            return -1;
        }
    }

    public static double getHeight() {
        return height;
    }

    public static double getWidth() {
        return width;
    }

    public static void setUpdateX(boolean updateX) {
        Map.updateX = updateX;
    }

    public static void setUpdateY(boolean updateY) {
        Map.updateY = updateY;
    }

    public static void setLastScreenX() {

        for (int i = 0; i < drawingMapStructs.size(); i++) {
            drawingMapStructs.get(i).setLastScreenX();
        }

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).getLastScreenX();
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setLastScreenX();
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setLastScreenX();
        }

        Effects.setLastScreenX();
    }

    public static void setLastScreenY() {

        for (int i = 0; i < drawingMapStructs.size(); i++) {
            drawingMapStructs.get(i).setLastScreenY();
        }

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).getLastScreenY();
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setLastScreenY();
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setLastScreenY();
        }

        Effects.setLastScreenY();
    }

    public static ArrayList<MapStruct> getDrawingMapStructs() {
        return drawingMapStructs;
    }

    public static void setSetLsX(boolean setLsX) {

        for (int i = 0; i < drawingMapStructs.size(); i++) {
            drawingMapStructs.get(i).setSetLsX(setLsX);
        }

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSetLsX(setLsX);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setSetLsX(setLsX);
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setSetLsX(setLsX);
        }

        Effects.setSetLsX(setLsX);
    }

    public static void setSetLsY(boolean setLsY) {

        for (int i = 0; i < drawingMapStructs.size(); i++) {
            drawingMapStructs.get(i).setSetLsY(setLsY);
        }

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSetLsY(setLsY);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setSetLsY(setLsY);
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setSetLsY(setLsY);
        }

        Effects.setSetLsY(setLsY);
    }

    public static void changeDrawingRoom(int room) {

        drawingMapStructs.clear();
        drawingMapStructs.addAll(mapStructs.get(room));
    }

    public static void removeCard(int i){
        cards.remove(i);
    }

    public static double getScreenX(int i) {
        return drawingMapStructs.get(i).getScreenX();
    }

    public static double getScreenY(int i) {
        return drawingMapStructs.get(i).getScreenY();
    }

    public static ArrayList<Card> getCardsArrayList(){
        return cards;
    }

    public static double getCardScreenX(int i) {
        return cards.get(i).getScreenX();
    }

    public static double getCardScreenY(int i) {
        return cards.get(i).getScreenY();
    }
}