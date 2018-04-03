package SysFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import java.util.Random;
import java.util.Set;

public class Mserver {
    private Parent dad;
    private CommunicationHandler cH;
    private Sender sender ;
    

    public Mserver(Parent dad, CommunicationHandler cH){
        this.dad=dad;
        this.cH=cH;
        this.sender= new Sender();
    }

    public void execute(){
        System.out.println("This is the M-server");
    }

    public void newMsgClient(String msg){
        String requestType,fileName,appendSize;
        int serverId,destID;

        requestType=msg.split(";")[2];
        fileName=msg.split(";")[3];


        if (requestType.equals("creation")){
            //Select random Fserver. Get dstID
            serverId=this.randomFserver();
            msg=dad.typeHost+";"+dad.myID+";"+"creation"+";"+fileName+";"+serverId+";"+0+";"+0;

            // send request to Fserver
            sender.sendMessage(msg, cH.peers_listen, serverId);
        } else if (requestType.equals("append")) {
            destID=Integer.parseInt( msg.split(";")[1]); // Get Client ID who sent the request
            appendSize=msg.split(";")[5];
            serverId=checkFserver(fileName);
            msg=dad.typeHost+";"+dad.myID+";"+"AnswerAppend"+";"+fileName+";"+serverId+";"+appendSize+";"+0;
            sender.sendMessage(msg, cH.peers_listen, destID);
        }
    }

    public void newMsgFserver(String msg){
        String requestType;
        requestType=msg.split(";")[2];

        if (requestType.equals("hb")) {
            this.update(msg);
        }
    }

    public int randomFserver(){
        int answer=11;

        int max=13;
        int min=11;
        int diff=max-min;
        Random rn = new Random();
        answer=rn.nextInt(diff+1)+min;
        System.out.println("F-server selected: "+answer);

        return answer;
    }

    public int checkFserver(String fileName){
        int serverId;

        //TODO: Mserver.checkFserver()
        serverId=13;

        return serverId;

    }

    public void update(String msg) {
        String datas,serverId,fileName,time,chunkNs;
        ArrayList<String> data;
        int chunkN;

        datas=msg.split(";")[3];
        serverId=msg.split(";")[1];
        data=this.procesDatas(datas);
        

        for (String dt:data) {
                fileName=dt.split("_")[0];
                // System.out.println("fileName: "+fileName);
                time=dt.split("-")[1];
                // System.out.println("time: "+time);
                chunkNs=dt.split("_")[2];
                chunkNs=chunkNs.split("\\.")[0];
                chunkN=Integer.parseInt(chunkNs);
                // System.out.println("chunkN: "+chunkN);

                this.checkMeta(fileName, time, chunkN,serverId);
        }

    }

    public void checkMeta(String fileName,String time,int chunkN, String serverId) {
        String value;
        int metachunkN, metaTime;
        ArrayList<String> dataFile= new ArrayList<String>(); 

        System.out.println("[checkMeta3] Existe el fileName: "+dad.metadata.contains(fileName));
        if (dad.metadata.contains(fileName)) {
            dataFile=this.metadata.get(fileName);
            System.out.println("[checkMeta2] dataFile: "+dataFile);
            metachunkN=Integer.parseInt(dataFile.get(0));
            metaTime=Integer.parseInt(dataFile.get(1));

            if (metachunkN==chunkN) {
                if (metaTime==Integer.parseInt(time)) {
                    dataFile.remove(chunkN);
                    value=serverId+"-"+time;
                    dataFile.add(chunkN, value);
                    this.metadata.put(fileName, dataFile);
                    //dataFile.clear();
                }

            } else {
                value=serverId+"-"+time;
                dataFile.add(chunkN, value);
                this.metadata.put(fileName, dataFile);
                //dataFile.clear();
                
            }

        } else {
            value=serverId+"-"+time;
            // System.out.println("[checkMeta] value: "+value);
            // System.out.println("[checkMeta] chunkN: "+chunkN);

            for (int i=0;i<=5;i++){
                dataFile.add(i, null);
            }

            dataFile.add(chunkN, value);
            
            this.metadata.put(fileName, dataFile);
            //System.out.println("[checkMeta] key: "+fileName+", value: "+this.metadata.get(fileName));
            //dataFile.clear();
        }
        this.metadataStatus();


    }

    public ArrayList<String> procesDatas(String datas){
        ArrayList<String> result = new ArrayList<String>();
        String[] data=datas.split(",");
        for (String nx:data) {
            String n1=nx.split("]",1)[0];
			n1=n1.replaceAll("]", "");
			n1=n1.replaceAll("\\[", "");
            n1=n1.replaceAll("\\s+", "");
            result.add(n1);
        }
        return result;
    }

    public void metadataStatus(){
        Set<String> keys=this.metadata.keySet();
        System.out.println("Metadata size: "+this.metadata.size());

        for (String key:keys){
            Collection<ArrayList<String>> values = this.metadata.values();
            System.out.println("Metadata FileName: "+key+", value: "+this.metadata.get(key));
            // for (ArrayList<String> value:values){
            //     System.out.println("Metadata FileName: "+key+", value: "+value);
            // }
        }

        


    }
}