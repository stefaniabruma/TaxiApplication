package com.example.taximetrie.Utils;

public interface Observable {

    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
