package SysFile;


public class Fserver {

    private Parent dad;
    private CommunicationHandler cH;

    public Fserver(Parent dad, CommunicationHandler cH) {

        this.dad=dad;
        this.cH=cH;
    }

    public void execute(){

        HeartBeat heart = new HeartBeat(dad.myID,dad,cH,dad.folder);
        heart.start();

    }
}
