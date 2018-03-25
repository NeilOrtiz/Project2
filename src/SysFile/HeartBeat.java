package SysFile;


public class HeartBeat extends Thread {

    private int myID;

    public HeartBeat (int myID) {
        super("Receiver");
        this.myID=myID;

    }

    @Override
    public void run() {

        System.out.println("...thump thump... from server: "+myID);

    }
}