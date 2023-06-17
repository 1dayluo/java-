package org.example.rmi;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RmiServer {
    public static void main(String[] args) throws RemoteException, NotBoundException {

        // 创建一个stub
        MessengerService server = new MessengerServiceImpl();
        // 转换为远程服务接口
        MessengerService stub = (MessengerService) UnicastRemoteObject.exportObject((MessengerService) server, 0);
        // 创建registry
        Registry registry = LocateRegistry.createRegistry(1099);
        //绑定Stub
        registry.rebind("MessengerService", stub);


    }

}
