package io.reactivex.properties;


import java.lang.ref.WeakReference;
import java.util.LinkedList;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;

/**
 * a group of {@link Emitter} instances
 * <p>
 * Created by Ahmed Adel Ismail on 4/24/2017.
 */
class EmittersGroup<T> extends LinkedList<WeakReference<ObservableEmitter<T>>>
        implements
        Emitter<T> {


    public boolean add(ObservableEmitter<T> object) {
        return super.add(new WeakReference<>(object));
    }

    @Override
    public void onNext(final T value) {
        Observable.fromIterable(this).forEach(new Consumer<WeakReference<ObservableEmitter<T>>>() {
            @Override
            public void accept(WeakReference<ObservableEmitter<T>> e) throws Exception {
                ObservableEmitter<T> emitter = e.get();
                if (emitter != null) emitter.onNext(value);
            }
        });
    }

    @Override
    public void onError(final Throwable error) {
        Observable.fromIterable(this).forEach(new Consumer<WeakReference<ObservableEmitter<T>>>() {
            @Override
            public void accept(WeakReference<ObservableEmitter<T>> e) throws Exception {
                ObservableEmitter<T> emitter = e.get();
                if (emitter != null) emitter.onError(error);
            }
        });
    }

    @Override
    public void onComplete() {
        Observable.fromIterable(this).forEach(new Consumer<WeakReference<ObservableEmitter<T>>>() {
            @Override
            public void accept(WeakReference<ObservableEmitter<T>> e) throws Exception {
                ObservableEmitter<T> emitter = e.get();
                if (emitter != null) emitter.onComplete();
            }
        });
    }
}
