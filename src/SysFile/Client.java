package SysFile;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private String msg;
    private Parent dad;
    private Sender sender;
    private CommunicationHandler cH;


    public Client(Parent dad,CommunicationHandler cH){
        this.msg=null;
        this.dad=dad;
        this.sender=new Sender();
        this.cH=cH;

    }

    public void execute() {

        Scanner choice = new Scanner(System.in);
        Console console = System.console();
        String newFileName,appendSize,offset; 
            int choiceEntry=-1;

            while (choiceEntry!=4) {

                while (choiceEntry<1 || choiceEntry >5) {

                    System.out.println("Enter one of the following commands:");
                    System.out.println("1 - Create");
                    System.out.println("2 - Read");
                    System.out.println("3 - Append");
                    System.out.println("4 - Exit");
                    System.out.println("5 - Query");
                    System.out.println();
                    if (choice.hasNextInt()) {

                        choiceEntry=choice.nextInt();
                        if (choiceEntry<1 || choiceEntry >5) {
                            System.out.println("Invalidad input");
                        }
                        
                    }
                }

                switch (choiceEntry) {
                    case 1:
                        System.out.println("Creating");
                        System.out.println();
                        newFileName=console.readLine("Enter new File Name: ");
                        msg=dad.typeHost+";"+dad.myID+";"+"creation"+";"+newFileName;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        break;

                    case 2:
                        System.out.println("Reading");
                        newFileName=console.readLine("Enter new File Name: ");
                        offset=console.readLine("Enter offset: ");
                        msg=dad.typeHost+";"+dad.myID+";"+"read"+";"+newFileName+";"+0+";"+0+";"+offset;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        System.out.println();
                        break;

                    case 3:
                        System.out.println("Appending");
                        System.out.println();
                        newFileName=console.readLine("Enter new File Name: ");
                        appendSize=console.readLine("Enter append size: ");
                        msg=dad.typeHost+";"+dad.myID+";"+"append"+";"+newFileName+";"+0+";"+appendSize+";"+0;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        break;

                    case 4:
                        System.out.println("Saliendo");
                        choice.close();
                        System.exit(1);
                        break;
                    
                    case 5:
                        System.out.println("Gathering information...");
                        msg=dad.typeHost+";"+dad.myID+";"+"ls"+";"+0+";"+0+";"+0+";"+0;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        break;
                    
                    default:
                        break;
                }
                choiceEntry=-1;
            }
            choice.close();

    }

    public void newMsgMserver(String msg) {
        String requestType,fileName,serverId,appendSize,datas;
        int destID,startOffset,endOffset;
        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        datas=msg.split(";")[3];
        serverId=msg.split(";")[4];
        destID=Integer.parseInt(serverId);
        appendSize=msg.split(";")[5];
        //startOffset=Integer.parseInt(da);


        if (requestType.equals("AnswerAppend")) {
            System.out.println("[INFO] File: "+fileName+" is located in F-server "+serverId);
            msg=dad.typeHost+";"+dad.myID+";"+"append"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            sender.sendMessage(msg, cH.peers_listen, destID);
        } else if (requestType.equals("AnswerLs")) {

            Mserver mserver=new Mserver(dad, cH);
            ArrayList<String> result = new ArrayList<String>();
            
            result=mserver.procesDatas(datas);
            
            System.out.println("    -File list: ");
            for (String key:result) {
                System.out.println("        > "+key.split("-")[0]+": "+key.split("-")[1]+" bytes");
            }
        } else if (requestType.equals("AnswerRead")) {
            System.out.println("[INFO] Lectura: "+datas);
            System.out.println("[INFO] File: "+fileName+" is located in F-server "+serverId);
            msg=dad.typeHost+";"+dad.myID+";"+"read"+";"+fileName+";"+serverId+";"+0+";"+0;
            
        }
    }

    public void newMsgFserver(String msg) {
        String requestType,fileName,appendSize;

        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        appendSize=msg.split(";")[5];

        if (requestType.equals("successAppend")) {
            System.out.println("[NOTI] "+appendSize+" bytes has been append in "+fileName+".");
        } else if (requestType.equals("failedAppend")) {
            System.out.println("[ERROR] "+appendSize+" bytes has NOT been append in "+fileName+".");
        }

        
    }
}