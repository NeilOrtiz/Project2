package SysFile;

import java.util.Random;

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
        String requestType,fileName;
        int serverId;

        requestType=msg.split(",")[2];
        fileName=msg.split(",")[3];

        if (requestType.equals("creation")){
            //Select random Fserver. Get dstID
            serverId=this.randomFserver();
            msg=dad.typeHost+","+dad.myID+","+"creation"+","+fileName+","+serverId+","+0;

            // send request to Fserver
            sender.sendMessage(msg, cH.peers_listen, serverId);
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
}