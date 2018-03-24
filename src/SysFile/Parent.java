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
                        System.out.println("Creating");
                        System.out.println();
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

            

        } else {

            System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }

    }
} 