package seedu.duke.userinterface.command.timetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.duke.tasks.Task;
import seedu.duke.tasks.TaskList;
import seedu.duke.userinterface.AppState;
import seedu.duke.userinterface.command.CliCommand;

public class FindCommandTimetableMode extends CliCommand {
    public static final String COMMAND_WORD = "find";
    private String keyword;
    private String tag;
    private boolean isPersonalised = true;

    public FindCommandTimetableMode(String keyword, String tag, AppState appState) {
        this.keyword = keyword.toLowerCase();
        this.tag = tag.toLowerCase();
        this.appState = appState;
    }

    public void execute() {
        ArrayList<Task> tasksFound;
        TaskList tasks = appState.getTaskList();
        if (tag.equals("") && !keyword.equals("")) {
            tasksFound = getTasksWithTitleContainingKeyword(tasks);
        } else if (!tag.equals("") && keyword.equals("")) {
            tasksFound = getTasksWithTag(tasks);
        } else {
            System.out.println("Missing keyword/tag");
            System.out.println("Format: find [KEYWORD] or find /t[TAG]");
            return;
        }
        System.out.println("Here are the tasks I found:");
        for (Task task : tasksFound) {
            System.out.println(task);
        }
    }

    private ArrayList<Task> getTasksWithTitleContainingKeyword(TaskList tasks) {
        ArrayList<Task> matchedTasks = new ArrayList<>();
        for (Task task : tasks.getTaskArrayList()) {
            Set<String> wordsInTitle = new HashSet<>(getWordsInTitle(task.getTitle()));
            for (String word : wordsInTitle) {
                if (word.contains(keyword)) {
                    matchedTasks.add(task);
                }
            }
        }
        return matchedTasks;
    }

    private ArrayList<Task> getTasksWithTag(TaskList tasks) {
        ArrayList<Task> matchedTasks = new ArrayList<>();
        for (Task task : tasks.getTaskArrayList()) {
            String taskTag = task.getTag().toLowerCase();
            if (!taskTag.equals("") && taskTag.equals(tag)) {
                matchedTasks.add(task);
            }
        }
        return matchedTasks;
    }

    private List<String> getWordsInTitle(String description) {
        return Arrays.asList(description.toLowerCase().split(" "));
    }

    @Override
    public boolean isPersonalised() {
        return isPersonalised;
    }
}
