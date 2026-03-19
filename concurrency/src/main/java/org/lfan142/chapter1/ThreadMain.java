package org.lfan142.chapter1;


public class ThreadMain {

    public static void main(String[] args) throws InterruptedException {


        IntegerSum sumCalculator = new IntegerSum();
        Thread t1 = new Thread(()->{
            int i=10000;
            while(i>0){
                sumCalculator.add(1);
                i--;
            }
        });
        Thread t2 = new Thread(()->{
            int j = 600;
            while(j>0){
                j--;
                sumCalculator.add(j);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(sumCalculator.getValue());

    }

}
