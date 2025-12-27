package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class Button extends Pane {
    private final double HOVER_OPACITY = 0.2;
    private Font labelFont = Vals.FontTools.getButtonFont();
    private double borderThickness = Vals.GraphicalUI.DRAW_THICKNESS;
    private Color feedbackColour = Vals.Colour.FEEDBACK;
    private Pane animPane = new Pane();
    private FadeTransition ft = new FadeTransition();

    public Button(String text) {
        CustomBox border = new CustomBox(borderThickness);
        CustomBox.applyCustomBorder(this, border);

        Label label = new Label(text);
        label.setTextFill(border.getStrokeColour());
        label.setAlignment(Pos.CENTER);
        label.setFont(labelFont);
        label.prefWidthProperty().bind(this.widthProperty());
        label.prefHeightProperty().bind(this.heightProperty());
        label.relocate(0, 0);
        label.setViewOrder(2);

        double vertiInset = border.getVertiPadding() + 3;
        double horizInset = border.getHorizPadding() + 4;
        Insets animPaneInsets = new Insets(vertiInset, horizInset, vertiInset, horizInset);
        BackgroundFill bgFill =
            new BackgroundFill(feedbackColour, new CornerRadii(12), animPaneInsets);
        animPane.setBackground(new Background(bgFill));
        animPane.prefWidthProperty().bind(this.widthProperty());
        animPane.prefHeightProperty().bind(this.heightProperty());
        animPane.relocate(0, 0);
        animPane.setOpacity(0);
        animPane.setViewOrder(1);
        ft.setNode(animPane);

        this.getChildren().addAll(label, animPane);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                executeOnClick();
                playClickAnim();
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playMouseEnterAnim();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playMouseExitAnim();
            }
        });
    }

    public abstract void executeOnClick();

    private void playClickAnim() {
        ft.stop();
        ft.setDuration(Duration.millis(200));
        ft.setFromValue(0.8);
        ft.setToValue(0);
        ft.playFromStart();
    }

    private void playMouseEnterAnim() {
        ft.stop();
        ft.setDuration(Duration.millis(300));
        ft.setFromValue(0);
        ft.setToValue(HOVER_OPACITY);
        ft.playFromStart();
    }

    private void playMouseExitAnim() {
        ft.stop();
        ft.setDuration(Duration.millis(300));
        ft.setFromValue(HOVER_OPACITY);
        ft.setToValue(0);
        ft.playFromStart();
    }

    public void setFeedbackColour(Color feedbackColour) {
        this.feedbackColour = feedbackColour;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }
}
