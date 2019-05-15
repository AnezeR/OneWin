package com.example.onewin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.example.onewin.card.Effects;
import com.example.onewin.map.Map;
import com.example.onewin.map.MapStruct;

import java.util.ArrayList;
import java.util.Scanner;

import static com.example.onewin.Params.JR;
import static com.example.onewin.Params.joystickR;


public class GameThread extends Thread {

    private static final String TAG = "MyHuntTag";

    private final SurfaceHolder surfaceHolder;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int R = Params.R;
    private int heroFloor = Params.startFloor;

    private boolean isJoystickDraw = false;
    private boolean running;
    private boolean updating;
    private boolean jumping = false;
    private boolean xP = false, yP = false, xM = false, yM = false;
    private boolean isEnded = false;

    private double enemyScreenX = 0, enemyScreenY = 0;
    private double joystickX, joystickY;
    private double jMX, jMY;

    private float screenSizeX = Params.screenX / 2, screenSizeY = Params.screenY / 2;


    GameThread(SurfaceHolder surfaceHolder) {

        this.surfaceHolder = surfaceHolder;

        paint.setTextSize(40);
    }

    @Override
    public void run() {

        MainActivity.mClient.sayYouRunning();
        boolean fst = true;
        while (running) {
            try {
                checkEffects();

                synchronized (Map.class) {
                    if (!isEnded) {
                        checkGo();
                        if (!jumping)
                            checkFloor();
                    }
                }

                synchronized (surfaceHolder) {

                    Canvas canvas = surfaceHolder.lockCanvas();

                    canvas.drawColor(Color.WHITE);

                    if (fst) {
                        float ts = paint.getTextSize();
                        paint.setTextSize(100);
                        canvas.drawText("LOADING...", screenSizeX, screenSizeY, paint);
                        paint.setTextSize(ts);

                        surfaceHolder.unlockCanvasAndPost(canvas);
                        MainActivity.mClient.waitForServer();
                        fst = false;
                    }

                    synchronized (Map.class) {
                        Map.draw(canvas);
                    }

                    syncWithServer();

                    paint.setColor(Color.BLACK);

                    canvas.drawText(Hero.hp + "/10", screenSizeX * 2 - 200, 100, paint);

                    paint.setStyle(Paint.Style.FILL);

                    paint.setColor(Color.CYAN);
                    canvas.drawCircle(screenSizeX, screenSizeY, Params.R, paint);

                    paint.setColor(Color.RED);
                    canvas.drawCircle((float) enemyScreenX, (float) enemyScreenY, Params.R, paint);

                    paint.setColor(Color.argb(255 / 2, 255, 255, 255));

                    if (isJoystickDraw)
                        drawJoystick(canvas, paint);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkGo() {

        boolean notX = ((Map.getWidth() + Map.getX() <= screenSizeX + R && xM)
                || (Map.getX() >= screenSizeX - R && xP));
        boolean notY = ((Map.getHeight() + Map.getY() <= screenSizeY + R && yM)
                || (Map.getY() >= screenSizeY - R && yP));

        if (notX) {
            Map.setLastScreenX();
            Map.setUpdateX(false);
        } else
            Map.setUpdateX(updating);

        if (notY) {
            Map.setLastScreenY();
            Map.setUpdateY(false);
        } else
            Map.setUpdateY(updating);

    }

    private void checkFloor() {

        ArrayList<MapStruct> assets = Map.getDrawingMapStructs();
        boolean ableX = true, ableY = true;
        Map.setSetLsX(true);
        Map.setSetLsY(true);

        for (int i = 0; i < assets.size(); i++) {

            MapStruct struct = assets.get(i);

            if (struct.getFloor() != heroFloor) {

                boolean right = (struct.getScreenX() + struct.getWidth() <= screenSizeX - R);
                boolean left = (struct.getScreenX() >= screenSizeX + R);
                boolean bottom = (struct.getScreenY() + struct.getHeight() <= screenSizeY - R);
                boolean top = (struct.getScreenY() >= screenSizeY + R);

                if (!right && !left) {
                    ableX = false;
                }

                if (!top && !bottom) {
                    ableY = false;
                }

                if (!ableX && !ableY) {

                    Map.setSetLsX(false);
                    Map.setSetLsY(false);

                    right = (struct.getLastScreenX() + struct.getWidth() <= screenSizeX - R);
                    left = (struct.getLastScreenX() >= screenSizeX + R);
                    bottom = (struct.getLastScreenY() + struct.getHeight() <= screenSizeY - R);
                    top = (struct.getLastScreenY() >= screenSizeY + R);

                    if ((right && xP) || (left && xM)) {

                        Map.setLastScreenX();
                        Map.setUpdateX(false);
                    } else {
                        Map.setUpdateX(updating);
                    }

                    if ((top && yM) || (bottom && yP)) {
                        Map.setLastScreenY();
                        Map.setUpdateY(false);
                    } else {
                        Map.setUpdateY(true);
                    }

                } else {
                    ableX = true;
                    ableY = true;
                }
            }
        }

    }

    private void syncWithServer() {


        ArrayList<Object> data = MainActivity.mClient.getClientData();

        String s;

        Scanner sc;

        if (data.size() > 1) {
            s = String.valueOf(data.get(0)) + '\n' + data.get(1);

            sc = new Scanner(s);

            String a;
            a = sc.nextLine();
            enemyScreenX = -Double.parseDouble(a) + Map.getScreenX(0) + screenSizeX;
            a = sc.nextLine();
            enemyScreenY = -Double.parseDouble(a) + Map.getScreenY(0) + screenSizeY;
        }

        for (int i = 2; i < data.size() - 3; i += 4) {

            s = String.valueOf(data.get(i)) + ' ' + data.get(i + 1) + '\n' +
                    data.get(i + 2) + '\n' + data.get(i + 3);
            sc = new Scanner(s);

            int id = sc.nextInt();
            long time = (sc.nextLong());
            sc.nextLine();
            String str = sc.nextLine();
            float scX = (Float.parseFloat(str));
            str = sc.nextLine();
            float scY = (Float.parseFloat(str));

            Effects.callEffect(id, time, scX, scY);
        }
    }


    private void drawJoystick(Canvas canvas, Paint paint) {

        paint.setColor(Color.argb(200, 255, 255, 255));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.drawCircle((float) jMX, (float) jMY, (float) joystickR, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);

        canvas.drawCircle((float) jMX, (float) jMY, (float) joystickR, paint);

        paint.setAlpha(200);

        canvas.drawCircle((float) joystickX, (float) joystickY, (float) JR, paint);

        paint.setAlpha(240);

        canvas.drawCircle((float) joystickX, (float) joystickY, (float) JR, paint);
    }

    private void checkEffects() {

        if (Effects.check()) {
            Hero.hp--;
        }

        if (Hero.hp == 0) {
            end();
        }
    }

    private void end() {
        isEnded = true;
        Params.speed = 0;
    }

    void setJoystickDraw(boolean isJoystickDraw) {
        this.isJoystickDraw = isJoystickDraw;
    }

    void setJoystickParams(double x, double y, double midX, double midY) {


        jMX = midX;
        jMY = midY;

        double ex = x - midX, ey = y - midY;

        double c = Math.hypot(ex, ey);

        if (c > joystickR) {
            x = joystickR / c * ex + midX;
            y = joystickR / c * ey + midY;

        } else {
            int speed = 12;
            Params.speed = (int) (c / joystickR * speed);
        }

        joystickX = x;
        joystickY = y;
    }

    private void drawBulletJoystick(Canvas canvas) {
        if (BulletJoystickParams.isEnabled()) {
            double midX = BulletJoystickParams.getMidX(), midY = BulletJoystickParams.getMidY();
            double x = BulletJoystickParams.getX(), y = BulletJoystickParams.getY();

            double ex = x - midX, ey = y - midY;

            double c = Math.hypot(ex, ey);

            if (c > joystickR) {
                x = joystickR / c * ex + midX;
                y = joystickR / c * ey + midY;
            } else {
            }

            Paint paint = new Paint();

            paint.setColor(Color.argb(200, 255, 255, 255));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            canvas.drawCircle((float) midX, (float) midY, (float) joystickR, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);

            canvas.drawCircle((float) midX, (float) midY, (float) joystickR, paint);

            paint.setAlpha(200);

            canvas.drawCircle((float) x, (float) y, (float) JR, paint);

            paint.setAlpha(240);

            canvas.drawCircle((float) x, (float) y, (float) JR, paint);
        }
    }

    void setUpdate(boolean update, boolean xP, boolean yP, boolean xM, boolean yM) {

        if (!isEnded) {

            this.xP = xP;
            this.xM = xM;
            this.yP = yP;
            this.yM = yM;

            Map.setPM(xP, yP, xM, yM);

            updating = update;
        } else {
            Map.setUpdateX(false);
            Map.setUpdateY(false);
            updating = false;
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    double getScreenX(int i) {
        return Map.getScreenX(i);
    }

    double getScreenY(int i) {
        return Map.getScreenY(i);
    }

    void setDrawingRoom(int room) {
        Map.changeDrawingRoom(room);
    }
}