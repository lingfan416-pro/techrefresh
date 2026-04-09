package org.lfan142.concurrency.codeexample;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD  = 1000;
    private final int[] array;
    private final int start;
    private int end;

    SumTask(int[] array, int start, int end){
        this.array = array;
        this.start = start;
         this.end = end;
    }

    @Override
    protected Long compute() {
        if(end - start <= THRESHOLD){
            long sum = 0;
            for(int i = start; i < end; i++){
                sum += array[i];
            }
            return sum;
        }

        int mid = (start + end) / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);

        left.fork(); //async execute left
        long rightResult = right.compute(); //compute right directly
        long leftResult = left.join(); //wait for left result

        return leftResult + rightResult;

    }
}
