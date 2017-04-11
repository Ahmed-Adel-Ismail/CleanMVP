package com.appzoneltd.lastmile.customer.features.tracking;

import com.base.presentation.references.Property;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Wafaa on 2/20/2017.
 */
public class TrackingModelTest {

    public TrackingModelTest() {
    }

    @Test
    public void rxSubjectOnNextWithBufferOperator$$finalResult19() throws Exception {

        // Arrange
        final Property<Integer> finalResult = new Property<>(0);
        final PublishSubject<Integer> subject = PublishSubject.create();
        Observable<List<Integer>> observable = subject.buffer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.trampoline());


        Disposable disposable = observable.subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                for (int i : integers) {
                    finalResult.set(i);
                }
                System.out.println("onNext() : " + integers.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError()");
                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete()");
            }
        });


        // Act
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subject.onNext(i);
        }
        subject.onComplete();

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }


        // Assert
        Assert.assertTrue(finalResult.get() == 19);
    }
}