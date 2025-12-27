package code.frontend.windows;

import code.frontend.misc.Vals;
import code.frontend.misc.Vals.UtilityUI;
import code.frontend.panels.Button;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddWindow extends Stage {
    private static AddWindow window = null;
    private static VBox root;

    private static InputField nameField;
    private static DateInputField dateField;
    private static InputField daysField;

    private AddWindow() {
        this.setResizable(false);
        this.initStyle(StageStyle.UTILITY);
        this.setMinWidth(UtilityUI.WIDTH);
        this.setMaxWidth(UtilityUI.WIDTH);
        this.setMinHeight(UtilityUI.HEIGHT);
        this.setMaxHeight(UtilityUI.HEIGHT);
        this.setTitle("creating countdown");
    }

    public static AddWindow getStage() {
        if (window == null) {
            window = new AddWindow();

            root = new VBox();
            root.setPrefSize(UtilityUI.WIDTH, UtilityUI.HEIGHT);
            root.setOpacity(0);
            root.relocate(0, 0);

            configureChildren();

            Scene scene = new Scene(root);
            scene.setFill(Vals.Colour.BACKGROUND);
            window.setScene(scene);
            window.show();
        }
        window.toFront();
        return window;
    }

    private static void configureChildren() {
        Pane namePart = createNamePart();
        Pane duePart = createDuePart();
        Pane buttonPart = createButtonPart();
        root.getChildren().addAll(namePart, duePart, buttonPart);
    }

    private static Pane createNamePart() {
        VBox container = new VBox();
        container.setFillWidth(true);

        Label label = new Label("its name is...");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(UtilityUI.WIDTH);
        label.setFont(UtilityUI.getFont());
        nameField = new InputField();
        nameField.setMaxWidth(UtilityUI.WIDTH);
        VBox.setMargin(label, new Insets(10, 5, 0, 5));
        VBox.setMargin(nameField, new Insets(3, 5, 0, 5));

        container.getChildren().addAll(label, nameField);

        return container;
    }

    private static Pane createDuePart() {
        HBox container = new HBox();

        VBox dateFieldContainer = new VBox();
        Label dateLabel = new Label("it is due on...");
        dateLabel.setAlignment(Pos.CENTER_LEFT);
        dateLabel.setMaxWidth(UtilityUI.WIDTH);
        dateLabel.setFont(UtilityUI.getFont());
        dateField = new DateInputField();
        dateField.setMaxWidth(UtilityUI.WIDTH);
        VBox.setMargin(dateLabel, new Insets(0, 5, 0, 5));
        VBox.setMargin(dateField, new Insets(3, 5, 0, 5));
        dateFieldContainer.getChildren().addAll(dateLabel, dateField);

        Label separator = new Label("or in");
        separator.maxHeightProperty().bind(dateFieldContainer.heightProperty());
        separator.setAlignment(Pos.CENTER);
        separator.setFont(UtilityUI.getFont());
        HBox.setMargin(separator, new Insets(0, 8, 0, 8));

        VBox daysFieldContainer = new VBox();
        Label daysLabel = new Label("day(s)");
        daysLabel.setAlignment(Pos.CENTER_RIGHT);
        daysLabel.setMaxWidth(UtilityUI.WIDTH);
        daysLabel.setFont(UtilityUI.getFont());
        daysField = new InputField();
        daysField.setNumInputOnly(true);
        daysField.setTextLimit(5);
        VBox.setMargin(daysLabel, new Insets(0, 5, 0, 5));
        VBox.setMargin(daysField, new Insets(0, 5, 3, 5));
        daysFieldContainer.getChildren().addAll(daysField, daysLabel);

        container.getChildren().addAll(dateFieldContainer, separator, daysFieldContainer);
        return container;
    }

    private static Pane createButtonPart() {
        BorderPane container = new BorderPane();
        container.setPrefWidth(UtilityUI.WIDTH);
        Button button = new Button("create!") {
            @Override
            public void executeOnClick() {
                // TODO
            }
        };
        button.setPrefHeight(50);
        button.setPrefWidth(80);
        container.setCenter(button);

        return container;
    }
}
