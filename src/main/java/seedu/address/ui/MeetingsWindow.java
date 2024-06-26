package seedu.address.ui;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.person.Meeting;

/**
 * Controller for a help page
 */
public class MeetingsWindow extends UiPart<Stage> {
    private static final Logger logger = LogsCenter.getLogger(MeetingsWindow.class);
    private static final String FXML = "MeetingsWindow.fxml";
    private final Logic logic;

    @FXML
    private Label meetingsMessage;

    /**
     * Creates a new MeetingsWindow.
     *
     * @param root Stage to use as the root of the MeetingsWindow.
     */
    public MeetingsWindow(Stage root, Logic logic) {
        super(FXML, root);
        this.logic = logic;
        root.setMinWidth(600);
        root.setMinHeight(450);
    }

    /**
     * Creates a new MeetingsWindow with a new Stage.
     */
    public MeetingsWindow(Logic logic) {
        this(new Stage(), logic);
        assert(getRoot() != null); // Ensure that this UiPart is correctly initialized
    }

    /**
     * Shows the meetings window and displays the list of meetings for this week.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void displayMeetings() {
        logger.fine("Showing this week's meetings window for the application.");
        getRoot().show();
        getRoot().centerOnScreen();

        List<Meeting> meetings = logic.getMeetingList();
        meetings = meetings.stream()
                .filter(m -> m.getMeeting() != null)
                .filter(m -> m.getMeeting().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                        == LocalDateTime.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                .collect(Collectors.toList());
        meetings.sort(Comparator.comparing(Meeting::getMeeting));
        StringBuilder sb = new StringBuilder();
        sb.append("Here are all of this week's meetings in chronological order: \n");
        int count = 1;
        for (Meeting m : meetings) {
            sb.append(count).append("  |  ").append(m.toString())
                    .append(" with: ").append(m.getName()).append("\n");
            count++;
        }
        meetingsMessage.setText(sb.toString());
        closeOnEsc();
    }

    /**
     * Returns true if the meetings window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the meetings window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Reopens the meetings window.
     */
    public void reopen() {
        hide();
        displayMeetings();
    }

    /**
     * Closes the window when user presses "Esc" key.
     */
    public void closeOnEsc() {
        getRoot().addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                hide();
            }
        });
    }
}
