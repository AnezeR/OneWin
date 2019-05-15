package com.example.onewin.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements Runnable {

    private static final String TAG = "MyHuntTag";

    private static volatile Server instance = null;

    private static final int port = 8080;

    static final ArrayList<String> data = new ArrayList<>();

    static final ArrayList<Boolean> isWorkersHere = new ArrayList<>();

    private static int startedConnections = 0;

    private ServerSocket serverSocket = null;

    private Server() {
    }

    public static Server getServer() {

        if (instance == null) {
            synchronized (Server.class) {
                if (instance == null) {
                    instance = new Server();
                }
            }
        }

        return instance;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            Log.d(TAG, "Открыт сервер на порту " + port);
            int place = 0;
            while (true) {
                ConnectionWorker worker;

                try {
                    worker = new ConnectionWorker(serverSocket.accept(), place++);

                    Log.d(TAG, "Get client connection");

                    data.add(new String(new byte[1024 * 4], 0, 1024 * 4));
                    isWorkersHere.add(false);

                    new Thread(worker).start();

                    Log.d(TAG, "Изменение базы: " + data.size());

                    if (place == 2){
                        break;
                    }

                } catch (IOException e) {
                    Log.d(TAG, "Connection error " + e.getMessage());
                }
            }

        } catch (IOException e) {
            Log.d(TAG, "Cant start server on port " + port + " : " + e.getMessage());
        } finally {

            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    static synchronized void plusConnection(){
        startedConnections++;
    }

    static synchronized int getStartedConnections(){
        return startedConnections;
    }
}
