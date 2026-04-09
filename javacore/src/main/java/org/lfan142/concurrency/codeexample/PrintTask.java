package org.lfan142.concurrency.codeexample;

import java.util.concurrent.RecursiveAction;

public class PrintTask extends RecursiveAction {

    private final int start;
    private final int end;

    PrintTask(int start, int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if(end - start <= 5){
            for(int i=start; i<end; i++){
                System.out.println(i);
            }
        } else {
            int mid = (start + end)/2;
            invokeAll(new PrintTask(start, mid),
                    new PrintTask(mid, end));
        }
    }
}
