package code.frontend.gui;

import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.CountdownPaneView.ButtonMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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

    private static final double BG_RADIUS = 12;
    private static final double BG_INSETS = 4;

    private static final double BORDER_CORNER_OFFSET = 0.12;
    private static final double BORDER_DEV = 0;
    private static final double BORDER_CORNER_DEV = 0.014;
    private static final double BORDER_THICKNESS = 2;

    private static final int RIGHTLEFT_INSET = 15;

    private static final double WIDTH = 190;
    private static final double HEIGHT = 185;

    private static final double BUTTON_HEIGHT = 40;

    private final String MARK_COMPLETE_STR = "Mark as complete";
    private final String EDIT_STR = "Edit...";
    private final String ADD_TO_FOLDER_STR = "Add to folder...";
    private final String DELETE_STR = "Delete";

    private Button markAsComplete;
    private Button edit;
    private Button addToFolder;
    private Button delete;

    private RightClickMenu() {
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

    public static RightClickMenu openAt(double x, double y) {
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
        mc.getChildren().add(instance);
        instance.setViewOrder(-100);

        return instance;
    }

    public static void close() {
        if (instance == null)
            return;
        instance.setVisible(false);
        MainContainer.getInstance().getChildren().remove(instance);
        instance = null;
    }

    private void initStyling() {
        CustomBox border =
            new CustomBox(BORDER_THICKNESS, BORDER_DEV, BORDER_CORNER_DEV, BORDER_CORNER_OFFSET);
        CustomBox.applyCustomBorder(this, border);
        this.setManaged(false);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, BG_RADIUS, BG_INSETS));
        this.setFillWidth(true);
        this.resize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setAlignment(Pos.CENTER);
    }

    private void initButtonStylings(Button... buttons) {
        for (Button button : buttons) {
            button.setPrefSize(WIDTH, BUTTON_HEIGHT);
            button.setMaxSize(WIDTH, BUTTON_HEIGHT);
            button.setAnimationsEnabled(false);
            button.getCustomBorder().setVisible(false);
            button.getLabel().setAlignment(Pos.CENTER_LEFT);
            button.setConsumeEvent(true);
            VBox.setMargin(button, new Insets(0, RIGHTLEFT_INSET, 0, RIGHTLEFT_INSET));
        }

        final int RADIUS = 8;
        BackgroundFill markFill = new BackgroundFill(
            Colour.BTTN_MARK_COMPLETE, new CornerRadii(RADIUS), new Insets(5, -5, 5, -5));
        BackgroundFill editFill =
            new BackgroundFill(Colour.BTTN_EDIT, new CornerRadii(RADIUS), new Insets(5, -5, 5, -5));
        BackgroundFill addToFolderFill = new BackgroundFill(
            Colour.BTTN_ADD_TO_FOLDER, new CornerRadii(RADIUS), new Insets(5, -5, 5, -5));
        BackgroundFill deleteFill = new BackgroundFill(
            Colour.BTTN_REMOVE, new CornerRadii(RADIUS), new Insets(5, -5, 5, -5));

        this.markAsComplete.setFeedbackBackground(new Background(markFill));
        this.edit.setFeedbackBackground(new Background(editFill));
        this.addToFolder.setFeedbackBackground(new Background(addToFolderFill));
        this.delete.setFeedbackBackground(new Background(deleteFill));
    }

    private Region createDivider() {
        final double DIVIDER_HEIGHT = 5;
        Pane divider = new Pane();
        divider.setPrefSize(WIDTH, DIVIDER_HEIGHT);
        divider.setMaxSize(WIDTH, DIVIDER_HEIGHT);
        divider.setBackground(null);
        divider.setOpacity(0.3);
        CustomLine line = new CustomLine(BORDER_THICKNESS, Type.HORIZONTAL_TYPE);
        line.setPadding(RIGHTLEFT_INSET);
        line.setStrokeColour(Color.WHITE);
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
        ButtonMode mode = CountdownPaneView.getInstance().getMode();
        switch (mode) {
            case NO_SELECT:
                this.markAsComplete.setEnabled(false);
                this.edit.setEnabled(false);
                this.delete.setEnabled(false);
                this.addToFolder.setEnabled(false);
                System.out.println("no");
                break;
            case SINGLE_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(true);
                this.delete.setEnabled(true);
                this.addToFolder.setEnabled(true);
                System.out.println("single");
                break;
            case MULTI_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(false);
                this.delete.setEnabled(true);
                this.addToFolder.setEnabled(true);
                System.out.println("multi");
                break;
            default:
                break;
        }
    }
}
