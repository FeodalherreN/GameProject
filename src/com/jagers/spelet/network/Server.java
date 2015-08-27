package com.jagers.spelet.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.jagers.spelet.level.Player;
import com.jagers.spelet.models.MPPlayer;

public class Server implements Runnable {
	
	private ServerSocket providerSocket;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	private MPPlayer self;
	private MPPlayer oponent;
    
	public Server(MPPlayer inSelf, MPPlayer inOponent)
	{
		this.self = inSelf;
		this.oponent = inOponent;
	}
	public void run()
    {
		while(true)
		{
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(9997, 10);
            //2. Wait for connection
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
            //4. The two parts communicate via the input and output streams
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("Server>> " + message);
                    String[] parts = message.split("-");
                    oponent.changeX(Float.parseFloat(parts[0]));
                    String x = Float.toString(self.getX());
                    String y = Float.toString(self.getY());
                    sendMessage(x + "-" + y);
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!message.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
		}
    }
    private void sendMessage(String inMessage)
    {
        try{
            out.writeObject(inMessage);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
