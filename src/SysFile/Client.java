package SysFile;

import java.io.Console;
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

        System.out.println("********** Executando desde Client.java -***********");
        Scanner choice = new Scanner(System.in);
        Console console = System.console();
        String newFileName,appendSize; 
            int choiceEntry=-1;

            while (choiceEntry!=4) {

                while (choiceEntry<1 || choiceEntry >4) {

                    System.out.println("Enter one of the following commands:");
                    System.out.println("1 - Create");
                    System.out.println("2 - Read");
                    System.out.println("3 - Append");
                    System.out.println("4 - Exit");
                    System.out.println();
                    if (choice.hasNextInt()) {

                        choiceEntry=choice.nextInt();
                        if (choiceEntry<1 || choiceEntry >4) {
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
                
                    default:
                        break;
                }
                choiceEntry=-1;
            }
            choice.close();

    }

    public void newMsgMserver(String msg) {
        String requestType,fileName,serverId,appendSize;
        int destID;
        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        serverId=msg.split(";")[4];
        destID=Integer.parseInt(serverId);
        appendSize=msg.split(";")[5];

        if (requestType.equals("AnswerAppend")) {
            System.out.println("[INFO] File: "+fileName+" is located in F-server "+serverId);
            msg=dad.typeHost+";"+dad.myID+";"+"append"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            sender.sendMessage(msg, cH.peers_listen, destID);
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