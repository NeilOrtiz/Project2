package SysFile;

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

        } else {
            System.err.println("Usage: java -jar Project2/dist/Parent.jar <ID> <M|s|c>");
			System.exit(1);
        }

    }
} 