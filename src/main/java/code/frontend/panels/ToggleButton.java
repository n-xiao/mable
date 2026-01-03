package code.frontend.panels;

public abstract class ToggleButton extends Button {
    private boolean isToggled;

    public ToggleButton(String name) {
        this.isToggled = false;
        super(name);
    }

    @Override
    protected void playClickAnim() {
        if (this.isToggled)
            this.getFeedbackBackground().setOpacity(this.getHoverOpacity());
        else
            this.getFeedbackBackground().setOpacity(1);
        this.isToggled = !this.isToggled;
    }

    public boolean isToggled() {
        return this.isToggled;
    }

    public void setToggled(boolean toggle) {
        this.isToggled = toggle;
        // careful: this is the opposite behaviour to what is in playClickAnim()
        if (this.isToggled) {
            this.getFeedbackBackground().setOpacity(1);
        } else {
            this.getFeedbackBackground().setOpacity(this.getHoverOpacity());
        }
    }
}
