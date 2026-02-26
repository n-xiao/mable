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

package code.frontend.libs.katlaf.transitions;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.util.Duration;

/**
 * Wrapper class for JavaFX transitions.
 */
public final class Transitioner {
    private SequentialTransition master;

    public Transitioner() {}

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/
    public Transitioner play(final Transition transition) {
        this.master.getChildren().add(transition);
        return this;
    }

    public Transitioner play(final Transition... transitions) {
        this.master.getChildren().addAll(transitions);
        return this;
    }

    public Transitioner playParallel(final Transition... transitions) {
        final ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(transitions);
        this.master.getChildren().add(pt);
        return this;
    }

    public Transitioner hold(final Duration duration) {
        final PauseTransition pause = new PauseTransition(duration);
        this.master.getChildren().add(pause);
        return this;
    }

    public void setFadeToValues(final double value) {
        setFadeToValues(value, this.master);
    }

    private void setFadeToValues(final double value, Animation animation) {
        if (animation instanceof FadeTransition fade) {
            fade.setToValue(value);
            fade.setFromValue(fade.getNode().getOpacity());
        } else if (animation instanceof ParallelTransition innerPt) {
            setFadeToValues(value, innerPt);
        } else if (animation instanceof SequentialTransition innerSt) {
            setFadeToValues(value, innerSt);
        }
    }

    private void setFadeToValues(final double value, ParallelTransition pt) {
        pt.getChildren().forEach(animation -> { setFadeToValues(value, animation); });
    }

    private void setFadeToValues(final double value, SequentialTransition st) {
        st.getChildren().forEach(animation -> { setFadeToValues(value, animation); });
    }

    public SequentialTransition getTransition() {
        return this.master;
    }

    public Transitioner prepare() {
        if (this.master != null)
            this.master.stop();
        this.master = new SequentialTransition();
        return this;
    }
}
