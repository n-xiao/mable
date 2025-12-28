package code.frontend.panels;

import code.frontend.windows.AddWindow;
import javafx.scene.layout.HBox;

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

    private Button addBtn; // add button
    private Button editBtn; // edit button
    private Button removeBtn; // remove button
    private Button deselectBtn; // deselect

    private CountdownPaneView view;

    private CountdownPaneControls() {
        this.setBackground(null);
        this.setFillHeight(true);
    }

    private CountdownPaneControls getInstance() {
        if (instance == null) {
            instance = new CountdownPaneControls();
        }
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
        instance.deselectBtn = new Button("deselect all") {
            @Override
            public void executeOnClick() {
                instance.deselect();
            }
        };
        return instance;
    }

    private void add() {
        AddWindow.getInstance();
    }

    private void edit() {
        // TODO
    }

    private void remove() {
        // TODO
    }

    private void deselect() {
        // TODO
    }
}
