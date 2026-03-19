package org.lfan142.javabasic.markableinterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReadTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
       try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.cer"))){
           User user = (User) ois.readObject();
       }
    }
}
