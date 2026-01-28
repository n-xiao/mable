package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.Cursor;

public abstract class BasicButton extends Button {
    public BasicButton(String text) {
        super(text);
        this.getCustomBorder().setVisible(false);
        this.getCustomBackground().setOpacity(0);
        this.getLabel().setTextFill(RiceHandler.getColour());
        this.getLabel().setFont(FontHandler.getNormal());
        this.setCursor(Cursor.DEFAULT);
    }

    @Override
    protected void playMouseEnterAnim() {
        this.getCustomBackground().setOpacity(1);
    }

    @Override
    protected void playMouseExitAnim() {
        this.getCustomBackground().setOpacity(0);
    }
}
