package com.example.model.impl;

import com.example.model.LoginModel;
import com.example.presenter.OnLoginListener;
import com.example.utils.ConnectUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class LoginModelImpl implements LoginModel {
    private PublishSubject<String> publishSubject;


    public LoginModelImpl(){

        publishSubject = PublishSubject.create();

    }

    @Override
    public void login(String verInfo) {
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ConnectUtils.getSocket().getOutputStream()));
            bw.write(verInfo);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGdtmId(final OnLoginListener onLoginListener) {
            publishSubject.subscribeOn(Schedulers.io());
            publishSubject.observeOn(AndroidSchedulers.mainThread());
            publishSubject.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    try {
                        InputStream is  = ConnectUtils.getSocket().getInputStream();
                        byte[] b = new byte[1024];
                        int len = 0;
                        while ((len = is.read(b))!=-1){
                            String str = new String(b,0,len);
                            publishSubject.onNext(str);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNext(@NonNull String s) {
                       if(!"#E".equals(s) && !"#F".equals(s)){
                           String[] str = s.split("\\$");
                           String[] gdtmId = new String[str.length-1];
                           for(int i=0;i<gdtmId.length;i++){
                               gdtmId[i] = str[i+1];
                           }
                           onLoginListener.onSuccess(gdtmId);
                       }else{
                           onLoginListener.onFailure(s);
                       }
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

//            publishSubject.subscribe(new Consumer<String>() {
//                @Override
//                public void accept(@NonNull String s) throws Exception {
//                       if(!"#E".equals(s) && !"#F".equals(s)){
//                           String[] str = s.split("\\$");
//                           String[] gdtmId = new String[str.length-1];
//                           for(int i=0;i<gdtmId.length;i++){
//                               gdtmId[i] = str[i+1];
//                           }
//                           onLoginListener.onSuccess(gdtmId);
//                       }else{
//                           onLoginListener.onFailure(s);
//                       }
//                }
//            });




    }
}
