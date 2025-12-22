package code.frontend.panels;

import java.time.Instant;

import code.backend.Countdown;
import code.frontend.foundation.CustomBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CountdownPanel extends VBox {
    private final double WIDTH = 250;
    private final double HEIGHT = 130;

    private HBox hoverHBox;
    private HBox contentHBox;

    public CountdownPanel(Countdown cd, Instant now) {
        this.setAlignment(Pos.CENTER);
        this.setMinSize(WIDTH, HEIGHT);
        this.setMaxSize(WIDTH, HEIGHT);
        initHoverHBox();
        initContentHBox();
        this.getChildren().addAll(this.hoverHBox, this.contentHBox);
    }

    private void initHoverHBox() {
        this.hoverHBox = new HBox();
        hoverHBox.setPrefSize(WIDTH, 30);
        VBox.setMargin(hoverHBox, new Insets(0, 0, 1, 0));
        hoverHBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

        // todo mouse listener stuff
    }

    private void initContentHBox() {
        this.contentHBox = new HBox();
        contentHBox.setPrefSize(WIDTH, 100);
        CustomBox border = new CustomBox(3);
        CustomBox.applyAsPaneBorder(contentHBox, border);
        contentHBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
    }

}
