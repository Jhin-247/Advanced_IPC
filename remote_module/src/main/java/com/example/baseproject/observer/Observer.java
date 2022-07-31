package com.example.baseproject.observer;

import com.example.baseproject.MyProduct;

import java.util.List;

public interface Observer {
    void notifyProducts(List<MyProduct> productList);
}
