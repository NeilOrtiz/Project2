package SysFile;

import java.io.Console;
import java.util.ArrayList;

public class Client {
    private String msg;
    private Parent dad;
    private Sender sender;
    private CommunicationHandler cH;
    private Mserver mserver;


    public Client(Parent dad,CommunicationHandler cH){
        this.msg=null;
        this.dad=dad;
        this.sender=new Sender();
        this.cH=cH;
        this.mserver=new Mserver(dad, cH);

    }

    public void execute() {

        dad.availability=true;
        Console console = System.console();
        String newFileName,appendSize,offset,arg,request,fileName; 
        boolean exit=false;

        System.out.println("");
        System.out.println("*******************************************************");
        System.out.println("                CS6378: PROJECT II");
        System.out.println("* File  Creation:    >> create <fileName>");
        System.out.println("* Append File   :    >> append <fileName> <appendSize>");
        System.out.println("* Read File     :    >> read <fileName> <offset>");
        System.out.println("* File list     :    >> ls");
        System.out.println("* Exit          :    >> exit");

        while(!exit) {
            System.out.println("");
            arg=console.readLine("[Project2]>> ");
            request=arg.split(" ")[0];
            if (dad.availability) {
                if (request.equals("exit")){
                    exit=true;
                } else if (request.equals("ls")) {
                    System.out.println("Gathering information...");
                    msg=dad.typeHost+";"+dad.myID+";"+"ls"+";"+0+";"+0+";"+0+";"+0;
                    sender.sendMessage(msg, cH.peers_listen, 10);
                    
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getStackTrace());
                    }

                } else if (request.equals("create")) {
                    

                    try {
                        newFileName=request=arg.split(" ")[1];
                        msg=dad.typeHost+";"+dad.myID+";"+"creation"+";"+newFileName;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        System.out.println("File "+newFileName+" created");
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.err.println("Usage: create <fileName>");
                    }
                } else if (request.equals("append")) {
                    
                    try {
                        fileName=arg.split(" ")[1];
                        try {
                            appendSize=arg.split(" ")[2];
                            int maxAppend=Integer.parseInt(appendSize);
                            if (maxAppend<=2048) {
                                msg=dad.typeHost+";"+dad.myID+";"+"append"+";"+fileName+";"+0+";"+appendSize+";"+0;
                                sender.sendMessage(msg, cH.peers_listen, 10);
                                System.out.println("Appending...");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getStackTrace());
                                }
                            } else {
                                System.err.println("[ERROR] Max amount of data that can be appended is 2048 bytes");
                            }
                            
                        } catch (ArrayIndexOutOfBoundsException|NumberFormatException ex) {
                            System.err.println("Usage: append <fileName> <appendSize>");
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.err.println("Usage: append <fileName> <appendSize>");
                    } 

                } else if (request.equals("read")) {
                    try {
                        
                        fileName=arg.split(" ")[1];

                        try {
                            offset=arg.split(" ")[2];
                            Integer.parseInt(offset);
                            msg=dad.typeHost+";"+dad.myID+";"+"read"+";"+fileName+";"+0+";"+0+";"+offset;
                            sender.sendMessage(msg, cH.peers_listen, 10);
                            //System.out.println("Reading...");
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                System.out.println(ex.getStackTrace());
                            }
        
                        } catch (ArrayIndexOutOfBoundsException|NumberFormatException ex) {
                            System.err.println("Usage: read <fileName> <offset>");
                        }

                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.err.println("Usage: read <fileName> <offset>");
                    }

                    
                    
                    

                } else {
                    System.err.println("Usage: <ls|create|append|read|exit> <fileName> <appendSize|offset>");
                }
            } else {
                System.err.println("System not available");
                if (request.equals("exit")){
                    exit=true;
                }
            }
        }
        System.exit(1);
    }

    public void newMsgMserver(String msg) {
        String requestType,fileName,serverId,appendSize,datas,offset;
        int destID;
        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        datas=msg.split(";")[3];
        serverId=msg.split(";")[4];
        destID=Integer.parseInt(serverId);
        appendSize=msg.split(";")[5];
        offset=msg.split(";")[6];
        //startOffset=Integer.parseInt(da);


        if (requestType.equals("AnswerAppend")) {
            System.out.println("[INFO] File: "+fileName+" last chunk is located in F-server "+serverId);
            msg=dad.typeHost+";"+dad.myID+";"+"append"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            sender.sendMessage(msg, cH.peers_listen, destID);
        } else if (requestType.equals("AnswerLs")) {

            ArrayList<String> result = new ArrayList<String>();
            
            result=mserver.procesDatas(datas);
            
            System.out.println("    -File list: ");
            for (String key:result) {
                System.out.println("        > "+key.split("-")[0]+": "+key.split("-")[1]+" bytes");
            }
        } else if (requestType.equals("AnswerRead")) {
            //System.out.println("[INFO] Lectura: "+offset);
            //System.out.println("[INFO] File: "+fileName+" is located in F-server "+serverId);
            msg=dad.typeHost+";"+dad.myID+";"+"read"+";"+fileName+";"+serverId+";"+0+";"+offset;
            
            if (destID==-1) {
                System.err.println("[ERROR] File doesn't exist");
            } else {
                if (offset.equals("[-1]")) {
                    System.err.println("[ERROR] Offset bigger than file size");
                } else if (offset.equals("[-2]")) {
                    System.err.println("[ERROR] Empty file");
                } else {
                    sender.sendMessage(msg, cH.peers_listen, destID);
                }
            }

            
        }
    }

    public void newMsgFserver(String msg) {
        String requestType,fileName,appendSize,datas;

        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        appendSize=msg.split(";")[5];

        if (requestType.equals("successAppend")) {
            System.out.println("[INFO] "+appendSize+" bytes has been append in "+fileName+".");
        } else if (requestType.equals("failedAppend")) {
            System.out.println("[ERROR] "+appendSize+" bytes has NOT been append in "+fileName+".");
        } else if (requestType.equals("resultRead")) {
            datas=msg.split(";")[6];
            //request=msg.split(";")[7];
            ArrayList<String> result = new ArrayList<String>();
            //ArrayList<String> requestA= new ArrayList<String>();
            //ArrayList<String> reading = new ArrayList<String>();
            result=mserver.procesDatas(datas);
            //requestA=mserver.procesDatas(request);
            //System.out.println("[newMsgFserver] result: "+result.toString());
            //System.out.println("[newMsgFserver] msg: "+msg);
            //System.out.println("[newMsgFserver] requestA: "+requestA);
            this.printReading(result);

        }  
    }

    public void printReading(ArrayList<String> result){
        int temp;
        System.out.print("Reading: ");
            for (String x:result){
                temp=Integer.parseInt(x);
                if (temp>=33 && temp<=126) {
                    System.out.print((char)temp);
                } else {
                    System.out.print("|");
                }
            }
            System.out.print(" END");
            System.out.println("");   
    }
}