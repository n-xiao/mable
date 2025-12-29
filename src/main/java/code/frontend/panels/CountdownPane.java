package code.frontend.panels;

import code.backend.Countdown;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.GraphicalUI;
import java.time.LocalDate;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

public class CountdownPane extends VBox {
    public static final double WIDTH = 280;
    public static final double HEIGHT = 140;
    public static final double NAME_WIDTH = 150;
    public static final double DIV_WIDTH = 10;
    public static final double CONTENT_HEIGHT = 100;

    private HBox hoverHBox; // container
    private HBox contentHBox; // container
    private Label statusLabel; // for displaying the status on mouse hover
    private Label endDateLabel; // for displaying the due date on mouse hover
    private FadeTransition ft; // for ui hover animation
    private Countdown countdown; // points to the backend object
    private CustomBox border; // for selection ui indication
    private boolean selected; // for selection detection
    private CountdownPane prev; // for multi-select functionality

    public CountdownPane(Countdown cd, LocalDate now) {
        this.prev = null;
        this.countdown = cd;
        this.selected = false;
        this.setAlignment(Pos.CENTER);
        initContentHBox(now);
        initHoverHBox();
        initSelectable(this);
        this.getChildren().addAll(this.hoverHBox, this.contentHBox);
    }

    private void initHoverHBox() {
        int leftRightPadding = 16;
        double height = HEIGHT - CONTENT_HEIGHT;
        this.hoverHBox = new HBox();
        hoverHBox.setPrefSize(WIDTH, height);
        Font font = Font.font(Vals.FontTools.FONT_FAM, FontWeight.MEDIUM, FontPosture.ITALIC, 14);
        statusLabel = new Label();
        statusLabel.setAlignment(Pos.BOTTOM_LEFT);
        statusLabel.setFont(font);
        statusLabel.setTextFill(Vals.Colour.TXT_GHOST);
        statusLabel.setMaxSize(WIDTH / 2, height);
        HBox.setMargin(statusLabel, new Insets(0, 0, 0, leftRightPadding));
        Pane spacer = new Pane();
        spacer.setMaxSize(WIDTH, height);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        endDateLabel = new Label();
        endDateLabel.setAlignment(Pos.BOTTOM_RIGHT);
        endDateLabel.setFont(font);
        endDateLabel.setTextFill(Vals.Colour.TXT_GHOST);
        endDateLabel.setMaxSize(WIDTH / 2, height);
        HBox.setMargin(endDateLabel, new Insets(0, leftRightPadding, 0, 0));
        hoverHBox.setFillHeight(true);
        hoverHBox.getChildren().addAll(statusLabel, spacer, endDateLabel);

        ft = new FadeTransition(Duration.millis(300), hoverHBox);
        // hoverHBox.setBackground(new Background(new BackgroundFill(Color.VIOLET, null, null)));

        // todo mouse listener stuff
        contentHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ft.stop();
                if (selected)
                    return; // do nothing if selected
                setMouseEnterAnim(ft);
                // TODO: put this in its own update method later, to be called by a watchdog
                LocalDate now = LocalDate.now();
                String status = countdown.getStatusString(now);
                String end = countdown.getStringDueDate(now);
                statusLabel.setText(status);
                endDateLabel.setText("Due: " + end);
                ft.playFromStart();
            }
        });

        contentHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected)
                    return; // do nothing if selected
                ft.stop();
                setMouseExitAnim(ft);
                ft.playFromStart();
            }
        });
    }

    private void setMouseEnterAnim(FadeTransition anim) {
        anim.setFromValue(0);
        anim.setToValue(1);
    }

    private void setMouseExitAnim(FadeTransition anim) {
        anim.setFromValue(1);
        anim.setToValue(0);
    }

    private void initContentHBox(LocalDate now) {
        this.contentHBox = new HBox();
        contentHBox.setPrefSize(WIDTH, CONTENT_HEIGHT);
        contentHBox.setFillHeight(true);
        // adds the border
        this.border = new CustomBox(GraphicalUI.DRAW_THICKNESS);
        CustomBox.applyCustomBorder(contentHBox, border);
        // adds the name display
        contentHBox.getChildren().add(createNameLabel(countdown));
        // adds the divider
        contentHBox.getChildren().add(createVerticalDivider());
        // adds the day countdown pane
        contentHBox.getChildren().add(createCountdownDisplay(countdown, now));

        // contentHBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
    }

    private Label createNameLabel(Countdown cd) {
        String name = cd.getName();
        Label nameLabel = new Label(name);
        Font nameFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.SEMI_BOLD, 17);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTextAlignment(TextAlignment.JUSTIFY);
        nameLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        nameLabel.setFont(nameFont);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setPrefWidth(NAME_WIDTH);
        nameLabel.prefHeightProperty().bind(this.heightProperty());

        HBox.setMargin(nameLabel, new Insets(10, -2, 10, 10));
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        return nameLabel;
    }

    private Pane createVerticalDivider() {
        Pane pane = new Pane();
        Color colour = Color.rgb(255, 255, 255, 0.3);
        CustomLine separator = new CustomLine(2, CustomLine.Type.VERTICAL_TYPE);
        pane.setPrefSize(DIV_WIDTH, HEIGHT);
        separator.setStrokeColour(colour);
        separator.setPadding(20);
        CustomLine.applyCustomBorder(pane, separator);
        return pane;
    }

    private VBox createCountdownDisplay(Countdown cd, LocalDate now) {
        VBox display = new VBox();
        int daysLeft = Math.abs(cd.daysUntilDue(now));

        Label numLabel = new Label(Vals.GraphicalUI.intToString(daysLeft));
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

    public void applyDeselectStyle() {
        ft.stop();
        hoverHBox.setOpacity(1);
        border.setStrokeColour(Color.WHITE);
        statusLabel.setTextFill(Vals.Colour.TXT_GHOST);
        endDateLabel.setTextFill(Vals.Colour.TXT_GHOST);
    }

    public void applySelectStyle() {
        ft.stop();
        hoverHBox.setOpacity(1);
        border.setStrokeColour(Vals.Colour.SELECTED);
        statusLabel.setTextFill(Vals.Colour.SELECTED);
        endDateLabel.setTextFill(Vals.Colour.SELECTED);
    }

    private void initSelectable(CountdownPane thisInstance) {
        contentHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected) {
                    // deselect procedure
                    applyDeselectStyle();
                    CountdownPaneView.getInstance().removeSelected(thisInstance);
                    CountdownPaneControls.getInstance().updateSelectionButtonIndicators();
                } else {
                    // select procedure
                    applySelectStyle();
                    CountdownPaneView.getInstance().addSelected(thisInstance);
                    CountdownPaneControls.getInstance().updateSelectionButtonIndicators();
                }
                selected = !selected;
            }
        });
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
