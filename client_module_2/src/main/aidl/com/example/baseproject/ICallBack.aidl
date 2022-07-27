// ICallBack.aidl
package com.example.baseproject;
import com.example.baseproject.MyProduct;
// Declare any non-default types here with import statements

interface ICallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onReceiveProduct(inout MyProduct myProduct);
    void onReceiveFailed();
}