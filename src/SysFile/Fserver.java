package SysFile;


public class Fserver {

    private Parent dad;
    private CommunicationHandler cH;
    private Chunk chunk;

    public Fserver(Parent dad, CommunicationHandler cH) {

        this.dad=dad;
        this.cH=cH;
        this.chunk=new Chunk();
    }

    public void execute(){
        //HearBeat
        HeartBeat heart = new HeartBeat(dad.myID,dad,cH,dad.folder);
        heart.start();
    }

    public void appendChunk() {
        //TODO: Fserver.append()
    }

    public void newChunk(String fileName){
        chunk.create(dad.folder.getPath(), fileName, 0);
    }

    public void readChunk(){
        //TODO: Fserver.readChunk()
    }

    public void newMsgMserver(String msg){
        String requestType,fileName;

        requestType=msg.split(",")[2];
        fileName=msg.split(",")[3];

        if (requestType.equals("creation")){
            this.newChunk(fileName);
        }


    }
}
