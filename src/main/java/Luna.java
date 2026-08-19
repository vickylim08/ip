import java.util.Scanner;

public class Luna {
    public static void main(String[] args) {
        String banner =
                "____________________________________________________________\n" +
                "   Hello! I'm Luna.\n" +
                "   What can I do for you?\n" +
                "____________________________________________________________\n";
        System.out.println(banner);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type anything (type 'bye' to exit):");
        String exit =
                "   ____________________________________________________________\n" +
                "       Bye. Hope to see you again soon!\n" +
                "   ____________________________________________________________";

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String echo =
                    "   ____________________________________________________________\n" +
                    "       " + input + "\n" +
                    "   ____________________________________________________________";
            if (input.equalsIgnoreCase("bye")) {
                System.out.println(exit);
                break;
            } else {
                System.out.println(echo);
            }
        }
        scanner.close();
    }
}
