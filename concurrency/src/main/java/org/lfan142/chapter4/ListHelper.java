package org.lfan142.chapter4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ListHelper<E> {
    String s;

    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    public synchronized boolean putIfAbsent(E x){


        synchronized(list){
            boolean absent = !list.contains(x);
            if(absent){
                list.add(x);
            }
            return absent;
        }
    }
}
