package code.frontend.gui;

import code.frontend.gui.pages.HomePage;
import javafx.scene.layout.StackPane;

public class Content extends StackPane {
    private static Content instance = null;

    private Content() {
        this.getChildren().addAll(HomePage.getInstance());
    }

    public static Content getInstance() {
        if (instance == null) {
            instance = new Content();
        }
        return instance;
    }
}
