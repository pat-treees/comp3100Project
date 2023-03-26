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
    
                sendMessage("HELO", dout);
    
                receiveMessage(din);
    
                sendMessage("AUTH 46370633", dout);
    
                receiveMessage(din);
                
                sendMessage("REDY", dout);
             
                String str = receiveMessage(din);
    
                String[] jobInfo = str.split(" ");
                int jobID = Integer.parseInt(jobInfo[2]); // get job ID which is JOBN jobID YY
                System.out.println("jobID: " + jobID);
                
                sendMessage("GETS All", dout);
               
                str = receiveMessage(din);
    
                sendMessage("OK", dout);
            
                String[] serverInfoList = str.split(" ");
    
                int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)
                System.out.println("Server number: " + serverNumber);
    
                for (int i = 0; i < serverNumber; i++){
                   str = receiveMessage(din);
                 //loop x times and by the x time, you get the largest server type and server ID
                }
    
                //extract ServerType and ServerID from data presented by server 
                String[] serverData = str.split(" ");
                String serverType = serverData[0];
                int serverID = Integer.parseInt(serverData[1]);
    
                sendMessage("OK", dout);
    
                receiveMessage(din);
    
                System.out.println("SCHD " + jobID + " " + serverType + " " + serverID +"\n");
    
               //SCHD 1 job 
                dout.write(("SCHD " + jobID + " " + serverType + " " + serverID +"\n").getBytes());
                dout.flush();
            
                receiveMessage(din);
    
                sendMessage("QUIT", dout);
    
                
                din.close();
                dout.close();
                s.close();
            
                }
    
            catch(Exception e){System.out.println(e);
            }
         
         
        }
    
        // Function below sends message to server and prints it out on terminal
        static void sendMessage(String str, DataOutputStream dout) throws IOException{ 
            dout.write((str + "\n").getBytes());
            dout.flush();
            System.out.println("SENT: " + str);
        }
    
        // Function below receivers message from server and prints it out on terminal
        static String receiveMessage(BufferedReader din) throws IOException{
            String str = (String)din.readLine();
            System.out.println("RVCD: " + str);
            return str;
    
        }

 }
    
    