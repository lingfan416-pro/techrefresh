package org.lfan142.chapter1;

public class CalculateThread extends Thread {
    private int cal = 0;
    public void run(){
        cal = cal+1;
        System.out.println(cal + " test");
    }
    public int getCal(){
        return cal;
    }
    public static void main(String[] args) throws InterruptedException {
        CalculateThread thread = new CalculateThread();
        thread.start();

        System.out.println(thread.getCal());
    }
}
