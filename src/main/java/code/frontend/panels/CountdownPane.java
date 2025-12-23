package code.frontend.panels;

import java.time.Instant;
import java.time.LocalDate;

import code.backend.Countdown;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.misc.Vals;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CountdownPane extends VBox {
    public final double WIDTH = 290;
    public final double HEIGHT = 140;
    public final double NAME_WIDTH = 150;
    public final double DIV_WIDTH = 10;
    public final double CONTENT_HEIGHT = 100;

    private HBox hoverHBox;
    private HBox contentHBox;

    public CountdownPane(Countdown cd, Instant now) {
        this.setAlignment(Pos.CENTER);
        initHoverHBox(cd);
        initContentHBox(cd, now);
        this.getChildren().addAll(this.hoverHBox, this.contentHBox);
    }

    private void initHoverHBox(Countdown cd) {
        int leftRightPadding = 18;
        double height = this.HEIGHT - this.CONTENT_HEIGHT;
        this.hoverHBox = new HBox();
        hoverHBox.setPrefSize(this.WIDTH, height);
        Font font = Font.font(Vals.FontTools.FONT_FAM, FontWeight.LIGHT, FontPosture.ITALIC, 14);
        Label statusLabel = new Label();
        statusLabel.setAlignment(Pos.BOTTOM_LEFT);
        statusLabel.setFont(font);
        statusLabel.setTextFill(Vals.Colour.TXT_GHOST);
        statusLabel.setMaxSize(this.WIDTH / 2, height);
        HBox.setMargin(statusLabel, new Insets(0, 0, 0, leftRightPadding));
        Pane spacer = new Pane();
        spacer.setMaxSize(this.WIDTH, height);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label endDateLabel = new Label();
        endDateLabel.setAlignment(Pos.BOTTOM_RIGHT);
        endDateLabel.setFont(font);
        endDateLabel.setTextFill(Vals.Colour.TXT_GHOST);
        endDateLabel.setMaxSize(this.WIDTH / 2, height);
        HBox.setMargin(endDateLabel, new Insets(0, leftRightPadding, 0, 0));
        hoverHBox.setFillHeight(true);
        hoverHBox.getChildren().addAll(statusLabel, spacer, endDateLabel);
        // hoverHBox.setBackground(new Background(new BackgroundFill(Color.VIOLET, null, null)));

        // todo mouse listener stuff
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String status = cd.getStatusString(Instant.now());
                String end = cd.getLocalDueDate(Instant.now(), LocalDate.now()).toString();
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initContentHBox(Countdown cd, Instant now) {
        this.contentHBox = new HBox();
        contentHBox.setPrefSize(WIDTH, CONTENT_HEIGHT);
        contentHBox.setFillHeight(true);
        // adds the border
        CustomBox border = new CustomBox(2);
        CustomBox.applyToPane(contentHBox, border);
        // adds the name display
        contentHBox.getChildren().add(createNameLabel(cd));
        // adds the divider
        contentHBox.getChildren().add(createVerticalDivider());
        // adds the day countdown pane
        contentHBox.getChildren().add(createCountdownDisplay(cd, now));

        // contentHBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
    }

    private Label createNameLabel(Countdown cd) {
        String name = cd.getName();
        Label nameLabel = new Label(name);
        Font nameFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.SEMI_BOLD, 19);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTextAlignment(TextAlignment.JUSTIFY);
        nameLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        nameLabel.setFont(nameFont);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setPrefSize(NAME_WIDTH, HEIGHT);

        HBox.setMargin(nameLabel, new Insets(10));
        return nameLabel;
    }

    private Pane createVerticalDivider() {
        Pane pane = new Pane();
        Color colour = Color.rgb(255, 255, 255, 0.3);
        CustomLine separator = new CustomLine(2, CustomLine.Type.VERTICAL_TYPE);
        pane.setPrefSize(DIV_WIDTH, HEIGHT);
        separator.setColour(colour);
        separator.setPadding(20);
        CustomLine.applyToPane(pane, separator);
        return pane;
    }

    private VBox createCountdownDisplay(Countdown cd, Instant now) {
        VBox display = new VBox();
        int daysLeft = Math.abs(cd.daysUntilDue(now));

        Label numLabel = new Label(Integer.toString(daysLeft));
        Font numFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 30);
        numLabel.setAlignment(Pos.CENTER);
        numLabel.setTextAlignment(TextAlignment.CENTER);
        numLabel.setFont(numFont);
        numLabel.setTextFill(Color.WHITE);
        numLabel.prefWidthProperty().bind(display.widthProperty());
        // numLabel.setPrefWidth(COUNT_WIDTH);
        // VBox.setMargin(numLabel, new Insets(5, 0, 0, 0));
        display.getChildren().add(numLabel);

        Font textFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13);
        String textNoun = (daysLeft != 1) ? "DAYS" : "DAY";
        String textAdverb = (cd.isOverdue(now)) ? "AGO" : "LEFT";
        Label textLabel = new Label(textNoun + "\n" + textAdverb);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setFont(textFont);
        textLabel.setTextFill(Color.WHITE);
        textLabel.prefWidthProperty().bind(display.widthProperty());
        display.getChildren().add(textLabel);
        HBox.setMargin(display, new Insets(10, 10, 10, 0));
        HBox.setHgrow(display, Priority.ALWAYS);
        return display;
    }
}
