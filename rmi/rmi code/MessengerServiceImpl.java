package org.example.rmi;
import java.rmi.*;

public class MessengerServiceImpl implements MessengerService {
    @Override
    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }


}
