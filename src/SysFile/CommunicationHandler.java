package SysFile;

import java.util.Hashtable;

public class CommunicationHandler {
    private Parent dad;
    private int myID;
    public Hashtable<Integer,String> fsList;// F-server connection hash table
    public Hashtable<Integer,String> cList;// Clients connection hash table
    public Hashtable<Integer,String> ms;// M-server connection hash table


    public CommunicationHandler(Parent dad, int myID) {
        this.dad=dad;
        this.myID=myID;
        this.fsList=new Hashtable<Integer,String>();
        this.cList=new Hashtable<Integer,String>();
        this.ms=new Hashtable<Integer,String>();
    }

    public boolean estComm(String typeHost) {
        boolean success=false;

        switch (typeHost) {
            case "M":
                this.getList(typeHost);
                this.connectAll(fsList);
                this.connectAll(cList);
                boolean listSuccess = this.listeNewConnection();
                if (listSuccess) {
                    success=true;
                } else {
                    success=false;
                }
                
                
                break;
        
            case "s":
            //TODO: cH.estComm.s
                break;

            default:
            //TODO: cH.estComm.c
                break;
        }

        
        //TODO: CH.estComm()
        return success;
    }

    public void getList(String typeHost){

        switch (typeHost) {
            case "M":
                // F-server
                for (int i=1;i<=3;i++){
                    this.fsList.put(i, "dc1"+Integer.toString(i)+".utdallas.edu");
                }
                // Clients list
                for (int i=1;i<=2;i++){
                    this.cList.put(i, "dc0"+Integer.toString(i)+".utdallas.edu");
                }
                break;

            case "s":
                // TODO: cH.getList.s
                break;
        
            default: //Clients
            // TODO: cH.getList.c
                break;
        }

    }

    public void connectAll (Hashtable<Integer,String> peers){
        for(Integer id : peers.keySet()) {
            this.connect(id, peers.get(id));
        }
    }

    public void connect (int id, String hostname) {
        System.out.println("Connection to hostId: "+id+", hostname: "+hostname);
    }

    public boolean listeNewConnection() {
        boolean success=false;

        System.out.println("Listening incoming connections...");
        success=true;

        return success;

    }
}