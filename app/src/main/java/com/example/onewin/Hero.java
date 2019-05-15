package com.example.onewin;

import com.example.onewin.card.Card;

import java.util.ArrayList;

final class Hero {

    static ArrayList<Card> cards;
    static int hp = 10, speed, weapon;

    static void create(){
        cards = new ArrayList<>();
    }
}