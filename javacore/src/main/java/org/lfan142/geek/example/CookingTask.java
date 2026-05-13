package org.lfan142.geek.example;

public class CookingTask extends Thread{

    private String task;

    CookingTask(String task){
        this.task = task;
    }

    @Override
    public void run(){
        System.out.println(task + " is being prepared by " + Thread.currentThread().getName());
    }
}
