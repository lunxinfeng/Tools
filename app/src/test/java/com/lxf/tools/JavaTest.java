package com.lxf.tools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class JavaTest {

    @Test
    public void zip() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        list2.add("d");
        List<String> list3 = new ArrayList<>();
        list3.add("A");
        list3.add("B");
        list3.add("C");
        list3.add("D");
        list3.add("E");
        list3.add("F");
        Observable observable_1 = Observable.fromIterable(list1);
        Observable observable_2 = Observable.fromIterable(list2);
        Observable observable_3 = Observable.fromIterable(list3);
        Observable
                .zip(
                        observable_1,
                        observable_2,
                        observable_3,
                        new Function3() {
                            @Override
                            public Object apply(Object o1, Object o2, Object o3) throws Exception {
                                System.out.println("old data t1:" + o1);
                                System.out.println("old data t2:" + o2);
                                System.out.println("old data t3:" + o3);

                                return o1.toString() + o2 + o3;
                            }
                        }
                )
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("new data:" + o);
                        System.out.println("===================");
                    }
                });
    }
}
