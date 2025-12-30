package code.frontend.gui;

import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.CountdownPaneView.ButtonMode;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * man, i should really write more of these things called comments...
 * RightClickMenu is a singleton class which returns the menu that
 * is shown under the user's cursor when a user presses right-click
 * on a button or something that would trigger this to be shown.
 *
 * In order for it to show up anywhere, this VBox is set to unmanaged
 * and should be manually position based on the cursor position.
 *
 * It is imperative to note that this class uses a "lazy loading"
 * concept, where the GUI is only created (and seen by the user)
 * when the user right-clicks on something. Hence, the only calls
 * to getInstance should be from right-click listeners.
 *
 */
public class RightClickMenu extends VBox {
    private static RightClickMenu instance = null;
    private static final double BG_RADIUS = 0.2;
    private static final double BG_INSETS = 4;

    private static final double WIDTH = 190;
    private static final double HEIGHT = 300;

    private static final double BUTTON_HEIGHT = 60;

    private final String MARK_COMPLETE_STR = "Mark as complete";
    private final String EDIT_STR = "Edit...";
    private final String ADD_TO_FOLDER_STR = "Add to folder...";
    private final String DELETE_STR = "Delete";

    private final ButtonMode MODE;

    private Button markAsComplete;
    private Button edit;
    private Button addToFolder;
    private Button delete;

    private RightClickMenu() {
        this.MODE = CountdownPaneView.getInstance().getMode();
        this.markAsComplete = new Button(MARK_COMPLETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().markSelectedAsComplete();
                RightClickMenu.close();
            }
        };
        this.edit = new Button(EDIT_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().editSelected();
                RightClickMenu.close();
            }
        };
        this.addToFolder = new Button(ADD_TO_FOLDER_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().addSelectedToFolder();
                RightClickMenu.close();
            }
        };
        this.delete = new Button(DELETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().deleteSelected();
                RightClickMenu.close();
            }
        };
    }

    public static RightClickMenu getInstance(double x, double y) {
        RightClickMenu.close();

        instance = new RightClickMenu();
        instance.initStyling();

        instance.initButtonStylings(
            instance.markAsComplete, instance.edit, instance.addToFolder, instance.delete);
        instance.getChildren().addAll(instance.markAsComplete, instance.createDivider(),
            instance.edit, instance.addToFolder, instance.createDivider(), instance.delete);

        instance.setMode();
        instance.updateSelectionButtonIndicators();

        MainContainer mc = MainContainer.getInstance();
        instance.relocate(x, y);
        instance.setVisible(true);
        instance.setViewOrder(-100);
        mc.getChildren().addFirst(instance);

        return instance;
    }

    private static void close() {
        if (instance == null)
            return;
        instance.setVisible(false);
        MainContainer.getInstance().getChildren().remove(instance);
        instance = null;
    }

    private void initStyling() {
        CustomBox border = new CustomBox();
        CustomBox.applyCustomBorder(this, border);
        this.setMinSize(WIDTH, HEIGHT);
        this.setMaxSize(WIDTH, HEIGHT);
        this.setManaged(false);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, BG_RADIUS, BG_INSETS));
        this.setFillWidth(true);
    }

    private void initButtonStylings(Button... buttons) {
        for (Button button : buttons) {
            button.setMinSize(WIDTH, BUTTON_HEIGHT);
            button.setMaxSize(WIDTH, BUTTON_HEIGHT);
            button.setAnimationsEnabled(false);
            button.getCustomBorder().setVisible(false);
            button.getLabel().setAlignment(Pos.CENTER_LEFT);
            button.setConsumeEvent(true);
        }

        this.markAsComplete.setFeedbackColour(Colour.BTTN_MARK_COMPLETE);
        this.edit.setFeedbackColour(Colour.BTTN_EDIT);
        this.addToFolder.setFeedbackColour(Colour.BTTN_EDIT);
        this.delete.setFeedbackColour(Colour.BTTN_REMOVE);
    }

    private Region createDivider() {
        final double DIVIDER_HEIGHT = 10;
        Pane divider = new Pane();
        divider.setMinSize(RightClickMenu.WIDTH, DIVIDER_HEIGHT);
        divider.setMaxSize(RightClickMenu.WIDTH, DIVIDER_HEIGHT);
        divider.setBackground(null);
        divider.setOpacity(0.3);
        CustomLine line = new CustomLine(1.5, Type.HORIZONTAL_TYPE);
        CustomLine.applyCustomBorder(divider, line);

        return divider;
    }

    public void updateSelectionButtonIndicators() {
        // shows the user how many selections there are via the deselectButton
        String newMarkCompleteString = MARK_COMPLETE_STR;
        String newAddToFolderString = ADD_TO_FOLDER_STR;
        String newDeleteString = DELETE_STR;
        int numOfSelections = CountdownPaneView.getInstance().getNumOfSelections();
        if (numOfSelections > 1) {
            appendNumOfSelections(newMarkCompleteString, newAddToFolderString, newDeleteString);
        }
        this.markAsComplete.setTextLabel(newMarkCompleteString);
        this.addToFolder.setTextLabel(newAddToFolderString);
        this.delete.setTextLabel(newDeleteString);
    }

    private void appendNumOfSelections(String... strings) {
        int selections = CountdownPaneView.getInstance().getNumOfSelections();
        for (int i = 0; i < strings.length; i++) {
            strings[i] += " (" + Integer.toString(selections) + ")";
        }
    }

    private void setMode() {
        switch (this.MODE) {
            case NO_SELECT:
                this.markAsComplete.setEnabled(false);
                this.edit.setEnabled(false);
                this.delete.setEnabled(false);
                this.addToFolder.setEnabled(false);
                break;
            case SINGLE_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(true);
                this.delete.setEnabled(true);
                this.addToFolder.setEnabled(true);
                break;
            case MULTI_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(false);
                this.delete.setEnabled(true);
                this.addToFolder.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
