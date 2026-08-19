import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Luna {
    public static void main(String[] args) {
        String line = "   ____________________________________________________________\n";
        String banner =
                "____________________________________________________________\n" +
                "   Hello! I'm Luna.\n" +
                "   What can I do for you?\n" +
                "____________________________________________________________\n";
        System.out.println(banner);
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        System.out.println(
                "Available Commands:\n" +
                        "<any text>: adds a new item to your task list\n" +
                        "list: displays all currently saved items with index numbers\n" +
                        "mark <index>: Marks the task at the specified index number as completed ([X]).\n" +
                        "unmark <index>: Marks the task at the specified index number as not done ([ ]).\n" +
                        "bye: Exits the program\n"
        );

        String exit =
                line +
                "       Bye. Hope to see you again soon!\n" +
                line;

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String response =
                    line +
                    "       added: " + input + "\n" +
                    line;
            if (input.equalsIgnoreCase("bye")) {
                System.out.println(exit);
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.print(line);
                System.out.print("      Here are the tasks in your list\n");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("      " + (i + 1) + ". " + tasks.get(i));
                }
                System.out.println(line);
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                if (index >= 0 && index < tasks.size()) {
                    Task task = tasks.get(index);
                    task.markAsDone();
                    System.out.print(line);
                    System.out.print("      Nice! I've marked this as done:\n");
                    System.out.println("      " + task);
                    System.out.print(line);
                }
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                if (index >= 0 && index < tasks.size()) {
                    Task task = tasks.get(index);
                    task.markAsNotDone();
                    System.out.print(line);
                    System.out.print("      OK, I've marked this task as not done yet:\n");
                    System.out.println("      " + task);
                    System.out.print(line);
                }
            } else if (!input.isEmpty()) {
                    Task task = new Task(input);
                    tasks.add(task);
                    System.out.println(response);
            }
        }
        scanner.close();
    }
}
