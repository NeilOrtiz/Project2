package SysFile;

import java.io.IOException;
import java.util.ArrayList;

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
        HeartBeat heart = new HeartBeat(dad,cH,dad.folder);
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

    public ArrayList<String> readChunk(String chunkName, int startOffset, int endOffset) throws IOException{
        ArrayList<String> reading = new ArrayList<String>();
        reading = chunk.read(chunkName, dad.folder.getPath(), startOffset, endOffset);
        return reading;
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
        String requestType,fileName,appendSize,serverId,sourceId,datas,chunkName;
        int appended_size,destID,chunk,startOffset,endOffset;
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
        } else if (requestType.equals("read")) {
            datas=msg.split(";")[6];
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<String> reading = new ArrayList<String>();
            Mserver mserver=new Mserver(dad, cH);
            result=mserver.procesDatas(datas);
            System.out.println("[newMsgClient] result: "+result);

            for (String key:result) {
                chunk=Integer.parseInt(key.split("-")[0]);
                startOffset=Integer.parseInt(key.split("-")[1]);
                endOffset=Integer.parseInt(key.split("-")[2]);
                chunkName=fileName+"_data_"+chunk+".bin";
                System.out.println("[newMsgClient] chunkName: "+chunkName);
                System.out.println("[newMsgClient] filename: "+fileName+"_data_"+chunk+".bin"+", OffsetStar: "+startOffset+", OffsetEnd: "+endOffset);

                try {
                    reading.add(this.readChunk(chunkName, startOffset, endOffset).toString());
                    System.out.println("[newMsgClient] reading: "+reading);
                    
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
            msg=dad.typeHost+";"+dad.myID+";"+"resultRead"+";"+fileName+";"+serverId+";"+0+";"+reading+";"+datas;
            sender.sendMessage(msg, cH.peers_listen, destID);
            
        }
        
    }
}
