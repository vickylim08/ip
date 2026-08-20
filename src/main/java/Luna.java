import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Luna {
    private static final String line = "   ____________________________________________________________\n";

    public static void main(String[] args) {
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
                        "> todo <desc>: Adds a simple task with no date/time attached\n" +
                        "> deadline <desc> /by <date/time>: Adds a task that must be done before a specific time\n" +
                        "> event <desc> /from <start> /to <end>: Adds a task that spans across a specific time\n" +
                        "> list: displays all currently saved items with index numbers\n" +
                        "> mark <index>: Marks the task at the specified index number as completed ([X]).\n" +
                        "> unmark <index>: Marks the task at the specified index number as not done ([ ]).\n" +
                        "> bye: Exits the program\n"
        );

        String exit =
                line +
                "       Bye. Hope to see you again soon!\n" +
                line;

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("bye")) {
                System.out.println(exit);
                break;
            }
            processCommand(input, tasks);
        }
        scanner.close();
    }

    private static void processCommand(String input, List<Task> tasks) {
        if (input.equalsIgnoreCase("list")) {
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
        } else if (input.startsWith("todo ")) {
            String description = input.substring(5).trim();
            Task task = new Todo(description);
            tasks.add(task);
            System.out.print(line);
            System.out.print("      Got it. I've added this task:\n");
            System.out.println("          " + task);
            System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
            System.out.println(line);
        } else if (input.startsWith("deadline ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            Task task = new Deadline(parts[0], parts[1]);
            tasks.add(task);
            System.out.print(line);
            System.out.print("      Got it. I've added this task:\n");
            System.out.println("          " + task);
            System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
            System.out.println(line);
        } else if (input.startsWith("event ")) {
            String[] parts = input.substring(6).split(" /from | /to ");
            Task task = new Event(parts[0], parts[1], parts[2]);
            tasks.add(task);
            System.out.print(line);
            System.out.print("      Got it. I've added this task:\n");
            System.out.println("          " + task);
            System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
            System.out.println(line);
        }
    }
}
