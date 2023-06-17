package org.example.rmi;
import javax.swing.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;


public class RmiClient {
    public static void main(String[] args) throws RemoteException, NotBoundException  {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        MessengerService msg = (MessengerService) registry.lookup("MessengerService");
        String responseMessage = msg.sendMessage("Client Message");

        System.out.println(responseMessage);

    }
}
