package com.epam.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadContextDemo {
    private static final InheritableThreadLocal<Map<Integer, String>> context = new InheritableThreadLocal<>() {
        @Override
        protected Map<Integer, String> initialValue() {
            Map<Integer, String> initialValue = new HashMap<>();
            initialValue.put(1, "initial");
            return initialValue;
        }

        @Override
        protected Map<Integer, String> childValue(Map<Integer, String> parentValue) {
            return new HashMap<>(parentValue);
        }
    };

    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(2);
        System.out.println("Parent Thread : " + context.get());

        Map<Integer, String> map = context.get();
        var contextMap = new HashMap<>(map);

        Runnable r1 = () -> {
            context.get().putAll(contextMap);
            context.get().put(new Random().nextInt(), Thread.currentThread().getName());
            System.out.println("Child Thread Name : " + Thread.currentThread().getName() + ", its context : "+context.get());
            clearContext();
        };

        es.execute(r1);
        es.execute(r1);
        es.execute(r1);
        es.execute(r1);
        es.execute(r1);

        //shutdow the executor
        es.shutdown();
    }

    private static void clearContext() {
        context.get().clear();
    }
}
