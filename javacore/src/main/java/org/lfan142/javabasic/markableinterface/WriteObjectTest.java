package org.lfan142.javabasic.markableinterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObjectTest {

    public static void main(String[] args) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.cer"));

        oos.writeObject(new User("Alice", "abcd", 20));
        oos.close();
    }
}
