package SysFile;

import java.io.File;

public class HeartBeat extends Thread {

    private int myID;
    private Parent dad;
    private Sender sender ;
    private CommunicationHandler cH;
    private File folder;

    public HeartBeat (int myID,Parent dad, CommunicationHandler cH,File folder) {
        super("Receiver");
        this.dad=dad;
        this.myID=myID;
        this.sender= new Sender();
        this.cH=cH;
        this.folder=folder;

    }

    @Override
    public void run() {
        String msg;

        while (dad.listening) {

            // do something
            msg=this.generateMsg(this.folder);
            sender.sendMessage(msg, cH.peers_listen,10);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getStackTrace());
            }
            

        }

        //System.out.println("...thump thump... from server: "+myID);

    }

    public String generateMsg(File folder){

        String datas=this.enquiry(folder);

        String msg=this.myID+","+"hb"+","+datas;

        return msg;
    }

    public String enquiry(File folder){
        String update=null;
        File[] filesFolder = folder.listFiles();

        for (File file:filesFolder) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }

        update="Holas";
        //TODO: HeartBeat.enquiry()
        return update;

    }
}