package SysFile;

import java.util.Scanner;

public class Client {
    private String msg;
    private Parent dad;
    private Sender sender;
    private CommunicationHandler cH;


    public Client(Parent dad,CommunicationHandler cH){
        this.msg=null;
        this.dad=dad;
        this.sender=new Sender();
        this.cH=cH;

    }
    public void execute() {

        System.out.println("********** Executando desde Client.java -***********");
        Scanner choice = new Scanner(System.in);
            int choiceEntry=-1;

            while (choiceEntry!=4) {

                while (choiceEntry<1 || choiceEntry >4) {

                    System.out.println("Enter one of the following commands:");
                    System.out.println("1 - Create");
                    System.out.println("2 - Read");
                    System.out.println("3 - Append");
                    System.out.println("4 - Exit");
                    System.out.println();
                    if (choice.hasNextInt()) {

                        choiceEntry=choice.nextInt();
                        if (choiceEntry<1 || choiceEntry >4) {
                            System.out.println("Invalidad input");
                        }
                        
                    }
                }

                switch (choiceEntry) {
                    case 1:
                    String newFileName;    
                    
                        System.out.println("Creating");
                        System.out.println();
                        System.out.print("Enter new File Name: ");
                        newFileName=choice.nextLine();
                        msg=dad.typeHost+","+dad.myID+","+"creation"+newFileName;
                        sender.sendMessage(msg, cH.peers_listen, 10);
                        break;

                    case 2:
                        System.out.println("Reading");
                        System.out.println();
                        break;

                    case 3:
                        System.out.println("Appending");
                        System.out.println();
                        break;

                    case 4:
                        System.out.println("Saliendo");
                        choice.close();
                        System.exit(1);
                        break;
                
                    default:
                        break;
                }
                choiceEntry=-1;
            }
            choice.close();

    }
}