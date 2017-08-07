package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.LoginModel;
import com.example.presenter.inter.OnLoginListener;
import com.example.utils.ConnectUtils;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class LoginModelImpl implements LoginModel {



    public LoginModelImpl(){



    }

    @Override
    public void login(final String verInfo) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {

                try {

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ConnectUtils.getSocket().getOutputStream()));
                    bw.write(verInfo);
                    e.onNext(verInfo);
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
                 Log.e("verinfo",(String)o);
             }
         });


    }

    @Override
    public void loadGdtmId(final OnLoginListener onLoginListener) {
        final CompositeDisposable  compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                try {
                    InputStream is  = ConnectUtils.getSocket().getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = is.read(b))!=-1){
                        String str = new String(b,0,len);
                        e.onNext(str);
                        e.onComplete();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e("gdtm",(String)o);
                        String s =(String) o;
                        if(!"#E".equals(s) && !"#F".equals(s)){
                            String[] str = s.split("\\$");
                            String[] gdtmId = new String[str.length-1];
                            for(int i=0;i<gdtmId.length;i++){
                                gdtmId[i] = str[i+1];
                            }
                            onLoginListener.onSuccess(gdtmId);
                            compositeDisposable.clear();
                        }else{
                            onLoginListener.onFailure(s);
                            compositeDisposable.clear();
                        }
                    }
                }));


    }
}
