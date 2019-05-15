package com.example.onewin.server;

import android.util.Log;

import com.example.onewin.card.Effects;
import com.example.onewin.map.Map;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {

    private static final String TAG = "MyHuntTag";

    private static final String serverIp = "192.168.43.1";

    private static final int serverPort = 8080;

    private PrintWriter pw = null;

    private Scanner sc = null;

    private final ArrayList<Object> data = new ArrayList<>();

    private Socket socket = null;

    public Client() {
        this.setName("ClientThread");
    }

    public void openConnection() {
        try {
            socket = new Socket(serverIp, serverPort);

            sc = new Scanner(socket.getInputStream());

            pw = new PrintWriter(socket.getOutputStream());

            Log.d(TAG, "Успешное подключение к серверу: " + socket.getInetAddress());
        } catch (IOException e) {
            Log.d(TAG, "Невозможно открыть сокет " + e.getMessage());
        }
    }

    private void closeConnection() {

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.d(TAG, "Невозможно закрыть сокет " + e.getMessage());
            } finally {
                socket = null;
            }
        }

        socket = null;
    }

    @Override
    public void run() {
    }

    private void sendData() {

        StringBuilder out = new StringBuilder();

            try {
                out.append('w').append('\n').append('a').append('\n')
                        .append(Map.getScreenX(0)).append('\n')
                        .append(Map.getScreenY(0)).append('\n')
                        .append(Effects.getInfo()).append('t')
                        .append('\n');
            } catch (Exception ignored) {

            }

        if (out.length() == 6) {
            out.setCharAt(2, 'e');
        } else {
            out.setCharAt(2, 'f');
        }

        pw.print(out);

        pw.flush();

        sc.nextLine();
    }

    private void getData() {

        pw.println('r');
        pw.println('0');
        pw.flush();

        String nl = "";
        while (nl.isEmpty())
            nl = sc.nextLine();
        char c = nl.charAt(0);


        if (c == '+') {
            String str;
            data.clear();

            if (sc.hasNext()) {
                str = sc.nextLine();
                data.add(Float.parseFloat(str));

                str = sc.nextLine();
                data.add(Float.parseFloat(str));
            }
            if (sc.hasNextInt()) {
                while (sc.hasNextInt()) {

                    data.add(sc.nextInt());
                    data.add(sc.nextLong());
                    sc.nextLine();
                    str = sc.nextLine();
                    data.add(Float.parseFloat(str));
                    str = sc.nextLine();
                    data.add(Float.parseFloat(str));
                }
            } else {
                sc.nextLine();
            }
        } else if (c == '-')
            data.clear();


        sc.nextLine();
    }

    public void sayYouRunning(){
        pw.println();
        pw.flush();
    }

    public ArrayList<Object> getClientData() {

        sendData();
        getData();

        return new ArrayList<>(data);
    }

    public void waitForServer(){
        sc.nextLine();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }
}