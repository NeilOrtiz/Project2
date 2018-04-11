package SysFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Parent {

    public int myID;
    public String typeHost;
    public boolean listening;
    public File folder;
    public Hashtable<String,ArrayList<String>> metadata;
    public  Lock mutex1;

    public Parent(int myID,String typeHost) {
        this.myID=myID;
        this.typeHost=typeHost;
        this.listening=true;
        this.folder=null;
        this.metadata=new Hashtable<String,ArrayList<String>>();
        this.mutex1=new ReentrantLock(true);
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
            //Hashtable<String,ArrayList<String>> metadata=new Hashtable<String,ArrayList<String> >();

            Mserver mserver=new Mserver(dad,cH);

            boolean success= cH.estComm(typeHost);
            if (success==true) {
                System.out.println("M-server Online");
            }

            mserver.execute();

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

            Client client=new Client(dad,cH);
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