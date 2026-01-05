/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.panels;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public abstract class ToggleButton extends Button {
    private boolean isToggled;
    private Color toggledColour;
    private Color untoggledColour;

    public ToggleButton(String name) {
        this.isToggled = false;
        this.toggledColour = Color.WHITE;
        this.untoggledColour = Color.GRAY;
        super(name);
    }

    public boolean getIsToggled() {
        return this.isToggled;
    }

    @Override
    protected void playMouseExitAnim() {
        if (!isToggled)
            super.playMouseExitAnim();
    }

    @Override
    protected void playMouseEnterAnim() {
        if (!isToggled)
            super.playMouseEnterAnim();
    }

    @Override
    public void executeOnClick(MouseEvent event) {
        this.isToggled = !this.isToggled;
        if (this.isToggled) {
            // if toggled on, apply toggle style
            this.getCustomBorder().setStrokeColour(this.toggledColour);
        } else {
            this.getCustomBorder().setStrokeColour(this.untoggledColour);
        }
    }

    /**
     * This is only called by the program.
     * It should not be called by a user click or press,
     * since it assumes no mouse is hovering over the button.
     */
    public void untoggle() {
        this.isToggled = false;
        this.getCustomBorder().setStrokeColour(this.untoggledColour);
    }

    public void toggle() {
        this.isToggled = true;
        this.getCustomBorder().setStrokeColour(this.toggledColour);
    }

    public void setToggledColour(Color toggledColour) {
        this.toggledColour = toggledColour;
        if (this.isToggled)
            this.getCustomBorder().setStrokeColour(this.toggledColour);
    }

    public void setUntoggledColour(Color untoggledColour) {
        this.untoggledColour = untoggledColour;
        if (!this.isToggled)
            this.getCustomBorder().setStrokeColour(this.untoggledColour);
    }

    public Color getToggledColour() {
        return toggledColour;
    }

    public Color getUntoggledColour() {
        return untoggledColour;
    }
}
