package com.example.onewin.server;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConnectionWorker implements Runnable {

    private static final String TAG = "MyHuntTag";

    private Scanner sc = null;

    private PrintWriter pw = null;

    private int dataPlace;
    private int gettingDataPlace;

    private Socket clientSocket;

    ConnectionWorker(Socket socket, int dataPlace) {
        clientSocket = socket;

        this.dataPlace = dataPlace;
        gettingDataPlace = (dataPlace == 0 ? 1 : 0);
    }

    @Override
    public void run() {

        initInp();

        Log.d(TAG, "Успешное подключение к клиенту: " + clientSocket.getInetAddress());

        try {
            sc.nextLine();
            Server.plusConnection();
            while (Server.getStartedConnections() < 2) ;
            pw.println();
            pw.flush();
            synchronized (Server.isWorkersHere) {
                Server.isWorkersHere.set(dataPlace, true);
            }
        } catch (Exception e){
            Log.d(TAG, "Connection worker " + dataPlace + ':' + e.getMessage());
        }

        while (!Server.isWorkersHere.get(gettingDataPlace));

        while (true) {
            if (sc != null && pw != null) {

                String buf = "";
                while (buf.length() == 0) {
                    buf = sc.nextLine();
                }
                char ch = buf.charAt(0);
                sc.nextLine();

                if (ch == 'w') {

                    Pattern p = sc.delimiter();
                    sc.useDelimiter("t");

                    synchronized (Server.data) {
                        Server.data.set(dataPlace, sc.next());
                    }
                    sc.useDelimiter(p);
                    sc.nextLine();

                } else if (ch == 'r') {

                    String a;

                    synchronized (Server.data) {
                        a = Server.data.get(gettingDataPlace);
                    }

                    if (a.length() > 0) {
                        pw.println('+');
                        pw.println(a);
                    } else {
                        pw.println('-');
                    }
                }

                pw.println('q');
                pw.flush();
            } else {
                initInp();
            }
        }
    }

    private void initInp() {
        try {
            sc = new Scanner(clientSocket.getInputStream());
            Log.d(TAG, "Получен поток приёма с " + clientSocket.getInetAddress());
        } catch (IOException e) {
            Log.d(TAG, "Не удалось получить поток приёма " + clientSocket.getInetAddress());
        }

        try {
            pw = new PrintWriter(clientSocket.getOutputStream());
            Log.d(TAG, "Получен поток передачи на " + clientSocket.getInetAddress());
        } catch (IOException e) {
            Log.d(TAG, "Не удалось получить поток переадчи на " + clientSocket.getInetAddress());
        }
    }
}