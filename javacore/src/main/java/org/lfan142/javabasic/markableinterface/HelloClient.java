package org.lfan142.javabasic.markableinterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);

        HelloService server = (HelloService) registry.lookup("HelloService");
        String responseMessage = server.sayHello("client");
        System.out.println(responseMessage);
    }
}
