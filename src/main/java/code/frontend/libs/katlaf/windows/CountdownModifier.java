/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf.windows;

import code.backend.data.Countdown;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.Button;
import code.frontend.libs.katlaf.inputfields.DateInputField;
import code.frontend.libs.katlaf.inputfields.InputField;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class CountdownModifier extends Stage {
    public static final int WIDTH = 510;
    public static final int HEIGHT = 330;
    private static final Insets INSETS = new Insets(7);

    private VBox root;

    private InputField nameField;
    private DateInputField dateField;
    private InputField daysField;
    private Button button;

    protected CountdownModifier() {
        this.setStageStyling();
        this.configureChildren();
    }

    protected void setStageStyling() {
        this.setResizable(false);
        this.initStyle(StageStyle.DECORATED);
        this.setMinWidth(WIDTH);
        this.setMinHeight(HEIGHT);
        this.setMaxWidth(WIDTH);
        this.setMaxHeight(HEIGHT);

        this.root = new VBox();
        this.root.setPrefSize(WIDTH, HEIGHT);
        this.root.relocate(0, 0);
        this.root.setBackground(null);

        Scene scene = new Scene(this.root);
        scene.setFill(RiceHandler.getColour("background1"));
        this.setScene(scene);
    }

    protected void configureChildren() {
        nameField = createNameInputField();
        dateField = createDateInputField();
        daysField = createDaysInputField();
        button = createButton();
        Pane namePart = createNamePart();
        Pane duePart = createDuePart();
        Pane buttonPart = createButtonPart();
        createDateDayLink();
        root.getChildren().addAll(namePart, duePart, buttonPart);
    }

    private Pane createNamePart() {
        VBox container = new VBox();
        container.setFillWidth(true);

        Label label = new Label(getNameInputuserHint());
        label.setTextFill(RiceHandler.getColour());
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(WIDTH);
        label.setFont(FontHandler.getHeading(3));
        nameField.setFieldMargins(INSETS);
        nameField.setMaxWidth(WIDTH);
        VBox.setMargin(label, new Insets(0, 0, 0, 8));

        container.getChildren().addAll(label, nameField);

        VBox.setMargin(container, new Insets(10));
        return container;
    }

    private Pane createDuePart() {
        Pane filler1 = new Pane();
        Pane filler2 = new Pane();
        filler1.setVisible(false);
        filler2.setVisible(false);

        HBox container = new HBox();

        VBox dateFieldContainer = new VBox();
        Label dateLabel = new Label(getDateInputuserHint());
        dateLabel.setAlignment(Pos.CENTER_LEFT);
        dateLabel.setMaxWidth(WIDTH);
        dateLabel.setFont(FontHandler.getHeading(3));
        dateLabel.setTextFill(RiceHandler.getColour());
        dateField.setMaxWidth(300);
        dateField.setMinWidth(300);
        dateField.setFieldMargins(INSETS);
        VBox.setMargin(dateLabel, new Insets(0, 0, 0, 8));
        VBox.setVgrow(filler1, Priority.ALWAYS);
        dateFieldContainer.getChildren().addAll(dateLabel, dateField, filler1);

        Label separator = new Label("or in");
        separator.setMaxHeight(HEIGHT);
        separator.setAlignment(Pos.CENTER);
        separator.setFont(FontHandler.getHeading(3));
        separator.setTextFill(RiceHandler.getColour());
        separator.setMaxWidth(WIDTH);
        HBox.setHgrow(separator, Priority.ALWAYS);
        HBox.setMargin(separator, new Insets(0, 8, 0, 8));

        VBox daysFieldContainer = new VBox();
        Label daysLabel = new Label("day(s)");
        daysLabel.setAlignment(Pos.CENTER_RIGHT);
        daysLabel.setMaxWidth(WIDTH);
        daysLabel.setFont(FontHandler.getHeading(3));
        daysLabel.setTextFill(RiceHandler.getColour());
        daysField.setNumInputOnly(true);
        daysField.setTextLimit(7);
        daysField.setFieldMargins(INSETS);
        daysField.setMaxWidth(130);
        daysField.setMinWidth(130);
        daysField.getTextField().setAlignment(Pos.CENTER);
        VBox.setVgrow(filler2, Priority.ALWAYS);
        daysFieldContainer.getChildren().addAll(filler2, daysField, daysLabel);
        container.getChildren().addAll(dateFieldContainer, separator, daysFieldContainer);
        VBox.setMargin(container, new Insets(10, 14, 10, 14));

        return container;
    }

    private Pane createButtonPart() {
        BorderPane container = new BorderPane();
        container.setPrefWidth(WIDTH);
        button.setMaxSize(125, 35);
        button.setMinSize(125, 35);
        container.setCenter(button);

        VBox.setMargin(container, new Insets(30, 0, 40, 0));
        return container;
    }

    private void createDateDayLink() {
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
    protected InputField createNameInputField() {
        return new InputField();
    }

    /**
     * By default, this just returns a new DateInputField before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected DateInputField createDateInputField() {
        return new DateInputField();
    }

    /**
     * By default, this just returns a new InputField before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected InputField createDaysInputField() {
        return new InputField();
    }

    /**
     * By default, this just returns a new Button before any
     * styling has been applied to it by this class. Child classes
     * can override this method to add properties to the object
     * before it is styled. Avoid applying changes that may impact
     * its layout (e.g applying insets or changing its width/height).
     */
    protected Button createButton() {
        return new Button("button") {
            @Override
            public void executeOnClick(MouseEvent event) {}
        };
    }

    protected InputField getNameField() {
        return nameField;
    }

    protected DateInputField getDateField() {
        return dateField;
    }

    protected abstract String getNameInputuserHint();

    protected abstract String getDateInputuserHint();
}
