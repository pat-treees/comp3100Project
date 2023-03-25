import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;
//Wk 5 - job dispatcher
public class TCPClient {
        /**
         * @param args
         */
        public static void main(String[] args) {
            
            try {
                InetAddress aHost = InetAddress.getByName(args[0]);
                int aPort = Integer.parseInt(args[1]);
                Socket s = new Socket(aHost, aPort);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));

                System.out.println("Target IP: " + s.getInetAddress() + "Target Port: "+ s.getPort());
                System.out.println("Local IP: " + s.getLocalAddress() + "Local Port: " + s.getLocalPort());

                dout.write(("HELO\n").getBytes());
                dout.flush();
                System.out.println("SENT: HELO");
 
                String str = (String)din.readLine();
                System.out.println("RVCD: " + str);

                dout.write(("AUTH 46370633\n").getBytes());
                dout.flush();
                System.out.println("SENT: AUTH 46370633");

                str = (String)din.readLine();
                System.out.println("RVCD: " + str);
                
                dout.write(("REDY\n").getBytes());
                dout.flush();
                System.out.println("SENT: REDY");
             
                str = (String)din.readLine();
                System.out.println("RVCD: " + str);
                
                dout.write(("GETS All\n").getBytes());
                dout.flush();
                System.out.println("SENT: GETS All");
               
                str = (String)din.readLine();
                System.out.println("RVCD: " + str);

         
                  String[] jobInfo = str.split(" ");
                int jobID = Integer.parseInt(jobInfo[1]); // get job ID which is JOBN jobID YY
                System.out.println("jobID: " + jobID);

                dout.write(("OK\n").getBytes());
                dout.flush();
                System.out.println("SENT: OK");
            
                String[] serverInfoList = str.split(" ");

                int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)
                System.out.println("Server number: " + serverNumber);

                for (int i = 0; i < serverNumber; i++){
                    str = (String)din.readLine();
                    System.out.println("RVCD: " + str);
                 //loop x times and by the x time, you get the largest server type and server ID
                }

                dout.write(("OK\n").getBytes());
                dout.flush();
                System.out.println("SENT: OK");

                str = (String)din.readLine();
                System.out.println("RVCD: " + str);

                //SCDH JOB TO LARGEST SERVER
                

                dout.write(("QUIT\n").getBytes());
                dout.flush();
                System.out.println("SENT: QUIT");

                
                din.close();
                dout.close();
                s.close();
            
                }

            catch(Exception e){System.out.println(e);
            }
         
        }

}




            

           
            

    

