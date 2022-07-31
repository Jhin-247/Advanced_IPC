package com.example.baseproject.presenter;

import com.example.baseproject.MyProduct;

import java.util.List;

public interface MainContract {
    interface MainView {
        void updateView(List<MyProduct> productList);
    }

    interface MainPresenter {
        void startService();

        void disconnectService();
    }
}
