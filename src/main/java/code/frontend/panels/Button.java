package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
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

    private Font labelFont;
    private Color feedbackColour;
    private Color colour;
    private double borderThickness;

    private Pane animPane = new Pane();
    private FadeTransition ft = new FadeTransition();
    private boolean animationsEnabled;

    private Label label;
    private CustomBox border;

    private boolean enabled;

    public Button(String text) {
        this.labelFont = Vals.FontTools.getButtonFont();
        this.feedbackColour = Vals.Colour.FEEDBACK;
        this.colour = Color.WHITE;
        this.borderThickness = Vals.GraphicalUI.BTTN_THICKNESS;
        this.enabled = true;
        this.animationsEnabled = true;

        this.border = new CustomBox(borderThickness);
        CustomBox.applyCustomBorder(this, border);

        this.label = new Label(text);
        this.label.setTextFill(colour);
        this.label.setAlignment(Pos.CENTER);
        this.label.setFont(labelFont);
        this.label.prefWidthProperty().bind(this.widthProperty());
        this.label.prefHeightProperty().bind(this.heightProperty());
        this.label.relocate(0, 0);
        this.label.setViewOrder(2);

        double vertiInset = border.getVertiPadding() + 3;
        double horizInset = border.getHorizPadding() + 4;
        Insets animPaneInsets = new Insets(vertiInset, horizInset, vertiInset, horizInset);
        BackgroundFill bgFill =
            new BackgroundFill(feedbackColour, new CornerRadii(12), animPaneInsets);
        this.animPane.setBackground(new Background(bgFill));
        this.animPane.prefWidthProperty().bind(this.widthProperty());
        this.animPane.prefHeightProperty().bind(this.heightProperty());
        this.animPane.relocate(0, 0);
        this.animPane.setOpacity(0);
        this.animPane.setViewOrder(1);
        this.ft.setNode(animPane);

        this.getChildren().addAll(label, animPane);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (enabled) {
                    executeOnClick();
                    playClickAnim();
                }
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (enabled) {
                    playMouseEnterAnim();
                }
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (enabled) {
                    playMouseExitAnim();
                }
            }
        });
    }

    public abstract void executeOnClick();

    private void playClickAnim() {
        if (!animationsEnabled)
            return;
        this.ft.stop();
        this.ft.setDuration(Duration.millis(200));
        this.ft.setFromValue(0.8);
        this.ft.setToValue(0);
        this.ft.playFromStart();
    }

    private void playMouseEnterAnim() {
        if (!animationsEnabled) {
            this.animPane.setOpacity(1);
            return;
        }
        this.ft.stop();
        this.ft.setDuration(Duration.millis(300));
        this.ft.setFromValue(0);
        this.ft.setToValue(HOVER_OPACITY);
        this.ft.playFromStart();
    }

    private void playMouseExitAnim() {
        if (!animationsEnabled) {
            this.animPane.setOpacity(0);
            return;
        }
        this.ft.stop();
        this.ft.setDuration(Duration.millis(300));
        this.ft.setFromValue(HOVER_OPACITY);
        this.ft.setToValue(0);
        this.ft.playFromStart();
    }

    public Label getLabel() {
        return label;
    }

    public void setFeedbackColour(Color feedbackColour) {
        this.feedbackColour = feedbackColour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
        this.border.setStrokeColour(colour);
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public void setTextLabel(String text) {
        this.label.setText(text);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.border.setStrokeColour(colour);
            this.label.setTextFill(colour);
        } else {
            this.border.setStrokeColour(Colour.DISABLED);
            this.label.setTextFill(Colour.DISABLED);
        }
        this.enabled = enabled;
    }

    public void setAnimationsEnabled(boolean animationsEnabled) {
        this.animationsEnabled = animationsEnabled;
    }
}
