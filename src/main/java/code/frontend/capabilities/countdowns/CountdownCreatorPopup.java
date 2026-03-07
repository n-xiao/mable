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
import code.backend.data.CountdownHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.inputfields.BorderedField;
import code.frontend.libs.katlaf.inputfields.DateField;
import code.frontend.libs.katlaf.inputfields.DaysField;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class CountdownCreatorPopup extends Popup {
    private final BorderedField nameField;
    private final DateField dateField;
    private final DaysField daysField;
    private final CountdownList list;
    private CountdownListMember oldMember;
    /**
     * Creates a new instance.
     *
     * @param list      the CountdownList associated with this Popup
     *
     * @see CountdownList
     */
    public CountdownCreatorPopup(final CountdownList list) {
        this(list, null);
    }

    /**
     * Creates a new instance, to edit an existing Countdown by passing a CountdownListMember
     * as an additional parameter.
     *
     * @param list      the CountdownList associated with this Popup
     * @param member    the CountdownListMember that contains the Countdown that should
     *                  be edited
     *
     * @see CountdownList
     * @see CountdownListMember
     * @see Countdown
     */
    public CountdownCreatorPopup(final CountdownList list, final CountdownListMember member) {
        this.nameField = new BorderedField("NAME", RiceHandler.getColour("night"));
        this.dateField = new DateField("DUE DATE", RiceHandler.getColour("night"));
        this.daysField = new DaysField();
        this.list = list;
        this.oldMember = member;
        super(300, 290);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private boolean isEditing() {
        return this.oldMember != null;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected void configureContent(StackPane content) {
        final LocalDate now = LocalDate.now();
        if (this.isEditing()) {
            this.nameField.setUserInput(this.oldMember.getCountdown().getName());
            this.dateField.setLocalDateInput(this.oldMember.getCountdown().getLocalDueDate(now));
            this.daysField.setUserInput(
                Integer.toString(this.oldMember.getCountdown().getDaysUntilDue(now)));
        }
        /*
         * setup the styling of them fields
         */
        VBox.setMargin(this.nameField, new Insets(0, 0, 10, 0));
        VBox.setMargin(this.dateField, new Insets(0, 0, 10, 0));
        /*
         * need to add a label preceding the daysField; so it has
         * its own container
         */
        final Label label = new Label("which means it's due in");
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setFont(FontHandler.getNormal());
        label.setTextFill(RiceHandler.getColour("white"));
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setMargin(label, new Insets(0, 8, 0, 0));

        final VBox daysFieldContainer = new VBox();
        daysFieldContainer.setAlignment(Pos.CENTER_RIGHT);
        daysFieldContainer.getChildren().addAll(label, this.daysField);
        /*
         * setup the button
         */
        final FilledButton accept = new FilledButton(
            RiceHandler.getColour("dullpink"), RiceHandler.getColour("dullpink2")) {
            @Override
            public void onMousePressed(MouseEvent event) {
                final String name = nameField.getUserInput();
                final LocalDate due = dateField.getLocalDateInput(false);

                if (due == null)
                    return;

                if (isEditing()) {
                    list.removeMember(oldMember);
                    oldMember.getCountdown().delete();
                }

                final Countdown countdown = CountdownHandler.create(name, due);
                list.addMember(new CountdownListMember(countdown, list));

                Popup.despawn();
            }
        };
        accept.setLabel(this.isEditing() ? "Confirm" : "Create");
        accept.setMinSize(80, 30);
        accept.setMaxSize(80, 30);
        /*
         * add everything to container
         */
        final VBox container = new VBox();
        container.setFillWidth(true);
        container.setPadding(new Insets(0, 10, 0, 10));
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(this.nameField, this.dateField, daysFieldContainer, accept);
    }

    @Override
    protected String getIdent() {
        return "countdown creator";
    }
}
