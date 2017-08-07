package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.VertifyModel;
import com.example.presenter.inter.OnCheckListener;
import com.example.utils.ConnectUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class CheckModelImpl implements VertifyModel {


    public CheckModelImpl(){

    }



    @Override
    public void sendCode(final String version) {
    Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new OutputStreamWriter(ConnectUtils.getSocket().getOutputStream()));
                    bw.write(version);
                    e.onNext(version);
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
               }
            })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                 Log.e("version",(String)o);
            }
        });

    }
    @Override
    public void loadStatus(final OnCheckListener onCheckListener) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {

                InputStream is  = ConnectUtils.getSocket().getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = is.read(b))!=-1){
                    String str = new String(b,0,len);
                    e.onNext(str);
                }

            }
           })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e("msg1",(String) o);
                        String str = (String) o;
                        if("#0".equals(str)){
                            onCheckListener.onSuccess();
                            compositeDisposable.clear();
                        }else if("#F".equals(str)){
                            onCheckListener.onFailure();
                            compositeDisposable.clear();
                        }
                    }

                }));




    }
}
