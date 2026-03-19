package org.lfan142.chapter4;

public class MutablePoint {
    private int x;
    private int y;

    public MutablePoint(int x, int y){
        this.x = x;
        this.y = y;
    }


    public MutablePoint(MutablePoint loc) {
        this.x = loc.getX();
        this.y = loc.getY();
    }

    public int getX() {

        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int Y){
        this.y = y;
    }
}
