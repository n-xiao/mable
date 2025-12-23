package code.frontend.panels;

import java.time.Instant;

import code.backend.Countdown;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.FontVar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class CountdownPane extends VBox {
    public final double WIDTH = 290;
    public final double HEIGHT = 140;
    public final double NAME_WIDTH = 150;
    public final double DIV_WIDTH = 10;
    // public final double COUNT_WIDTH = WIDTH - NAME_WIDTH - DIV_WIDTH - 50;
    public final double CONTENT_HEIGHT = 100;

    private HBox hoverHBox;
    private HBox contentHBox;

    public CountdownPane(Countdown cd, Instant now) {
        this.setAlignment(Pos.CENTER);
        initHoverHBox();
        initContentHBox(cd, now);
        this.getChildren().addAll(this.hoverHBox, this.contentHBox);
    }

    private void initHoverHBox() {
        this.hoverHBox = new HBox();
        hoverHBox.setPrefSize(this.WIDTH, this.HEIGHT - this.CONTENT_HEIGHT);
        // VBox.setMargin(hoverHBox, new Insets(0, 0, 1, 0));
        // hoverHBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

        // todo mouse listener stuff
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
        Font nameFont = Font.font(Vals.FontTools.getFontName(FontVar.REGULAR), 16);
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
        Color colour = Color.rgb(255, 255, 255, 0.5);
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
        Font numFont = Font.font(Vals.FontTools.getFontName(FontVar.BOLD), 30);
        numLabel.setAlignment(Pos.CENTER);
        numLabel.setTextAlignment(TextAlignment.CENTER);
        numLabel.setFont(numFont);
        numLabel.setTextFill(Color.WHITE);
        numLabel.prefWidthProperty().bind(display.widthProperty());
        // numLabel.setPrefWidth(COUNT_WIDTH);
        // VBox.setMargin(numLabel, new Insets(5, 0, 0, 0));
        display.getChildren().add(numLabel);

        Font textFont = Font.font(Vals.FontTools.getFontName(FontVar.BOLD_ITALIC), 13);
        String textNoun = (daysLeft != 1) ? "DAYS" : "DAY";
        String textAdverb = (cd.isOverdue(now)) ? "AGO" : "LEFT";
        Label textLabel = new Label(textNoun + "\n" + textAdverb);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setFont(textFont);
        textLabel.setTextFill(Color.WHITE);
        textLabel.prefWidthProperty().bind(display.widthProperty());
        // textLabel.setPrefWidth(COUNT_WIDTH);
        display.getChildren().add(textLabel);
        // display.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        // display.setMinWidth(COUNT_WIDTH);
        HBox.setMargin(display, new Insets(10, 10, 10, 0));
        HBox.setHgrow(display, Priority.ALWAYS);
        // display.setPrefWidth(COUNT_WIDTH);
        return display;
    }
}
