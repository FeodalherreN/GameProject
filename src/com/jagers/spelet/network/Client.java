package com.jagers.spelet.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.jagers.spelet.models.MPPlayer;

public class Client implements Runnable {
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private float[] message;
	private String ip;
	private MPPlayer self;
	private MPPlayer opponent;
	
    public Client(String inIp, MPPlayer inSelf, MPPlayer inOponent)
    {
		this.self = inSelf;
		this.opponent = inOponent;
    	this.ip = inIp;
    }
    public void run()
    {
    	while(true)
    	{
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket(ip, 9996);
            requestSocket.setReuseAddress(true);
            System.out.println("Connected to localhost in port 9999");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            //3: Communicating with the server
            do{
                try{
                    message = (float[])in.readObject();
                    opponent.changeX(message[0]);
                    opponent.changeY(message[1]);
                    System.out.println("Client>> " + message[0] + " " + message[1]);
                    float[] fl = new float[]{self.getX(), self.getY()};
                    sendMessage(fl);
                }
                catch(ClassNotFoundException classNot){
                    System.err.println("data received in unknown format");
                }
            }while(true);
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    	}
    }
    private void sendMessage(float[] Coordinates)
    {
        try{
            out.writeObject(Coordinates);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
