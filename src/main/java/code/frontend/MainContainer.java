package code.frontend;

import javafx.scene.layout.StackPane;

public class MainContainer extends StackPane {
    private static MainContainer instance = null;

    private MainContainer() {
        this.setBackground(null);
    }

    public static MainContainer getInstance() {
        if (instance == null)
            instance = new MainContainer();
        return instance;
    }
}
