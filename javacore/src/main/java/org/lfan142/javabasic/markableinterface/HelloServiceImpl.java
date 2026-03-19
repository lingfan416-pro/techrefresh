package org.lfan142.javabasic.markableinterface;

import java.rmi.RemoteException;

public class HelloServiceImpl implements HelloService{

    protected HelloServiceImpl() throws RemoteException{
        super();
    }

    @Override
    public String sayHello(String str) throws RemoteException {
        return "Hello from " + str;
    }
}
