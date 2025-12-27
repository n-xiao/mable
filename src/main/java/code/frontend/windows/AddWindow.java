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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddWindow {
    private static Stage window = null;
    private static VBox root;

    private static InputField nameField;
    private static DateInputField dateField;
    private static InputField daysField;

    private AddWindow() {}

    public static Stage getInstance() {
        if (window == null) {
            window = new Stage();

            window.setResizable(false);
            window.initStyle(StageStyle.UTILITY);
            window.setMinWidth(UtilityUI.WIDTH);
            window.setMaxWidth(UtilityUI.WIDTH);
            window.setMinHeight(UtilityUI.HEIGHT);
            window.setMaxHeight(UtilityUI.HEIGHT);
            window.setTitle("creating countdown");

            root = new VBox();
            root.setPrefSize(UtilityUI.WIDTH, UtilityUI.HEIGHT);
            root.relocate(0, 0);
            root.setBackground(null);

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
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(UtilityUI.WIDTH);
        label.setFont(UtilityUI.getFont());
        nameField = new InputField();
        nameField.setMaxWidth(UtilityUI.WIDTH);
        VBox.setMargin(label, new Insets(0, 0, 0, 8));
        // VBox.setMargin(nameField, new Insets(3, 5, 0, 5));

        container.getChildren().addAll(label, nameField);

        VBox.setMargin(container, new Insets(10));
        return container;
    }

    private static Pane createDuePart() {
        Pane filler1 = new Pane();
        Pane filler2 = new Pane();
        filler1.setVisible(false);
        filler2.setVisible(false);

        HBox container = new HBox();

        VBox dateFieldContainer = new VBox();
        Label dateLabel = new Label("it is due on...");
        dateLabel.setAlignment(Pos.CENTER_LEFT);
        dateLabel.setMaxWidth(UtilityUI.WIDTH);
        dateLabel.setFont(UtilityUI.getFont());
        dateLabel.setTextFill(Color.WHITE);
        dateField = new DateInputField();
        dateField.setMaxWidth(UtilityUI.WIDTH);
        VBox.setMargin(dateLabel, new Insets(0, 0, 0, 8));
        // VBox.setMargin(dateLabel, new Insets(0, 5, 0, 5));
        // VBox.setMargin(dateField, new Insets(3, 5, 0, 5));
        VBox.setVgrow(filler1, Priority.ALWAYS);
        dateFieldContainer.getChildren().addAll(dateLabel, dateField, filler1);

        Label separator = new Label("or in");
        separator.setMaxHeight(UtilityUI.HEIGHT);
        separator.setAlignment(Pos.CENTER);
        separator.setFont(UtilityUI.getFont());
        separator.setTextFill(Color.WHITE);
        HBox.setMargin(separator, new Insets(0, 8, 0, 8));

        VBox daysFieldContainer = new VBox();
        Label daysLabel = new Label("day(s)");
        daysLabel.setAlignment(Pos.CENTER_RIGHT);
        daysLabel.setMaxWidth(UtilityUI.WIDTH);
        daysLabel.setFont(UtilityUI.getFont());
        daysLabel.setTextFill(Color.WHITE);
        daysField = new InputField();
        daysField.setNumInputOnly(true);
        daysField.setTextLimit(5);
        // VBox.setMargin(daysLabel, new Insets(3, 5, 0, 5));
        // VBox.setMargin(daysField, new Insets(-3, 0, 0, 0));
        VBox.setVgrow(filler2, Priority.ALWAYS);
        daysFieldContainer.getChildren().addAll(filler2, daysField, daysLabel);
        container.getChildren().addAll(dateFieldContainer, separator, daysFieldContainer);

        VBox.setMargin(container, new Insets(10));
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
        button.setMaxSize(150, 40);
        button.setMinSize(150, 40);
        container.setCenter(button);

        VBox.setMargin(container, new Insets(30, 0, 40, 0));
        return container;
    }
}
