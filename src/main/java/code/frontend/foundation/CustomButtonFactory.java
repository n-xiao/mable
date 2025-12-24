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

public abstract class CustomButtonFactory
{
    private Color feedbackColour = Vals.Colour.FEEDBACK;
    private double btnBorderThickness = Vals.GraphicalUI.DRAW_THICKNESS;
    private Font labelFont = new Font(Vals.FontTools.FONT_FAM, 13);
    private Pane hoverPane = null;
    private Pane clickPane = null;
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
    public static void setup(Pane pane, CustomButtonFactory cbf, double bgInsets)
    {
        cbf.clickPane = new Pane();
        cbf.clickPane.prefWidthProperty().bind(pane.widthProperty());
        cbf.clickPane.prefHeightProperty().bind(pane.heightProperty());
        cbf.clickPane.relocate(0, 0);
        cbf.clickPane.setBackground(Vals.Colour.createBG(cbf.feedbackColour, 4, bgInsets));
        cbf.clickPane.setOpacity(0);

        cbf.clickAnim = new FadeTransition(Duration.millis(200), cbf.clickPane);
        cbf.clickAnim.setFromValue(1);
        cbf.clickAnim.setToValue(0);
        cbf.clickPane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (cbf.hoverAnim != null)
                    {
                        cbf.hoverAnim.stop();
                        cbf.hoverPane.setOpacity(0);
                    }
                cbf.executeOnClick();
                if (cbf.feedbackColour != null)
                    cbf.clickAnim.playFromStart();
            }
        });

        if (cbf.useHoverAnim)
            {

                cbf.hoverPane = new Pane();
                double hoverOpacity = 0.2;
                cbf.hoverAnim = new FadeTransition(Duration.millis(300), cbf.hoverPane);
                cbf.hoverPane.prefWidthProperty().bind(pane.widthProperty());
                cbf.hoverPane.prefHeightProperty().bind(pane.heightProperty());
                cbf.hoverPane.setBackground(Vals.Colour.createBG(cbf.feedbackColour, 4, bgInsets));
                cbf.hoverPane.setOpacity(0);
                cbf.clickPane.setOnMouseEntered(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        if (cbf.clickAnim != null
                                && cbf.clickAnim.getStatus().equals(Animation.Status.RUNNING))
                            return;
                        cbf.hoverAnim.setFromValue(0);
                        cbf.hoverAnim.setToValue(hoverOpacity);
                        cbf.hoverAnim.playFromStart();
                    }
                });

                cbf.clickPane.setOnMouseExited(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        if (cbf.clickAnim != null
                                && cbf.clickAnim.getStatus().equals(Animation.Status.RUNNING))
                            return;
                        cbf.hoverAnim.setFromValue(hoverOpacity);
                        cbf.hoverAnim.setToValue(0);
                        cbf.hoverAnim.playFromStart();
                    }
                });
                pane.getChildren().add(cbf.hoverPane);
            }

        pane.getChildren().add(cbf.clickPane);
    }

    public static Pane createButton(String btnText, CustomButtonFactory cbf)
    {
        Pane btn = new Pane();
        CustomBox border = new CustomBox(cbf.btnBorderThickness);
        CustomBox.applyToPane(btn, border);

        Label label = new Label(btnText);
        label.setTextFill(border.getStrokeColour());
        label.setAlignment(Pos.CENTER);
        label.setFont(cbf.labelFont);
        label.prefWidthProperty().bind(btn.widthProperty());
        label.prefHeightProperty().bind(btn.heightProperty());
        label.relocate(0, 0);
        btn.getChildren().add(label);

        setup(btn, cbf, cbf.btnBorderThickness * 1.5);

        return btn;
    }

    /**
     * Sets the colour of the feedback that is played using a FadeTransition
     * to indicate to the user that the button was clicked. Setting
     * feedbackColour to null will stop the transition from playing.
     *
     * @param feedbackColour
     */
    public void setFeedbackColour(Color feedbackColour)
    {
        this.feedbackColour = feedbackColour;
    }

    public void setBtnBorderThickness(int btnBorderThickness)
    {
        this.btnBorderThickness = btnBorderThickness;
    }

    /**
     * Whether the button should be lightly coloured when a mouse hovers
     * (but does not click) over it. It is true by default.
     * @param useHoverAnim
     */
    public void setUseHoverAnim(boolean useHoverAnim)
    {
        this.useHoverAnim = useHoverAnim;
    }
}
