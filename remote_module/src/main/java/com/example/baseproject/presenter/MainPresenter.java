package com.example.baseproject.presenter;

import android.content.Context;

public class MainPresenter implements MainContract.MainPresenter {
    private MainContract.MainView mView;


    public MainPresenter(MainContract.MainView mView) {
        this.mView = mView;
    }

}
