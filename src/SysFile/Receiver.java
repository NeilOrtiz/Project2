package SysFile;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver extends Thread {
    private BufferedReader readSocket;
    private Parent dad;
    private CommunicationHandler cH;

    public Receiver(BufferedReader readSocket,Parent dad,CommunicationHandler cH) {
        super("Receiver");
        this.dad=dad;
        this.cH=cH;
        this.readSocket=readSocket;
    }

    @Override
	public void run() {

        while (dad.listening) {

            try {
                String msg =readSocket.readLine();
                System.out.println(msg);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }




        }

        

    }

}