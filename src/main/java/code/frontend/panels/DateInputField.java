package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateInputField extends VBox {
    private Label hintLabel;
    private String format;
    private CustomBox border;
    private InputField dayInput;
    private InputField monthInput;
    private InputField yearInput;
    private FadeTransition ft;
    private HBox contents;

    public DateInputField() {
        contents = new HBox();
        contents.setFillHeight(true);
        contents.prefWidthProperty().bind(this.widthProperty());
        this.setFillWidth(true);
        format = "dd/MM/yyyy"; // to be made configurable later, for the USA peeps
        hintLabel = new Label("please follow this format: " + format.toLowerCase());
        hintLabel.setTextFill(Vals.Colour.ERROR);
        hintLabel.setOpacity(0);
        hintLabel.setFont(
            Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 10));
        border = new CustomBox(Vals.GraphicalUI.INPUT_BORDER_WIDTH);
        CustomBox.applyCustomBorder(contents, border);
        dayInput = new InputField();
        monthInput = new InputField();
        yearInput = new InputField();
        configInputs(dayInput, monthInput, yearInput);
        yearInput.setTextLimit(4);
        ft = new FadeTransition();
        ft.setNode(hintLabel);

        contents.getChildren().addAll(
            dayInput, createSlashSeparator(), monthInput, createSlashSeparator(), yearInput);
    }

    private void configInputs(InputField... inputs) {
        for (InputField input : inputs) {
            input.setBorderColour(Color.rgb(0, 0, 0, 0));
            input.setNumInputOnly(true);
            input.setPrefWidth(80);
            input.prefHeightProperty().bind(contents.heightProperty());
            input.setTextLimit(2);
            input.setMinHeight(Vals.GraphicalUI.INPUT_MIN_HEIGHT);
            input.setMinWidth(70);
            HBox.setMargin(input, new Insets(5));
        }
    }

    private Label createSlashSeparator() {
        Label slash = new Label("/");
        slash.setFont(Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, 12));
        slash.setTextFill(Color.WHITE);
        slash.prefHeightProperty().bind(contents.heightProperty());
        slash.setPrefWidth(10);
        return slash;
    }

    public void triggerFormatHint() {
        ft.stop();
        ft.setDuration(Duration.millis(200));
        ft.setCycleCount(3);
        ft.setAutoReverse(true);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.playFromStart();
        // hintLabel.setTextFill(Color.WHITE);
        hintLabel.setOpacity(1);
    }

    /**
     * Returns a LocalDate while handling sanitisation. If a LocalDate cannot be parsed,
     * this method will return null. Any backend code calling this method should take
     * that into account.
     *
     * @return LocalDate
     */
    public LocalDate getLocalDateInput() {
        String day = dayInput.getTextField().getText();
        String month = monthInput.getTextField().getText();
        String year = monthInput.getTextField().getText();
        String dateString = day + "-" + month + "-" + year;
        DateTimeFormatter format = DateTimeFormatter.ofPattern(this.format);
        try {
            return LocalDate.parse(dateString, format);
        } catch (DateTimeParseException e) {
            triggerFormatHint();
            return null;
        }
    }
}
