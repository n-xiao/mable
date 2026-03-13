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

import java.util.Stack;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A wrapper class which abstracts the process of translating (animating) a UI component
 * in the downwards direction using TranslateTranslation.
 *
 * @see TranslateTransition
 * @since v3.0.0-beta
 */
public final class DownTranslation {
    private final TranslateTransition transition;
    private final Stack<Double> history;

    public DownTranslation(final Node target) {
        this.transition = new TranslateTransition(Duration.millis(200), target);
        this.history = new Stack<Double>();
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void play() {
        this.transition.playFromStart();
        this.history.push(this.transition.getByY());
    }

    /**
     * Plays the previous transition, but in the reverse direction. Note that the transition
     * is not played in reverse, only the distance. This means that the interpolation remains
     * the same. This exact procedure cannot be repeated or undone. Once a distance is popped
     * from the history stack, that distance will be used.
     */
    public void playBack() {
        setDist(this.history.pop());
        this.transition.playFromStart();
    }

    /**
     * Accumulates the distances of a specified number of previous translations into one
     * big, negated distance. Then, the translation distance is set and the transition
     * is played.
     * <p>
     * This is useful when a big translation is needed in the same amount of time as
     * all other translations.
     *
     * @param times     the number of previous translations that should be included
     *                  if the value exceeds the size of the history stack, the caller
     *                  is a dum dum and the method does nothing.
     */
    public void playBack(final int times) {
        int i = 0;
        double totalDist = 0;
        while (!this.history.isEmpty() && i < times) {
            totalDist -= this.history.pop();
        }

        if (totalDist != 0) {
            setDist(totalDist);
            this.transition.playFromStart();
        }
    }

    /**
     * Sets how much further the provided Node should be translated, in the downwards direction.
     * Note that this distance is an addition to a Node that may have already been moved,
     * meaning that its relative position (at runtime) should be considered instead of its absolute
     * position (specified in code).
     *
     * @param dist      how much further downwards the target Node should be translated by.
     *                  Negative values are allowed, just keep in mind that it would translate
     *                  the Node upwards.
     */
    public void setDist(final double dist) {
        this.transition.setByY(dist);
    }

    /**
     * Sets the interpolator of this translation. This controls whether the transition is done
     * linearly, cubicly or some other math-ly way.
     *
     * @param interpolator      the thing...
     */
    public void setInterpolator(final Interpolator interpolator) {
        this.transition.setInterpolator(interpolator);
    }

    /**
     * Sets the duration of the translation in milliseconds. The default duration
     * is 200 milliseconds.
     *
     * @param duration      duration of the translation in milliseconds
     */
    public void setDuration(final int duration) {
        this.transition.setDuration(Duration.millis(duration));
    }
}
