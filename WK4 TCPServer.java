// import java.io.Wruter;
import java.io.BufferedReader;
// import java.io.OutStreamerWriter;
// import java.net.InetAddress;
// import java.net.SocketException;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream; 
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.TimeUnit;

public class TCPServer {
	public static void main(String[] args) throws IOException {
        int aPort = Integer.parseInt(args[0]);
        System.out.println("Port Number: " + aPort);
        ServerSocket ss = new ServerSocket(aPort);
        while(true){
            try{
                Socket s = ss.accept();// establishes connection 
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                System.out.println("Target IP: " + s.getInetAddress() + " Target Port: " + s.getPort());
                System.out.println("Local IP: " + s.getLocalAddress() + " Local Port: " + s.getLocalPort());
                try{TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) {System.out.println(e);}

                String str = (String)in.readLine();
                System.out.println("RVCD: " +str);

                dout.write(("G'DAY\n").getBytes());
                System.out.println("SENT: G'DAY");

                str = (String)in.readLine();
                System.out.println("RVCD:" + str);

                dout.write(("BYE \n").getBytes());
                System.out.println("SENT: BYE");
        
                in.close();
                dout.close();
                s.close();
        }
        catch(Exception e){System.out.println(e);}
    }
}
        
}
