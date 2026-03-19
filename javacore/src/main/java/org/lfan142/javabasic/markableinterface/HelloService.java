package org.lfan142.javabasic.markableinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote {
    String sayHello(String str) throws RemoteException;
}
