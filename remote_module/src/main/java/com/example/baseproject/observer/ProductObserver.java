package com.example.baseproject.observer;

import com.example.baseproject.MyProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductObserver {
    private static ProductObserver sInstance;
    private List<Observer> observers;

    private ProductObserver() {
        observers = new ArrayList<>();
    }

    public static synchronized ProductObserver getInstance() {
        if (sInstance == null) {
            sInstance = new ProductObserver();
        }
        return sInstance;
    }

    public void notifyUpdate(List<MyProduct> productList) {
        for (Observer observer : observers) {
            observer.notifyProducts(productList);
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }


}
