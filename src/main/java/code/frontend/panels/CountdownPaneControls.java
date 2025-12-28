package code.frontend.panels;

import code.backend.StorageHandler;
import code.frontend.windows.AddWindow;
import code.frontend.windows.EditWindow;
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
    enum ControlMode { NO_SELECT, SINGLE_SELECT, MULTI_SELECT }

    private static CountdownPaneControls instance = null;

    private Button addBtn; // add button
    private Button editBtn; // edit button
    private Button removeBtn; // remove button
    private Button deselectBtn; // deselect

    protected static final String DESELECT_DEFAULT_STR = "deselect";

    private CountdownPaneView view;

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
                    instance.add();
                }
            };
            instance.editBtn = new Button("edit") {
                @Override
                public void executeOnClick() {
                    instance.edit();
                }
            };
            instance.removeBtn = new Button("remove") {
                @Override
                public void executeOnClick() {
                    instance.remove();
                }
            };
            instance.deselectBtn = new Button(DESELECT_DEFAULT_STR) {
                @Override
                public void executeOnClick() {
                    instance.deselectAll();
                }
            };
            applyStyling(
                instance.addBtn, instance.editBtn, instance.removeBtn, instance.deselectBtn);
        }
        return instance;
    }

    private static void applyStyling(Button... buttons) {
        for (Button button : buttons) {
            HBox.setMargin(button, new Insets(5, 10, 5, 10));
            button.setMinWidth(130);
            button.setMinHeight(40);
        }

        Pane filler = new Pane();
        filler.setVisible(false);
        HBox.setHgrow(filler, Priority.ALWAYS);
        filler.maxHeightProperty().bind(instance.maxHeightProperty());

        instance.getChildren().add(filler);
        instance.getChildren().addAll(buttons);
    }

    private void add() {
        AddWindow.getInstance();
    }

    private void edit() {
        CountdownPane[] selectedPanes = view.getAllSelected();
        if (selectedPanes == null || selectedPanes.length == 0)
            return;
        EditWindow.getInstance(selectedPanes[0].getCountdown());
    }

    private void remove() {
        CountdownPane[] selectedPanes = view.getAllSelected();
        for (CountdownPane countdownPane : selectedPanes)
            StorageHandler.deleteCountdown(countdownPane.getCountdown());
        StorageHandler.save();
    }

    private void deselectAll() {
        CountdownPane[] selectedPanes = view.getAllSelected();
        for (CountdownPane countdownPane : selectedPanes) {
            countdownPane.setSelected(false);
            countdownPane.applyDeselectStyle();
        }
        deselectBtn.setTextLabel(DESELECT_DEFAULT_STR);
    }

    public void updateSelectionButtonIndicator() {
        // shows the user how many selections there are via the deselectButton
        String newButtonLabel = DESELECT_DEFAULT_STR;
        int numOfSelections = CountdownPaneView.getInstance().getAllSelected().length;
        if (numOfSelections > 0) {
            newButtonLabel += " (" + Integer.toString(numOfSelections) + ")";
        }
        deselectBtn.setTextLabel(newButtonLabel);
    }

    public void setMode(ControlMode mode) {
        switch (mode) {
            case NO_SELECT:
                addBtn.setEnabled(true);
                editBtn.setEnabled(false);
                removeBtn.setEnabled(false);
                deselectBtn.setEnabled(false);
                break;
            case SINGLE_SELECT:
                addBtn.setEnabled(false);
                editBtn.setEnabled(true);
                removeBtn.setEnabled(true);
                deselectBtn.setEnabled(true);
                break;
            case MULTI_SELECT:
                addBtn.setEnabled(false);
                editBtn.setEnabled(false);
                removeBtn.setEnabled(true);
                deselectBtn.setEnabled(true);
                break;
            default:
                break;
        }
    }

    public Button getDeselectBtn() {
        return deselectBtn;
    }
}
