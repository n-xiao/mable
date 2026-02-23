/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.capabilities.countdowns;

import code.backend.data.Countdown;
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.interfaces.Colourable;
import code.frontend.libs.katlaf.lists.SimpleListMember;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class RelativeCountdownListMember extends SimpleListMember implements Updatable, Colourable {
    private final VBox container;
    private final CountdownListMember ogMember;
    private final Counter counter;
    private final MableBorder border;

    RelativeCountdownListMember(final Countdown countdown, final CountdownList list) {
        super(list.getSelector());
        this.container = new VBox();
        this.container.setBackground(null);
        this.container.setFillWidth(true);
        this.container.prefWidthProperty().bind(this.widthProperty());
        this.container.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.container);

        this.ogMember = new CountdownListMember(countdown, list);
        this.ogMember.removeCounter();
        this.counter = new Counter();
        this.container.getChildren().addAll(this.counter, this.ogMember);

        this.border = new MableBorder(1.5, 0.2, 0.25);
        this.border.setColour("dullgrey");
        this.border.bindSize(ogMember.widthProperty(), ogMember.heightProperty());
        this.ogMember.getChildren().add(this.border);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public void update() {
        this.counter.update();
    }

    @Override
    public void setColour(Color colour) {
        this.ogMember.setColour(colour);
    }

    @Override
    public void resetColour() {
        this.ogMember.resetColour();
    }

    @Override
    public void setToggle(boolean toggled) {
        super.setToggle(toggled);
        if (this.isToggled()) {
            this.border.setColour("white");
        } else {
            this.border.setColour("dullgrey");
        }
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Counter extends BorderPane implements Updatable {
        private final static int V_SEP = 15; // basically the height of this
        private final Label label;
        Counter() {
            this.label = new Label();
            this.label.setBackground(null);
            this.label.setFont(FontHandler.getItalic());
            this.label.setMaxHeight(Double.MAX_VALUE);
            this.label.setTextFill(RiceHandler.getColour("lightgrey"));
            this.setBackground(null);
            this.setMouseTransparent(true);
            this.setRight(this.label);
            this.setMinHeight(V_SEP);
            this.setMaxHeight(V_SEP);
        }

        @Override
        public void update() {
            final int days = ogMember.getCountdown().getDaysUntilDue(LocalDate.now());
            final String text = FormatHandler.intToString(days);
            this.label.setText(text);
        }
    }
}
