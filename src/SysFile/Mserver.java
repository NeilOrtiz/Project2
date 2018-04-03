package SysFile;

import java.util.ArrayList;
import java.util.Hashtable;

import java.util.Random;

public class Mserver {
    private Parent dad;
    private CommunicationHandler cH;
    private Sender sender ;
    private Hashtable<String,ArrayList<String>> metadata;
    

    public Mserver(Parent dad, CommunicationHandler cH){
        this.dad=dad;
        this.cH=cH;
        this.sender= new Sender();
        this.metadata=new Hashtable<String,ArrayList<String> >();
        
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
        

    //     for (String dt:data) {
    //         System.out.println("---------*****-------- "+dt);
            
    // }

        for (String dt:data) {
                fileName=dt.split("_")[0];
                System.out.println("fileName: "+fileName);
                time=dt.split("-")[1];
                System.out.println("time: "+time);
                chunkNs=dt.split("_")[2];
                chunkNs=chunkNs.split("\\.")[0];
                chunkN=Integer.parseInt(chunkNs);
                System.out.println("chunkN: "+chunkN);

                this.checkMeta(fileName, time, chunkN,serverId);
        }

    }

    public void checkMeta(String fileName,String time,int chunkN, String serverId) {
        String value;
        int metachunkN, metaTime;
        ArrayList<String> dataFile= new ArrayList<String>();

        if (this.metadata.contains(fileName)) {
            dataFile=this.metadata.get(fileName);
            metachunkN=Integer.parseInt(dataFile.get(0));
            metaTime=Integer.parseInt(dataFile.get(1));

            if (metachunkN==chunkN) {
                if (metaTime==Integer.parseInt(time)) {
                    dataFile.remove(chunkN);
                    value=serverId+"-"+time;
                    dataFile.add(chunkN, value);
                    this.metadata.put(fileName, dataFile);
                    dataFile.clear();
                }

            } else {
                value=serverId+"-"+time;
                dataFile.add(chunkN, value);
                this.metadata.put(fileName, dataFile);
                dataFile.clear();
                
            }

        } else {
            value=serverId+"-"+time;
            dataFile.add(chunkN, value);
            this.metadata.put(fileName, dataFile);
            dataFile.clear();
        }
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
}