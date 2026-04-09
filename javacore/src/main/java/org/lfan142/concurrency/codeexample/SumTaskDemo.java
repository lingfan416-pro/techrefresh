package org.lfan142.concurrency.codeexample;

import java.util.concurrent.ForkJoinPool;

public class SumTaskDemo {

    public static void main(String[] args) {
        int[] arr = new int[10000];
        for(int i=0; i<arr.length; i++){
            arr[i] = 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SumTask(arr, 0, arr.length));
        System.out.println(result);
    }
}
