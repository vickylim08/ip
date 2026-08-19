import java.util.ArrayList;
import java.util.List;
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
        List<String> list = new ArrayList<>();

        System.out.println(
                "Available Commands:\n" +
                        "<any text>: adds a new item to your task list\n" +
                        "list: displays all currently saved items with index numbers\n" +
                        "bye: Exits the program\n"
        );
        String exit =
                "   ____________________________________________________________\n" +
                "       Bye. Hope to see you again soon!\n" +
                "   ____________________________________________________________";

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String response =
                    "   ____________________________________________________________\n" +
                    "       added: " + input + "\n" +
                    "   ____________________________________________________________";
            if (input.equalsIgnoreCase("bye")) {
                System.out.println(exit);
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("   ____________________________________________________________");
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("      " + (i + 1) + ". " + list.get(i));
                }
                System.out.println("   ____________________________________________________________\n");
            } else if (!input.isEmpty()) {
                list.add(input);
                System.out.println(response);
            }
        }
        scanner.close();
    }
}
