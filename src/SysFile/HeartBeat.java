package SysFile;


public class HeartBeat extends Thread {

    private int myID;
    private Parent dad;

    public HeartBeat (int myID,Parent dad) {
        super("Receiver");
        this.dad=dad;
        this.myID=myID;

    }

    @Override
    public void run() {

        while (dad.listening) {

        }

        //System.out.println("...thump thump... from server: "+myID);

    }
}