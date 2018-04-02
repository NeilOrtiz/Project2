package SysFile;


public class Fserver {

    private Parent dad;
    private CommunicationHandler cH;
    private Chunk chunk;
    private String pathFile;
    private Sender sender;

    public Fserver(Parent dad, CommunicationHandler cH) {

        this.dad=dad;
        this.cH=cH;
        this.chunk=new Chunk();
        this.pathFile=dad.folder.getPath();
        this.sender=new Sender();
    }

    public void execute(){
        //HearBeat
        HeartBeat heart = new HeartBeat(dad.myID,dad,cH,dad.folder);
        heart.start();
    }

    public boolean appendChunk(int appended_size,String fileName) {
        boolean success;
    
        success=chunk.append(appended_size, fileName, pathFile);

        return success;
    }

    public void newChunk(String fileName){
        chunk.create(dad.folder.getPath(), fileName, 0);
    }

    public void readChunk(){
        //TODO: Fserver.readChunk()
    }

    public void newMsgMserver(String msg){
        String requestType,fileName;

        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];

        if (requestType.equals("creation")){
            this.newChunk(fileName);
        }


    }

    public void newMsgClient (String msg) {
        String requestType,fileName,appendSize,serverId,sourceId;
        int appended_size,destID;
        sourceId=msg.split(";")[1];
        destID=Integer.parseInt(sourceId);
        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];
        appendSize=msg.split(";")[5];
        serverId=msg.split(";")[4];
        appended_size=Integer.parseInt(appendSize);

        if (requestType.equals("append")){
            boolean success;
            success=this.appendChunk(appended_size,fileName);
            // Notification to Client

            if (success){
                msg=dad.typeHost+";"+dad.myID+";"+"successAppend"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            } else {
                msg=dad.typeHost+";"+dad.myID+";"+"failedAppend"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            }
            sender.sendMessage(msg, cH.peers_listen, destID);
        }
        
    }
}
