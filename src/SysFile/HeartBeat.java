package SysFile;


public class HeartBeat extends Thread {

    private int myID;
    private Parent dad;
    private Sender sender ;
    private CommunicationHandler cH;

    public HeartBeat (int myID,Parent dad, CommunicationHandler cH) {
        super("Receiver");
        this.dad=dad;
        this.myID=myID;
        this.sender= new Sender();
        this.cH=cH;

    }

    @Override
    public void run() {
        String msg;

        while (dad.listening) {

            // do something
            msg=this.generateMsg();
            sender.sendMessage(msg, cH.peers_listen,10);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getStackTrace());
            }
            

        }

        //System.out.println("...thump thump... from server: "+myID);

    }

    public String generateMsg(){

        String datas=this.enquiry();

        String msg=this.myID+","+"hb"+","+datas;

        return msg;
    }

    public String enquiry(){
        String update=null;

        update="Holas";
        //TODO: HeartBeat.enquiry()
        return update;

    }
}