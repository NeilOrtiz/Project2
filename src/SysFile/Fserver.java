package SysFile;

import java.io.File;

public class Fserver {

    private int myID;
    private Parent dad;
    private CommunicationHandler cH;
    private File folder;

    public Fserver(int myID,Parent dad, CommunicationHandler cH,File folder) {

        this.dad=dad;
        this.myID=myID;
        this.cH=cH;
        this.folder=folder;
    }

    public void execute(){

    }
}
