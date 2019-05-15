package com.example.onewin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.onewin.map.Map;

import java.math.BigInteger;
import java.util.ArrayList;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder surfaceHolder;
    public GameThread gameThread;
    Bitmap map;
    ToGameThread toGameThread;
    EnemyThread enemyThread;

    private static final String TAG = "MyHuntTag";
    float screenSizeX = Params.screenX, screenSizeY = Params.screenY;
    double midX, midY;


    public GameView(Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        map = BitmapFactory.decodeResource(getResources(), R.drawable.fl);

        int mapWidth = map.getWidth(), mapHeight = map.getHeight();

        map = Bitmap.createScaledBitmap(map, mapWidth, mapHeight, true);

        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Bitmap wall = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        wall = Bitmap.createScaledBitmap(wall, wall.getWidth(), wall.getHeight(), true);

        Bitmap oneMoreWall = BitmapFactory.decodeResource(getResources(), R.drawable.fill);
        mapWidth = oneMoreWall.getWidth() / 4;
        mapHeight = oneMoreWall.getHeight() / 4;
        oneMoreWall = Bitmap.createScaledBitmap(oneMoreWall, mapWidth, mapHeight, true);

        bitmaps.add(map);
        bitmaps.add(wall);
        bitmaps.add(oneMoreWall);

        Params.resources.addAll(bitmaps);

        enemyThread = new EnemyThread();
        gameThread = new GameThread(surfaceHolder);
        toGameThread = new ToGameThread();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        double x = event.getX(), y = event.getY();
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN && x <= screenSizeX / 2) {
            midX = x;
            midY = y;

            toGameThread.setMidXY(midX, midY);
            toGameThread.setJoystickDraw(true);

            toGameThread.setUpdating(true);
            toGameThread.setUpdate(true);

        } else if (action == MotionEvent.ACTION_MOVE) {
            toGameThread.setXY(x, y);

        } else if (action == MotionEvent.ACTION_UP) {

            toGameThread.setJoystickDraw(false);
            toGameThread.setPMtoFalse();

            toGameThread.setUpdating(false);
            toGameThread.setUpdate(false);
        } else {
            toGameThread.setJoystickDraw(false);
            toGameThread.setPMtoFalse();

            toGameThread.setUpdating(false);
            toGameThread.setUpdate(false);
        }

        return true;
    }

    private class ToGameThread extends Thread {

        private volatile boolean running, updating;

        private volatile boolean xP, yP, xM, yM;
        private volatile boolean update;
        private volatile double midX, midY, x, y;


        @Override
        public void run() {

            while (running) {
                while (updating) {

                    gameThread.setUpdate(update, xP, yP, xM, yM);

                    gameThread.setJoystickParams(x, y, midX, midY);
                }
            }
        }

        void setRunning(boolean running) {
            this.running = running;
        }

        void setUpdate(boolean update) {
            this.update = update;
        }

        void setPMtoFalse() {
            this.xM = false;
            this.yM = false;
            this.xP = false;
            this.yP = false;
        }

        void setUpdating(boolean updating) {
            this.updating = updating;
        }

        void setMidXY(double midX, double midY) {
            this.midX = midX;
            this.midY = midY;
        }

        void setXY(double x, double y) {
            this.x = x;
            this.y = y;

            setPMtoFalse();

            double ex = x - midX, ey = y - midY;

            double c = Math.hypot(ex, ey);


            double sinJ = ey / c;
            double cosJ = ex / c;

            if (sinJ > Math.sin(Math.PI / 6))
                yM = true;
            else if (sinJ < Math.sin(-Math.PI / 6))
                yP = true;


            if (cosJ > Math.cos(Math.PI / 3))
                xM = true;
            else if (cosJ < Math.cos(2 * Math.PI / 3))
                xP = true;
        }

        void setJoystickDraw(boolean joystickDraw) {
            gameThread.setJoystickDraw(joystickDraw);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        gameThread.setRunning(true);
        enemyThread.setRunning(true);
        toGameThread.setRunning(true);

        wait4Holder();

        gameThread.start();
        //enemyThread.start();
        toGameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        gameThread.setRunning(false);
        toGameThread.setRunning(false);
        enemyThread.setRunning(false);

        while (toGameThread.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (gameThread.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (enemyThread.isAlive()){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void wait4Holder() {
        while (!surfaceHolder.isCreating()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class EnemyThread extends Thread{

        BigInteger enemies = new BigInteger(String.valueOf(BigInteger.ZERO));
        long diffiult = 100000;
        private boolean running;
        private boolean isplus = true;
        private long waitTime;

        @Override
        public void run() {
            while(running) {
                Map.addEnemy();

                if (isplus && (enemies.intValue() / 100) % 2 == 0) {
                    isplus = false;
                    waitTime = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - waitTime >= 1000 * 60){
                    isplus = true;
                    waitTime = 0;
                }

                if (!isplus)
                    enemies = enemies.add(BigInteger.ONE);

                try {
                    Thread.sleep(diffiult / (enemies.intValue() + 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void setRunning(boolean running) {
            this.running = running;
        }
    }

    double getScreenX(int i) {
        return gameThread.getScreenX(i);
    }

    double getScreenY(int i) {
        return gameThread.getScreenY(i);
    }

    void setJumping(boolean jumping) {
        gameThread.setJumping(jumping);
    }
}