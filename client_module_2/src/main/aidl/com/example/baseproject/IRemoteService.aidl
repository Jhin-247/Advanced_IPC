package com.example.baseproject;

import com.example.baseproject.MyProduct;
import com.example.baseproject.ICallBack;
interface IRemoteService {
    int getPid();
    void sendProduct(ICallBack callback, int pId);
}