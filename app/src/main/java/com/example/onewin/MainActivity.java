package com.example.onewin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.onewin.card.Card;
import com.example.onewin.card.Effects;
import com.example.onewin.map.Map;
import com.example.onewin.server.Client;
import com.example.onewin.server.Server;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    private static final String TAG = "MyHuntTag";
    public static GameView gameView = null;
    static Client mClient = null;
    static Thread threadToSmt = null;
    public ConstraintLayout game = null;
    private ArrayList<ImageButton> cards = null;
    private ImageButton b1 = null, b2 = null;
    private ChangeCards changeCards = null;
    private Activity mainActivity;
    private float screenSizeX, screenSizeY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init();
    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    void init(){

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addParams();
        Map.create();
        Effects.create();
        setScreenSize();
        setNulls();

        int w, h;

        Bitmap b = Params.resources.get(2);
        w = b.getWidth() / 2;
        h = b.getHeight() / 2;
        b = Bitmap.createScaledBitmap(b, w, h, true);
        b1.setImageBitmap(b);
        b1.setBackground(null);
        b1.setAlpha((float) 0);

        b = Params.resources.get(3);
        w = b.getWidth() / 8;
        h = b.getHeight() / 8;
        b = Bitmap.createScaledBitmap(b, w, h, true);
        b2.setImageBitmap(b);
        b2.setBackground(null);
        b2.setX(screenSizeX * 2 - 200 - b.getWidth());
        b2.setY(screenSizeY * 2 - 200 - b.getHeight());

        View iv = new View(this);
        iv.setLeft((int) screenSizeX);
        iv.setRight((int) (screenSizeX * 2));
        iv.setTop(0);
        iv.setBottom((int) (screenSizeY * 2));
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                double x = motionEvent.getX(), y = motionEvent.getY();
                if (x > screenSizeX) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        BulletJoystickParams.setEnabled(true);
                        BulletJoystickParams.setMidXY(x, y);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                        BulletJoystickParams.setXY(x, y);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        BulletJoystickParams.setEnabled(false);
                    }
                }

                return true;
            }
        });

        gameView.setId(123123);
        game.addView(gameView);
        game.addView(iv);
        game.addView(b1);
        game.addView(b2);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setJumping(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                gameView.setJumping(false);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {

            boolean isCalled = false;

            @Override
            public void onClick(View v) {
                if (!isCalled) {
                    game.removeView(b1);
                    b1 = null;
                } else {
                    game.removeView(b1);
                    b1 = null;
                }
            }
        });

        for (int i = 0; i < 4; i++) {
            createCard(i);
        }

        setContentView(R.layout.activity_main);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(game);
            }
        });

        findViewById(R.id.button_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(Server.getServer()).start();

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_connect).callOnClick();
                    }
                });
            }
        });

        findViewById(R.id.button_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClient = new Client();

                threadToSmt =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mClient.openConnection();
                        mClient.start();
                    }
                });

                threadToSmt.start();

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_server).setEnabled(false);
                    }
                });
                v.setEnabled(false);
            }
        });


        changeCards.start();



    }

    void setNulls(){
        changeCards = new ChangeCards(this);
        game = new ConstraintLayout(this);
        gameView = new GameView(this);
        mainActivity = this;

        b1 = new ImageButton(this);
        b2 = new ImageButton(this);

        cards = new ArrayList<>();
    }

    void setScreenSize(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Params.screenX = size.x;
        Params.screenY = size.y;

        screenSizeX = size.x / 2f;
        screenSizeY = size.y / 2f;
    }

    void createCard(int id){
        final ImageButton button = new ImageButton(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bluecircle);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 16, bitmap.getHeight() / 16, true);

        button.setImageBitmap(bitmap);
        button.setBackground(null);

        final int finalI = id;
        button.setOnClickListener(new View.OnClickListener() {

            int id = finalI;
            ImageButton b = button;

            @Override
            public void onClick(View v) {
                try {
                    Effects.callEffect(id);
                    game.removeView(b);
                    cards.remove(b);

                    for (int j = 0; j < cards.size(); j++) {

                        game.removeView(cards.get(j));
                        cards.get(j).setX(300 + j * 200);

                        game.addView(cards.get(j));
                    }

                } catch (Exception ignored){

                }
            }
        });
        if (cards.size() != 0)
            button.setX(cards.get(cards.size() - 1).getX() + 200);
        else
            button.setX(300);

        button.setY(screenSizeY * 2 - 200);

        Log.d(TAG, "onCreate: " + button.getX() + ' ' + button.getY());

        cards.add(button);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                game.addView(button);
            }
        });
    }

    private void addParams(){
        Bitmap adding;

        Params.create();

        adding = BitmapFactory.decodeResource(getResources(), R.drawable.fon);
        Log.d(TAG, "Created bitmap 0");

        Params.resources.add(adding);

        Log.d(TAG, "added bitmap 0 " + Params.resources.get(0).toString());

        adding = BitmapFactory.decodeResource(getResources(), R.drawable.fill);
        Params.resources.add(adding);

        Log.d(TAG, "added bitmap 1");

        adding = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        Params.resources.add(adding);

        Log.d(TAG, "added bitmap 2");

        adding = BitmapFactory.decodeResource(getResources(), R.drawable.up);
        Params.resources.add(adding);

        Log.d(TAG, "added bitmap 3");


        Params.effectsArrayList.add(adding);
    }

    @Override
    protected void onStop() {
        game = null;
        super.onStop();
    }

    class ChangeCards extends Thread {

        boolean running = true;

        Context context;

        ChangeCards(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    ArrayList<Card> cardsArrayList = Map.getCardsArrayList();

                    for (int i = 0; i < cardsArrayList.size(); i++) {
                        Card struct = cardsArrayList.get(i);

                        boolean right = (struct.getScreenX() + struct.getWidth() <= screenSizeX - Params.R);
                        boolean left = (struct.getScreenX() >= screenSizeX + Params.R);
                        boolean bottom = (struct.getScreenY() + struct.getHeight() <= screenSizeY - Params.R);
                        boolean top = (struct.getScreenY() >= screenSizeY + Params.R);

                        if (!(right || left || bottom || top)){
                            synchronized (Map.class) {
                                Map.removeCard(i);
                            }
                            createCard(0);

                            Log.d(TAG, "run: card number: " + i);
                        }
                    }
                } catch (Exception e){
                    Log.d(TAG, "run: Exception: " + e.getMessage());
                }
            }
        }
    }
}