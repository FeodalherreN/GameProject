package com.jagers.spelet.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.jagers.spelet.models.MPPlayer;

public class Server implements Runnable {
	
	private ServerSocket providerSocket;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private float message[];
	private MPPlayer self;
	private MPPlayer opponent;
    
	public Server(MPPlayer inSelf, MPPlayer inOponent)
	{
		this.self = inSelf;
		this.opponent = inOponent;
	}
	
	public void CloseServer() throws IOException
	{
		try {
			if(in!=null)
				in.close();
			if(out!=null)
				out.close();
			} catch (IOException e1) {
			e1.printStackTrace();
			}
			if(!providerSocket.isClosed())
				providerSocket.close();
	}
	@Override
	public void run()
    {
		while(!Thread.currentThread().isInterrupted())
		{
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket();
            providerSocket.setReuseAddress(true);
            providerSocket.bind(new InetSocketAddress(9996));
            //2. Wait for connection
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            float[] fli = new float[] {3, 4};
            sendMessage(fli);
            //4. The two parts communicate via the input and output streams
            do{
                try{
                    message = (float[])in.readObject();
                    opponent.changeX(message[0]);
                    opponent.changeY(message[1]);
                    System.out.println("Server>> " + message[0] + " " + message[1]);
                    float[] fl = new float[] {self.getX(), self.getY()};
                    sendMessage(fl);
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!Thread.currentThread().isInterrupted());
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
        		try {
        			if(in!=null)
        				in.close();
        			if(out!=null)
        				out.close();
        			} catch (IOException e1) {
        			e1.printStackTrace();
        			}
        			if(!providerSocket.isClosed())
        				providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
		}
    }
    private void sendMessage(float[] inMessage)
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
