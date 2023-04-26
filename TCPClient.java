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
                int flag = 1;
                String serverType = " ";
                
                while(true){ 
                    
                    sendMessage("REDY", dout);
             
                    //-receiving job data and adding it on to string
                    String jobInfo = receiveMessage(din);
                    String[] jobs = jobInfo.split(" "); // RVCD JOB INFO
                    
                    if(jobs[0].equals("NONE")){ 
                        break;
                    }

                    if(jobs[0].equals("JOBN")){

                    int jobID = Integer.parseInt(jobs[2]); // get job ID which is JOBN jobID YY

                    int jobCore = Integer.parseInt(jobs[4]);
                    int jobRAM = Integer.parseInt(jobs[5]);
                    int jobDisk = Integer.parseInt(jobs[6]);
                    sendMessage("GETS Capable " + jobCore + " " + jobRAM + " " + jobDisk, dout);
               
                    String str = receiveMessage(din); // RCV full server DATA
    
                    sendMessage("OK", dout);
            
                    String[] serverInfoList = str.split(" ");
    
                    int serverNumber = Integer.parseInt(serverInfoList[1]); //get the X (server type)
            
                     //record all capable servers one by one
                    for(int i = 0; i < serverNumber; i++){
                         //loop x times and by the x time, you get the largest server type and server ID
                        str = receiveMessage(din); //DATA outputed one by one 
                        String[] serverData = str.split(" ");
                    System.out.print(flag);
                        if (flag == 1){
                            
                                serverID = Integer.parseInt(serverData[1]);  
                                serverType = serverData[0];                                  
                        }
                        flag = 0;
                    }

                    flag = 1;

                    sendMessage("OK", dout);
    
                    receiveMessage(din); //RCV " . "

                    //SCHD 1 job if JOBN
                        dout.write(("SCHD " + jobID + " " + serverType + " " + serverID +"\n").getBytes());
                        dout.flush();
                        receiveMessage(din);
                    
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