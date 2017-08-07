package com.example.model.impl;

import android.util.Log;

import com.example.model.VertifyModel;
import com.example.presenter.OnCheckListener;
import com.example.utils.ConnectUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class CheckModelImpl implements VertifyModel {


    public CheckModelImpl(){

    }



    @Override
    public void sendCode(String version) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(ConnectUtils.getSocket().getOutputStream()));
            bw.write(version);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void loadStatus(final OnCheckListener onCheckListener) {

        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {

                    InputStream is  = ConnectUtils.getSocket().getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = is.read(b))!=-1){
                        String str = new String(b,0,len);
                        e.onNext(str);
                        Log.e("tag",str);
                    }

            }

        });
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(Schedulers.io());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e("s",s);
                if(s.equals("#0")){
                    onCheckListener.onSuccess();
                }else{
                    onCheckListener.onFailure();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
      observable.subscribe(observer);

    }
}
