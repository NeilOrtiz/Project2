package SysFile;

public class CommunicationHandler {
    private Parent dad;
    private int myID;

    public CommunicationHandler(Parent dad, int myID) {
        this.dad=dad;
        this.myID=myID;
    }

    public boolean estComm(String typeHost) {
        boolean success=false;

        System.out.println("Estableciendo communicaciones...");
        success=true;
        //TODO: CH.estComm()

        return success;

    }
}