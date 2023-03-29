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

                int serverID = 0;
                int serverCount = 1;
                
                while(true){ 
                    
                    sendMessage("REDY", dout);
             
                    //-receiving job data and adding it on to string
                    String check2 = receiveMessage(din);
                    String[] jobInfo = check2.split(" "); // RVCD JOB INFO
                    System.out.println("jobinfo" + jobInfo[0]);
                    
                    if(jobInfo[0].equals("NONE")){ 
                        break;
                    }

                    int jobID = Integer.parseInt(jobInfo[2]); // get job ID which is JOBN jobID YY
                    sendMessage("GETS All", dout);
               
                    String str = receiveMessage(din); // RCV  DATA
    
                    sendMessage("OK", dout);
            
                    String[] serverInfoList = str.split(" ");
    
                    int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)

                    String serverType = " ";
            
                    int core = 0;
            
                     //get largest server type

                    for(int i = 0; i < serverNumber; i++){
                        //loop x times and by the x time, you get the largest server type and server ID
                        str = receiveMessage(din); //DATA outputed one by one 
                        String[] serverData = str.split(" ");
                        System.out.println("str = " + str);
                      
                        if(core < Integer.parseInt(serverData[4])){
                            serverCount = 1;   
                            core = Integer.parseInt(serverData[4]);
                            serverID = Integer.parseInt(serverData[1]);
                            if(serverType.equals(serverData[0])){
                                serverCount ++;    
                                System.out.println("reaches");
                            }
                            serverType = serverData[0];
                        } 
                        else if(serverType.equals(serverData[0])){
                            serverCount ++;    
                        }
                     }
                     System.out.println("ServerType = " + serverType + " core = " + core);
                     System.out.println("serverID = " + serverID);
                     System.out.println("serverCount = " + serverCount);

                     
                    System.out.println("serverID = " + serverID);


                    serverID%=serverCount; 

                    sendMessage("OK", dout);
    
                    receiveMessage(din); //RCV " . "

                    //SCHD 1 job if JOBN
                    if(jobInfo[0].equals("JOBN")){
                        dout.write(("SCHD " + jobID + " " + serverType + " " + serverID +"\n").getBytes());
                        dout.flush();
                        receiveMessage(din);
                        System.out.println("SCHD jobID: " + jobID + " to Server: " + serverType + " : " + serverID);
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
        }
    
        // Function below receivers message from server and prints it out on terminal
        static String receiveMessage(BufferedReader din) throws IOException{
            String str = (String)din.readLine();
            return str;
    
        }

 }
    
    