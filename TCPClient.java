import java.net.Socket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class TCPClient {
    /**
     *  * @param args
     *  */
        public static void main(String[] args) {
            try {
                InetAddress aHost = InetAddress.getByName(args[0]);
                int aPort = Integer.parseInt(args[1]);
                Socket s = new Socket(aHost, aPort);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
                
                System.out.println("Target IP: " + s.getInetAddress() + "Target Port: "+ s.getPort());
                System.out.println("Local IP: " + s.getLocalAddress() + "Local Port: " + s.getLocalPort());
                String username = System.getProperty("user.name");

                sendMessage("HELO", dout);
    
                receiveMessage(din); //RCVD OK
    
                sendMessage("AUTH " + username, dout);
    
                receiveMessage(din); // RCVD OK

                int serverID = 0;
                int serverCount = 1;
                int flag = 1;
                String serverType = " ";
                int core = 0;
                
                while(true){ 
                    
                    sendMessage("REDY", dout);
             
                    //-receiving job data and adding it on to string
                    String jobInfo = receiveMessage(din);
                    String[] jobs = jobInfo.split(" "); // RVCD JOB INFO
                    
                    if(jobs[0].equals("NONE")){ 
                        break;
                    }

                    int jobID = Integer.parseInt(jobs[2]); // get job ID which is JOBN jobID YY
                   
                    sendMessage("GETS All", dout);
               
                    String str = receiveMessage(din); // RCV  DATA
    
                    sendMessage("OK", dout);
            
                    String[] serverInfoList = str.split(" ");
    
                    int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)
            
                     //get largest server type
                     
                    for(int i = 0; i < serverNumber; i++){
                         //loop x times and by the x time, you get the largest server type and server ID
                        str = receiveMessage(din); //DATA outputed one by one 

                      /* */  if (flag == 1){
                            String[] serverData = str.split(" ");
                            if(core < Integer.parseInt(serverData[4])){
                                serverCount = 1;   
                                core = Integer.parseInt(serverData[4]);
                                serverID = Integer.parseInt(serverData[1]);                                    
                                serverType = serverData[0];
                                }
                                else if(serverType.equals(serverData[0])){
                                serverCount ++;    
                            }
                        }
                    }

                    flag = 0;

                    sendMessage("OK", dout);
    
                    receiveMessage(din); //RCV " . "

                    //SCHD 1 job if JOBN

                    if(jobs[0].equals("JOBN")){
                        serverID%=serverCount; 

                        dout.write(("SCHD " + jobID + " " + serverType + " " + serverID +"\n").getBytes());
                        dout.flush();
                        receiveMessage(din);
                        System.out.println("SCHD jobID: " + jobID + " to Server: " + serverType + " : " + serverID);
                        serverID++;
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
    static void sendMessage(String str, DataOutputStream dout) throws IOException {
        dout.write((str + "\n").getBytes());
        dout.flush();
    }

    // Function below receivers message from server and prints it out on terminal
    static String receiveMessage(BufferedReader din) throws IOException {
        String str = (String) din.readLine();
        return str;

    }
        }