package code.frontend.gui;

import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import java.util.HashSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class RightClickMenuTemplate extends VBox {
    private static final HashSet<RightClickMenuTemplate> MENUS =
        new HashSet<RightClickMenuTemplate>();
    private static final double BG_RADIUS = 12;
    private static final double BG_INSETS = 4;

    private static final double BORDER_CORNER_OFFSET = 0.12;
    private static final double BORDER_DEV = 0;
    private static final double BORDER_CORNER_DEV = 0.014;
    private static final double BORDER_THICKNESS = 2;

    private static final int RIGHTLEFT_INSET = 15;

    private static final double BUTTON_HEIGHT = 40;

    private final double WIDTH;
    private final double HEIGHT;
    private final Button[] BUTTONS;
    private final Color[] COLOURS;

    public static void despawnAll() {
        for (RightClickMenuTemplate menu : MENUS) {
            menu.close();
        }
        MENUS.clear();
    }

    public RightClickMenuTemplate(double width, double height, Button[] buttons, Color[] colours) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BUTTONS = buttons;
        this.COLOURS = colours;
        initStyling();
        initButtonStylings();
        MENUS.add(this);
    }

    public void openAt(double x, double y) {
        final MainContainer MC = MainContainer.getInstance();
        MC.getChildren().removeIf(child -> child instanceof RightClickMenuTemplate);

        this.setMode();
        this.relocate(x, y);
        this.setViewOrder(-100);
        MC.getChildren().add(this);
    }

    public void close() {
        final MainContainer MC = MainContainer.getInstance();
        MC.getChildren().remove(this);
    }

    private void initButtonStylings() {
        final CornerRadii CORNER_RADII = new CornerRadii(8);
        final Insets INSETS = new Insets(5, -5, 5, -5);

        assert this.COLOURS.length == this.BUTTONS.length;
        for (int i = 0; i < this.BUTTONS.length; i++) {
            Button button = this.BUTTONS[i];
            if (button != null) {
                button.setPrefSize(WIDTH, BUTTON_HEIGHT);
                button.setMaxSize(WIDTH, BUTTON_HEIGHT);
                button.setAnimationsEnabled(false);
                button.getCustomBorder().setVisible(false);
                button.getLabel().setAlignment(Pos.CENTER_LEFT);
                button.setConsumeEvent(true);
                button.setCursor(Cursor.DEFAULT);
                VBox.setMargin(button, new Insets(0, RIGHTLEFT_INSET, 0, RIGHTLEFT_INSET));
                // colouring
                BackgroundFill fill = new BackgroundFill(this.COLOURS[i], CORNER_RADII, INSETS);
                button.setFeedbackBackground(new Background(fill));
                this.getChildren().add(button);
            } else {
                this.getChildren().add(createDivider());
            }
        }
    }

    private void initStyling() {
        CustomBox border =
            new CustomBox(BORDER_THICKNESS, BORDER_DEV, BORDER_CORNER_DEV, BORDER_CORNER_OFFSET);
        CustomBox.applyToPane(this, border);
        this.setManaged(false);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, BG_RADIUS, BG_INSETS));
        this.setFillWidth(true);
        this.resize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setAlignment(Pos.CENTER);
    }

    protected Region createDivider() {
        final double DIVIDER_HEIGHT = 5;
        Pane divider = new Pane();
        divider.setPrefSize(WIDTH, DIVIDER_HEIGHT);
        divider.setMaxSize(WIDTH, DIVIDER_HEIGHT);
        divider.setBackground(null);
        divider.setOpacity(0.3);
        CustomLine line = new CustomLine(BORDER_THICKNESS, Type.HORIZONTAL_TYPE);
        line.setPadding(RIGHTLEFT_INSET);
        line.setStrokeColour(Color.WHITE);
        CustomLine.applyToPane(divider, line);

        return divider;
    }

    public abstract void setMode();
}
