package SysFile;

import java.util.Scanner;

public class Parent {

    public static void main (String[] args) {

        if (args.length!=2) {
			System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }
        
        if (args[1].equals("M")) {

            System.out.println("This is the M-server");

        } else if (args[1].equals("s")) {

            System.out.println("This is the file-server "+args[0]);

        } else if (args[1].equals("c")) {

            System.out.println("This is the client "+args[0]);

            System.out.println("Enter one of the following commands:");
            System.out.println("1 - Create");
            System.out.println("2 - Read");
            System.out.println("3 - Append");
            System.out.println("4 - Exit");
            Scanner choice = new Scanner(System.in);
            System.out.println();
            System.out.println("Enter \"1\", \"2\", \"3\"or \"4\"");
            System.out.println();
            int choiceEntry=choice.nextInt();

            while (choiceEntry!=4) {

                if (choiceEntry<1 || choiceEntry >3) {

                    System.out.println("Enter \"1\", \"2\", \"3\"or \"4\"");
                    choiceEntry=choice.nextInt();

                }
                else if (choiceEntry==1) {
                    System.out.println("Creating");
                    System.out.println();
                }
                else if (choiceEntry==2) {
                    System.out.println("Reading");
                    System.out.println();
                }
                else if (choiceEntry==3){
                    System.out.println("Appending");
                    System.out.println();
                }
                else if (choiceEntry==4){
                    System.out.println("Saliendo");
                    System.exit(1);
                }

                System.out.println("Enter \"1\", \"2\", \"3\"or \"4\"");
                choiceEntry=choice.nextInt(); 
            }

            

        } else {

            System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }

    }
} 