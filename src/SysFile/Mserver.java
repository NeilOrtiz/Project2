package SysFile;

import java.util.ArrayList;

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
        } else if (requestType.equals("ls")) {
            destID=Integer.parseInt( msg.split(";")[1]); // Get Client ID who sent the request
            String info;
            info= this.query();
            msg=dad.typeHost+";"+dad.myID+";"+"AnswerLs"+";"+info+";"+0+";"+0+";"+0;
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
        int serverId=-1;
        ArrayList<String> dataFile= new ArrayList<String>();

        if (dad.metadata.containsKey(fileName)) {
            dataFile=dad.metadata.get(fileName);
            for (String dF:dataFile) {
                if (!dF.equals("null")) {
                    serverId=Integer.parseInt(dF.split("-")[0]);
                }

            }

        } else {
            System.err.println("[ERROR] File "+fileName+" doesn't exist");
            //return -1;
        }


        return serverId;

    }

    public void update(String msg) {
        String datas,serverId,fileName,time,chunkNs,length;
        ArrayList<String> data;
        int chunkN;

        datas=msg.split(";")[3];
        serverId=msg.split(";")[1];
        data=this.procesDatas(datas);
        

        for (String dt:data) {
                fileName=dt.split("_")[0];
                time=dt.split("-")[1];
                chunkNs=dt.split("_")[2];
                chunkNs=chunkNs.split("\\.")[0];
                chunkN=Integer.parseInt(chunkNs);
                length=dt.split("-")[2];
                
                dad.mutex1.lock();
                this.checkMeta(fileName, time, chunkN,serverId,length);
                dad.mutex1.unlock();
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

    public void checkMeta(String fileName,String time,int chunkN, String serverId,String length) {
        String value;
        long metaTime;
        ArrayList<String> dataFile= new ArrayList<String>(); 

        if (dad.metadata.containsKey(fileName)) {
            dataFile=dad.metadata.get(fileName);
            int counter=0;
            int temp=100;
            value=serverId+"-"+time+"-"+length;
            for (String dF:dataFile) {
                if ((counter==chunkN)) {
                    if (!dF.equals("null")) {
                        metaTime=Long.parseLong(dF.split("-")[1]);
                        if (Long.parseLong(time)>metaTime) {
                            temp=counter;
                        }
                    } else {
                        temp=counter;
                    }
                } 
                counter++; 
            }
            if (temp!=100){
                dataFile.set(temp, value);
            }
        } else {
            value=serverId+"-"+time+"-"+length;
            for (int i=0;i<=5;i++){
                dataFile.add(i, "null");
            }
            dataFile.add(chunkN, value);
            dad.metadata.put(fileName, dataFile);
        }
        //this.metadataStatus();
    }

    public void metadataStatus(){
        Set<String> keys=dad.metadata.keySet();
        System.out.println("-------- METADATA --------------");
        System.out.println("Metadata size: "+dad.metadata.size());
        for (String key:keys){
            System.out.println("Metadata FileName: "+key+", value: "+dad.metadata.get(key));
        }
        System.out.println("------------------------------------");
    }

    public String query(){
        String answer=null;
        String lenghtS;
        Long lenght;
        ArrayList<String> datas=new ArrayList<String>();
        ArrayList<String> info=new ArrayList<String>();
        Set<String> keys=dad.metadata.keySet();
        for (String key:keys) {
            datas=dad.metadata.get(key);
            Long totalLenght=0L;
            for (String value:datas) {
                if (!value.equals("null")) {
                    lenghtS=value.split("-")[2];
                    lenght=Long.valueOf(lenghtS);
                    totalLenght=totalLenght+lenght;
                } 
            }
            info.add(key+"-"+totalLenght);
        }
        answer=info.toString();
        return answer;
    }
}