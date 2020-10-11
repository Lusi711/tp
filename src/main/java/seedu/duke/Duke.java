package seedu.duke;

import seedu.duke.exceptions.InvalidCommandException;
import seedu.duke.notebooks.NotebookShelf;
import seedu.duke.tasks.TaskList;
import seedu.duke.userinterface.CliUserInterface;

import java.util.Scanner;

public class Duke {
    public static boolean notQuit = true;
    private static final TaskList taskList = new TaskList();
    public static final NotebookShelf notebookShelf = new NotebookShelf();
    private static int numberOfTasks = 0;

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) throws Exception {
        CliUserInterface ui = new CliUserInterface(taskList,notebookShelf);
        Scanner in = new Scanner(System.in);
        ui.startUI();
        while (notQuit) {
            String userInput = in.nextLine();
            try {
                ui.executeCommand(userInput);
            } catch (InvalidCommandException i) {
                System.out.println("\tSorry, I did not get that. ");
            }
        }
    }
}