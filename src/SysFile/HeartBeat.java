package SysFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HeartBeat extends Thread {

    private Parent dad;
    private Sender sender ;
    private CommunicationHandler cH;
    private File folder;
    private List<String> setChunks;
    private List<String> chunks;

    public HeartBeat (Parent dad, CommunicationHandler cH,File folder) {
        super("Receiver");
        this.dad=dad;
        this.sender= new Sender();
        this.cH=cH;
        this.folder=folder;
        this.setChunks=new ArrayList<String>();
        this.chunks=new ArrayList<String>();

    }

    @Override
    public void run() {
        String msg;

        while (dad.listening) {
            
            msg=this.generateMsg(this.folder);
            sender.sendMessage(msg, cH.peers_listen,10);
            System.out.println("[INFO] Sending heartbeat datas: "+msg.split(";")[3]);
            System.out.println("");
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
        String msg=dad.typeHost+";"+dad.myID+";"+"hb"+";"+datas;
        //String msg=this.myID+";"+"hb"+";"+datas;

        return msg;
    }

    public String enquiry(File folder){
        String update=null;
        File[] filesFolder = folder.listFiles();

        for (File file:filesFolder) {
            if (file.isFile()) {
                this.chunks.add(file.getName()+"-"+String.valueOf(file.lastModified())+"-"+String.valueOf(file.length()));
                this.setChunks.add(chunks.toString());
                this.chunks.clear();
            }
        }

        update=setChunks.toString();
        // update=chunks.toString();
        setChunks.clear();

        return update;

    }
}