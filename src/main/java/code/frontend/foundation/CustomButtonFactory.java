package code.frontend.foundation;

import code.frontend.misc.Vals;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class CustomButtonFactory {
    private Pane clickPane = new Pane();
    private Color feedbackColour = Vals.Colour.FEEDBACK;

    public CustomButtonFactory() {}

    public abstract void executeOnClick();

    public static void setup(Pane pane, CustomButtonFactory cbf) {
        Pane clickPane = cbf.clickPane;
        clickPane.setManaged(false);
        clickPane.prefWidthProperty().bind(pane.widthProperty());
        clickPane.prefHeightProperty().bind(pane.heightProperty());
        clickPane.relocate(0, 0);
        clickPane.setBackground(Vals.Colour.createBG(cbf.feedbackColour, 10, 0));
        clickPane.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(200), clickPane);
        ft.setFromValue(1);
        ft.setToValue(0);
        clickPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cbf.executeOnClick();
                if (cbf.feedbackColour != null)
                    ft.playFromStart();
            }
        });
    }

    public static Pane createButton() {
        // TODO todo this when i feel better aaah
        return null;
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
}
