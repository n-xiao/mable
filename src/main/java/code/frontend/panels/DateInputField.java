package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

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
        contents.setMaxHeight(Vals.GraphicalUI.INPUT_MIN_HEIGHT);
        this.setFillWidth(true);
        format = "dd-MM-yyyy"; // to be made configurable later, for the USA peeps
        hintLabel = new Label("please use this format: " + format.toLowerCase());
        hintLabel.setTextFill(Vals.Colour.ERROR);
        hintLabel.setOpacity(0);
        hintLabel.setFont(Font.font(Vals.FontTools.FONT_FAM + " Medium Italic", 13));
        hintLabel.setAlignment(Pos.CENTER);
        hintLabel.maxWidthProperty().bind(this.widthProperty());
        border = new CustomBox(Vals.GraphicalUI.INPUT_BORDER_WIDTH);
        CustomBox.applyCustomBorder(contents, border);
        dayInput = new InputField();
        monthInput = new InputField();
        yearInput = new InputField();
        configInputs(dayInput, monthInput, yearInput);
        yearInput.setTextLimit(4);
        ft = new FadeTransition();
        ft.setNode(hintLabel);

        VBox.setMargin(hintLabel, new Insets(3, 0, 0, 0));
        contents.getChildren().addAll(
            dayInput, createSlashSeparator(), monthInput, createSlashSeparator(), yearInput);
        this.getChildren().addAll(contents, hintLabel);
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
            input.getTextField().setAlignment(Pos.CENTER);
            HBox.setMargin(input, new Insets(2, 5, 2, 5));
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

    private void triggerFormatHint() {
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
    public LocalDate getLocalDateInput(boolean silent) {
        String day = dayInput.getTextField().getText();
        String month = monthInput.getTextField().getText();
        String year = yearInput.getTextField().getText();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("00");
            day = decimalFormat.format(Integer.parseInt(day));
            month = decimalFormat.format(Integer.parseInt(month));
            String dateString = day + "-" + month + "-" + year;
            DateTimeFormatter format = DateTimeFormatter.ofPattern(this.format);
            return LocalDate.parse(dateString, format);
        } catch (Exception e) {
            if (!silent)
                triggerFormatHint();
            return null;
        }
    }

    public void setLocalDateInput(LocalDate date) {
        dayInput.getTextField().setText(Integer.toString(date.getDayOfMonth()));
        monthInput.getTextField().setText(Integer.toString(date.getMonthValue()));
        yearInput.getTextField().setText(Integer.toString(date.getYear()));
    }

    public InputField getDayInput() {
        return dayInput;
    }

    public InputField getMonthInput() {
        return monthInput;
    }

    public InputField getYearInput() {
        return yearInput;
    }
}
