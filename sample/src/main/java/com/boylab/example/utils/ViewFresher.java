package com.boylab.example.utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 定时刷新页面
 * （单刷）
 */
public class ViewFresher {

    private static ViewFresher viewFresher = null;
    private HashMap<Integer, Disposable> fresherMap = new HashMap<>();
    private OnViewFreshListener onViewFreshListener = null;

    ViewFresher() {
    }

    public static ViewFresher newInstance() {
        if (viewFresher == null) {
            viewFresher = new ViewFresher();
        }
        return viewFresher;
    }

    public void addFresh(int what, OnViewFreshListener onViewFreshListener) {
        this.addFresh(what, 50, onViewFreshListener);
    }

    public void addFresh(int what, long period, OnViewFreshListener onFreshListener) {
        remove(what);
        onViewFreshListener = onFreshListener;
        Observable<Long> timerObservable = Observable.interval(50, period, TimeUnit.MILLISECONDS, Schedulers.single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Disposable disposable = timerObservable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (onViewFreshListener != null){
                    onViewFreshListener.onViewFresh();
                }
            }
        });
        fresherMap.put(what, disposable);
    }

    private void filterFresh(int what) {
        if (fresherMap.containsKey(what)) {
            remove(what);
        }
    }

    public void remove(int what) {
        if (fresherMap.containsKey(what)){
            Disposable disposable = fresherMap.get(what);
            if (disposable != null && !disposable.isDisposed()){
                disposable.dispose();
            }
            fresherMap.remove(what);
        }
    }

    public interface OnViewFreshListener {
        void onViewFresh();
    }
}
