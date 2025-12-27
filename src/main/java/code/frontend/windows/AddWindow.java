package code.frontend.windows;

import code.backend.Countdown;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.UtilityUI;
import code.frontend.panels.Button;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private static Button button;

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
        nameField = createNameInputField();
        dateField = createDateInputField();
        daysField = createDaysInputField();
        button = createButton("create!");
        Pane namePart = createNamePart();
        Pane duePart = createDuePart();
        Pane buttonPart = createButtonPart();
        createDateDayLink();
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
        dateField.setMaxWidth(300);
        dateField.setMinWidth(300);
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
        separator.setMaxWidth(UtilityUI.WIDTH);
        HBox.setHgrow(separator, Priority.ALWAYS);
        HBox.setMargin(separator, new Insets(0, 8, 0, 8));

        VBox daysFieldContainer = new VBox();
        Label daysLabel = new Label("day(s)");
        daysLabel.setAlignment(Pos.CENTER_RIGHT);
        daysLabel.setMaxWidth(UtilityUI.WIDTH);
        daysLabel.setFont(UtilityUI.getFont());
        daysLabel.setTextFill(Color.WHITE);
        daysField.setNumInputOnly(true);
        daysField.setTextLimit(7);
        daysField.setMaxWidth(150);
        daysField.setMinWidth(150);
        daysField.getTextField().setAlignment(Pos.CENTER);
        // VBox.setMargin(daysLabel, new Insets(3, 5, 0, 5));
        // VBox.setMargin(daysField, new Insets(-3, 0, 0, 0));
        VBox.setVgrow(filler2, Priority.ALWAYS);
        daysFieldContainer.getChildren().addAll(filler2, daysField, daysLabel);
        container.getChildren().addAll(dateFieldContainer, separator, daysFieldContainer);
        VBox.setMargin(container, new Insets(10, 14, 10, 14));

        return container;
    }

    private static Pane createButtonPart() {
        BorderPane container = new BorderPane();
        container.setPrefWidth(UtilityUI.WIDTH);
        button.setMaxSize(150, 40);
        button.setMinSize(150, 40);
        container.setCenter(button);

        VBox.setMargin(container, new Insets(30, 0, 40, 0));
        return container;
    }

    private static void createDateDayLink() {
        TextField[] dateTextFields = new TextField[3];
        dateTextFields[0] = dateField.getDayInput().getTextField();
        dateTextFields[1] = dateField.getMonthInput().getTextField();
        dateTextFields[2] = dateField.getYearInput().getTextField();
        TextField dayTextField = daysField.getTextField();

        for (TextField textField : dateTextFields) {
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!textField.isFocused())
                        return;
                    LocalDate date = dateField.getLocalDateInput(true);
                    if (date != null) {
                        int daysBetween = Countdown.getDaysBetween(LocalDate.now(), date);
                        dayTextField.setText(Integer.toString(daysBetween));
                    }
                }
            });
        }

        dayTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!dayTextField.isFocused())
                    return;
                LocalDate date = LocalDate.now();
                try {
                    date = date.plusDays(Integer.parseInt(newValue));
                } catch (NumberFormatException e) {
                    return;
                }
                dateField.setLocalDateInput(date);
            }
        });
    }

    /**
     * By default, this just returns a new InputField before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected static InputField createNameInputField() {
        return new InputField();
    }

    /**
     * By default, this just returns a new DateInputField before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected static DateInputField createDateInputField() {
        return new DateInputField();
    }

    /**
     * By default, this just returns a new InputField before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected static InputField createDaysInputField() {
        return new InputField();
    }

    /**
     * By default, this just returns a new Button before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected static Button createButton(String buttonText) {
        return new Button(buttonText) {
            @Override
            public void executeOnClick() {
                // TODO Auto-generated method stub
            }
        };
    }
}
