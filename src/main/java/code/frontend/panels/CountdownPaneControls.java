package code.frontend.panels;

import code.frontend.misc.Vals.Colour;
import code.frontend.panels.CountdownPaneView.ButtonMode;
import code.frontend.windows.AddWindow;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Manages the logic for selecting, editing, creating and deleting {@link code.backend.Countdown}
 * instances. Does not perform any styling modifications.
 * When there are no selected {@link CountdownPane}, only the add button is shown.
 * When there is 1 {@link CountdownPane} selected, only the deselect, edit and remove buttons are
 * shown. When there is greater than 1 {@link CountdownPane} selected, only the deselect and remove
 * buttons are shown.
 *
 * TODO: implement shift and ctrl/command click
 *
 * TBD: for 1 or greater selections, a tag button will be shown when the system is implemented.
 */
public class CountdownPaneControls extends HBox {
    private static CountdownPaneControls instance = null;

    protected static final String DESELECT_DEFAULT_STR = "deselect";
    protected static final String REMOVE_DEFAULT_STR = "remove";

    private Button addBtn; // add button
    private Button deselectBtn; // deselect

    private CountdownPaneControls() {
        this.setBackground(null);
        this.setFillHeight(true);
    }

    public static CountdownPaneControls getInstance() {
        if (instance == null) {
            instance = new CountdownPaneControls();
            instance.addBtn = new Button("create") {
                @Override
                public void executeOnClick() {
                    AddWindow.getInstance();
                }
            };
            instance.deselectBtn = new Button(DESELECT_DEFAULT_STR) {
                @Override
                public void executeOnClick() {
                    CountdownPaneView.getInstance().deselectAll();
                }
            };
            instance.applyStyling(instance.addBtn, instance.deselectBtn);
            instance.setMode();
        }
        return instance;
    }

    private void applyStyling(Button... buttons) {
        for (Button button : buttons) {
            HBox.setMargin(button, new Insets(5, 10, 5, 10));
            button.setMinWidth(130);
            button.setMinHeight(40);
        }

        Pane filler = new Pane();
        filler.setVisible(false);
        HBox.setHgrow(filler, Priority.ALWAYS);
        filler.maxHeightProperty().bind(instance.maxHeightProperty());

        this.addBtn.setColour(Colour.BTTN_CREATE);
        this.deselectBtn.setColour(Colour.BTTN_DESELECT);

        this.getChildren().add(filler);
        this.getChildren().addAll(buttons);
    }

    public void updateSelectionButtonIndicators() {
        // shows the user how many selections there are via the deselectButton
        String newDeselectButtonLabel = DESELECT_DEFAULT_STR;
        int numOfSelections = CountdownPaneView.getInstance().getNumOfSelections();
        if (numOfSelections > 1) {
            newDeselectButtonLabel += " (" + Integer.toString(numOfSelections) + ")";
        }
        this.deselectBtn.setTextLabel(newDeselectButtonLabel);
    }

    public void setMode() {
        ButtonMode mode = CountdownPaneView.getInstance().getMode();
        switch (mode) {
            case NO_SELECT:
                addBtn.setEnabled(true);
                deselectBtn.setEnabled(false);
                break;
            default:
                addBtn.setEnabled(false);
                deselectBtn.setEnabled(true);
                break;
        }
    }
}
