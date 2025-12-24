package code;

import java.time.Instant;

import code.backend.Countdown;
import code.frontend.foundation.CustomButtonFactory;
import code.frontend.misc.Vals;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Launcher extends Application
{
    @Override
    public void init() throws Exception
    {
        Vals.FontTools.initFonts();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setMinWidth(Vals.GraphicalUI.MIN_WIDTH);
        stage.setMinHeight(Vals.GraphicalUI.MIN_HEIGHT);
        stage.setWidth(Vals.GraphicalUI.PREF_WIDTH);
        stage.setHeight(Vals.GraphicalUI.PREF_HEIGHT);
        Pane p = new Pane();
        p.setPrefSize(Vals.GraphicalUI.PREF_WIDTH, Vals.GraphicalUI.PREF_HEIGHT);
        p.setMinSize(Vals.GraphicalUI.MIN_WIDTH, Vals.GraphicalUI.MIN_HEIGHT);
        p.relocate(0, 0);
        p.setBackground(null);

        Countdown countdown = Countdown.create("hello", 25, 12, 2025);
        CountdownPane c = new CountdownPane(countdown, Instant.now());
        c.relocate(20,20);
        p.getChildren().add(c);

        Button btnTest = new Button("hello")
        {
            @Override
            public void executeOnClick()
            {
                System.out.println("hi");
            }
        };
        btnTest.setPrefSize(300, 100);
        btnTest.relocate(300, 300);
        p.getChildren().add(btnTest);

        Scene scene = new Scene(p);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

}
