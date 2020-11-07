package zeronote.userinterface.command.notebook;

import zeronote.exceptions.IncorrectAppModeException;
import zeronote.exceptions.InvalidNotebookException;
import zeronote.exceptions.InvalidPageException;
import zeronote.exceptions.InvalidSectionException;
import zeronote.exceptions.ZeroNoteException;
import zeronote.notebooks.Notebook;
import zeronote.notebooks.NotebookShelf;
import zeronote.notebooks.Page;
import zeronote.notebooks.Section;
import zeronote.userinterface.AppState;
import zeronote.userinterface.CliMessages;
import zeronote.userinterface.command.CliCommand;

public class RemoveCommandNotebookMode extends CliCommand {
    public static final String COMMAND_WORD = "delete";

    private CliMessages cliMessages = new CliMessages();
    private String notebookTitleToRemove;
    private String sectionTitleToRemove;
    private String pageTitleToRemove;

    private static final boolean isAutoSave = true;

    public RemoveCommandNotebookMode(String notebookTitle, String sectionTitle,
                                     String pageTitle, AppState appState) {
        this.appState = appState;
        notebookTitleToRemove = notebookTitle;
        sectionTitleToRemove = sectionTitle;
        pageTitleToRemove = pageTitle;

        PRINTS_PERSONAL_MESSAGE = true;
    }

    public void execute() {
        try {
            switch (appState.getAppMode()) {
            case NOTEBOOK_SHELF:
                if (notebookTitleToRemove.equals("")) {
                    throw new InvalidNotebookException(notebookTitleToRemove);
                }
                removeFromNotebookShelf();
                break;
            case NOTEBOOK_BOOK:
                if (!notebookTitleToRemove.equals("")) {
                    throw new IncorrectAppModeException();
                }
                removeFromNotebook(appState.getCurrentNotebook());
                break;
            case NOTEBOOK_SECTION:
                if (!notebookTitleToRemove.equals("") || !sectionTitleToRemove.equals("")) {
                    throw new IncorrectAppModeException();
                }
                removeFromSection(appState.getCurrentSection());
                break;
            default:
                throw new IncorrectAppModeException();
            }
        } catch (ZeroNoteException zne) {
            zne.printErrorMessage();
        }
    }

    private void removeFromSection(Section section) throws InvalidPageException {
        int indexOfPage = section.findPage(pageTitleToRemove);
        Page pageRemoved = section.removePage(indexOfPage);
        cliMessages.printRemovePageMessage(pageRemoved);
    }

    private void removeFromNotebook(Notebook notebook) throws InvalidPageException, InvalidSectionException {
        if (!sectionTitleToRemove.equals("") && !pageTitleToRemove.equals("")) {
            int indexOfSectionToRemove = notebook.findSection(sectionTitleToRemove);
            Section section;
            try {
                section = notebook.getSectionAtIndex(indexOfSectionToRemove);
            } catch (IndexOutOfBoundsException ioe) {
                throw new InvalidSectionException(sectionTitleToRemove);
            }
            removeFromSection(section);
        } else if (!sectionTitleToRemove.equals("")) {
            int indexOfSectionToRemove = notebook.findSection(sectionTitleToRemove);
            try {
                Section sectionRemoved = notebook.removeSection(indexOfSectionToRemove);
                cliMessages.printRemoveSectionMessage(sectionRemoved);
            } catch (IndexOutOfBoundsException ioe) {
                throw new InvalidSectionException(sectionTitleToRemove);
            }
        } else {
            throw new InvalidSectionException(sectionTitleToRemove);
        }
    }

    private void removeFromNotebookShelf() throws InvalidNotebookException, InvalidSectionException,
            InvalidPageException {

        assert !notebookTitleToRemove.equals("");
        NotebookShelf currentBookshelf = appState.getCurrentBookShelf();
        int indexOfNotebookToRemove = currentBookshelf.findNotebook(notebookTitleToRemove);

        if (!notebookTitleToRemove.equals("") && !sectionTitleToRemove.equals("")) {
            Notebook notebook;
            try {
                notebook = currentBookshelf.getNotebookAtIndex(indexOfNotebookToRemove);
            } catch (IndexOutOfBoundsException ioe) {
                throw new InvalidNotebookException(notebookTitleToRemove);
            }
            removeFromNotebook(notebook);
            return;
        } else if (!notebookTitleToRemove.equals("") && pageTitleToRemove.equals("")) {
            try {
                Notebook notebookRemoved = currentBookshelf.removeNotebook(indexOfNotebookToRemove);
                cliMessages.printRemoveNotebookMessage(notebookRemoved);
            } catch (IndexOutOfBoundsException ioe) {
                throw new InvalidNotebookException(notebookTitleToRemove);
            }
            return;
        }

        if (sectionTitleToRemove.equals("")) {
            throw new InvalidSectionException(sectionTitleToRemove);
        }
        if (!pageTitleToRemove.equals("")) {
            throw new InvalidPageException(pageTitleToRemove);
        }
    }

    @Override
    public boolean isTriggerAutoSave() {
        return isAutoSave;
    }
}