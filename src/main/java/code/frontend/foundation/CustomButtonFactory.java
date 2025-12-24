package code.frontend.foundation;

import code.frontend.misc.Vals;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class CustomButtonFactory {
    private Color feedbackColour = Vals.Colour.FEEDBACK;
    private int btnBorderThickness = Vals.GraphicalUI.DRAW_THICKNESS;
    private Font labelFont = new Font(Vals.FontTools.FONT_FAM, 13);
    private FadeTransition hoverAnim = null;
    private FadeTransition clickAnim = null;
    private boolean useHoverAnim = true;

    public CustomButtonFactory() {}

    public abstract void executeOnClick();

    /**
     * Modifies a given pane to become a clickable button.
     * bgInsets should be specified, since the button
     * might be enclosed within a CustomBox
     *
     * @param pane
     * @param cbf
     * @param bgInsets
     */
    public static void setup(Pane pane, CustomButtonFactory cbf, double bgInsets) {
        Pane clickPane = new Pane();
        clickPane.prefWidthProperty().bind(pane.widthProperty());
        clickPane.prefHeightProperty().bind(pane.heightProperty());
        clickPane.relocate(0, 0);
        clickPane.setBackground(Vals.Colour.createBG(cbf.feedbackColour, 10, bgInsets));
        clickPane.setOpacity(0);

        cbf.clickAnim = new FadeTransition(Duration.millis(200), clickPane);
        cbf.clickAnim.setFromValue(1);
        cbf.clickAnim.setToValue(0);
        clickPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cbf.hoverAnim != null)
                    cbf.hoverAnim.stop();
                cbf.executeOnClick();
                if (cbf.feedbackColour != null)
                    cbf.clickAnim.playFromStart();
            }
        });

        if (cbf.useHoverAnim) {
            Pane hoverPane = new Pane();
            double hoverOpacity = 0.5;
            cbf.hoverAnim = new FadeTransition(Duration.millis(300), hoverPane);
            hoverPane.setBackground(Vals.Colour.createBG(cbf.feedbackColour, 10, bgInsets));
            hoverPane.setOpacity(0);
            hoverPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (cbf.clickAnim != null
                            && cbf.clickAnim.getStatus().equals(Animation.Status.RUNNING))
                        return;
                    cbf.hoverAnim.setFromValue(0);
                    cbf.hoverAnim.setToValue(hoverOpacity);
                    cbf.hoverAnim.playFromStart();
                }
            });

            hoverPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (cbf.clickAnim != null
                            && cbf.clickAnim.getStatus().equals(Animation.Status.RUNNING))
                        return;
                    cbf.hoverAnim.setFromValue(hoverOpacity);
                    cbf.hoverAnim.setToValue(0);
                    cbf.hoverAnim.playFromStart();
                }
            });
        }

        pane.getChildren().add(clickPane);
    }

    public static Pane createButton(String btnText, CustomButtonFactory cbf) {
        Pane btn = new Pane();
        CustomBox border = new CustomBox(cbf.btnBorderThickness);
        CustomBox.applyToPane(btn, border);

        Label label = new Label(btnText);
        label.setAlignment(Pos.CENTER);
        label.setFont(cbf.labelFont);
        label.prefWidthProperty().bind(btn.widthProperty());
        label.prefHeightProperty().bind(btn.heightProperty());
        label.relocate(0, 0);
        btn.getChildren().add(label);

        setup(btn, cbf, cbf.btnBorderThickness * 2);

        return btn;
    }

    /**
     * Sets the colour of the feedback that is played using a FadeTransition
     * to indicate to the user that the button was clicked. Setting
     * feedbackColour to null will stop the transition from playing.
     *
     * @param feedbackColour
     */
    public void setFeedbackColour(Color feedbackColour) {
        this.feedbackColour = feedbackColour;
    }

    public void setBtnBorderThickness(int btnBorderThickness) {
        this.btnBorderThickness = btnBorderThickness;
    }

    /**
     * Whether the button should be lightly coloured when a mouse hovers
     * (but does not click) over it. It is true by default.
     * @param useHoverAnim
     */
    public void setUseHoverAnim(boolean useHoverAnim) {
        this.useHoverAnim = useHoverAnim;
    }
}
