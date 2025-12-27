package code.frontend.panels;

import javafx.scene.layout.HBox;

/**
 * Manages the buttons for editting, creating and deleting {@link code.backend.Countdown}
 * instances.
 * When there are no selected {@link CountdownPane}, only the add and select... buttons are shown.
 * When there is 1 {@link CountdownPane} selected, only the deselect, edit and remove buttons are
 * shown. When there is greater than 1 {@link CountdownPane} selected, only the deselect and remove
 * buttons are shown.
 *
 * TBD: for 1 or greater selections, a tag button will be shown when the system is implemented.
 */
public class CountdownPaneControls extends HBox {
    private Button addBtn; // add button
    private Button selectionBtn; // select or deselect button
    private Button editBtn; // edit button
    private Button removeBtn; // remove button
}
