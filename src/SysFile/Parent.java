package SysFile;

import java.io.File;
import java.util.Scanner;

public class Parent {

    public int myID;
    public String typeHost;
    public boolean listening;
    public File folder;

    public Parent(int myID,String typeHost) {
        this.myID=myID;
        this.typeHost=typeHost;
        this.listening=true;
        this.folder=null;
    }

    public static void main (String[] args) {

        if (args.length!=2) {
			System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }
        
        int myID=Integer.parseInt(args[0]);
        String typeHost = args[1];
        Parent dad = new Parent(myID,typeHost);
        CommunicationHandler cH=new CommunicationHandler(dad, myID);
        
        
        if (args[1].equals("M")) {

            System.out.println("This is the M-server");

            boolean success= cH.estComm(typeHost);
            if (success==true) {
                System.out.println("M-server Online");
            }

        } else if (args[1].equals("s")) {

            dad.getFolderPath(myID);
            Fserver fserver = new Fserver(dad, cH);

            // System.out.println("This is the file-server "+args[0]);
            boolean success= cH.estComm(typeHost);
            if (success==true) {
                System.out.println("F-server "+myID+ " is Online");
            }

            fserver.execute();
        
        } else if (args[1].equals("c")) {

            Client client=new Client();
            System.out.println("This is the client "+args[0]);

            boolean success= cH.estComm(typeHost);
            if (success==true) {
                System.out.println("Client "+myID+ " is Online");
            }

            client.execute();


            

            

        } else {

            System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }

    }

    public void terminate() {
        this.listening=false;
    }

    public void getFolderPath(int serverId){

        switch (serverId) {
            case 11:
                this.folder=new File("./Server11/");
                break;

            case 12:
                this.folder=new File("./Server12/");
                break;

            case 13:
                this.folder=new File("./Server13/");
                break;
        
            default:
                System.err.println("Usage: java -jar Project2/dist/Parent.jar <11|12|13> <s>");
                System.exit(1);
                break;
        }

    }
} 