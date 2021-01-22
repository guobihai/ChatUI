package com.smart.chatui.ui.sort;


import java.util.Comparator;

import trf.smt.com.netlibrary.enity.Person;

public class PinyinComparator implements Comparator<Person> {

    public int compare(Person o1, Person o2) {
        if (o1.getLetters().equals("@")
                || o2.getLetters().equals("#")) {
            return 1;
        } else if (o1.getLetters().equals("#")
                || o2.getLetters().equals("@")) {
            return -1;
        } else {
            return o1.getLetters().compareTo(o2.getLetters());
        }
    }

}
