package com.example.java_mvc_pattern.model;

import java.util.ArrayList;

public class Database {
    private static Database instance;
    private ArrayList<Person> personList = new ArrayList<>();
    private DatabaseListener databaseListener;

    private Database() {
        for (int index = 0; index < 100; index++) {
            personList.add(new Person(index, String.format("StickCode%d", index)));
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void add(Person person) {
        personList.add(0, person);
        notifyChange();
    }

    public void remove(Person person) {
        personList.remove(person);
        notifyChange();
    }

    private void notifyChange() {
        if (databaseListener != null) {
            databaseListener.onChanged();
        }
    }

    public void setOnDatabaseListener(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

    public interface DatabaseListener {
        void onChanged();
    }

}
