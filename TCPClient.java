import java.net.Socket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

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
    
                receiveMessage(din); //RCVD OK
    
                sendMessage("AUTH 46370633", dout);
    
                receiveMessage(din); // RCVD OK

                while(true){ 
                    
                    sendMessage("REDY", dout);
             
                    //receiving job data and adding it on to string
                String check2 = receiveMessage(din);
                String[] jobInfo = check2.split(" "); // RVCD JOB INFO

                    if(check2.equals("NONE")){ 
                        break;
                    }
                
                    System.out.println("jobs: " + jobInfo[0]);
                    int jobID = Integer.parseInt(jobInfo[2]); // get job ID which is JOBN jobID YY
                    System.out.println("jobID: " + jobID);
                
                    sendMessage("GETS All", dout);
               
                    String str = receiveMessage(din); // RCV  DATA
    
                    sendMessage("OK", dout);
            
                    String[] serverInfoList = str.split(" ");
    
                    int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)
                    System.out.println("Server number: " + serverNumber);
    
                    
                    for (int i = 0; i < serverNumber; i++){ //loop x times and by the x time, you get the largest server type and server ID
                        str = receiveMessage(din); //DATA outputed one by one
                    }
    
                    //extract ServerType and ServerID from data presented by server 
                    String[] serverData = str.split(" ");
                    String serverType = serverData[0];
                    int serverID = Integer.parseInt(serverData[1]);
    
                    sendMessage("OK", dout);
    
                    receiveMessage(din); //RCV " . "
    
                    //SCHD 1 job if JOBN
                    if(jobInfo[0].equals("JOBN")){
                        dout.write(("SCHD " + jobID + " " + serverType + " " + serverID +"\n").getBytes());
                        System.out.println("SCHD jobID: " + jobID + " to " + "serverType: " + serverType);
                        dout.flush();
                        receiveMessage(din);
                    } else {
                        System.out.println("[Job: " + jobInfo[0] + "] cannot be scheduled");
                    }
                   
                }   
    
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
    
    