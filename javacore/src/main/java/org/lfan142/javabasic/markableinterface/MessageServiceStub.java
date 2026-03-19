package org.lfan142.javabasic.markableinterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MessageServiceStub {

    public static void main(String[] args) throws RemoteException {
        HelloService server = new HelloServiceImpl();
        HelloService stub = (HelloService) UnicastRemoteObject.exportObject(server, 0);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("HelloService", stub);
    }
}
