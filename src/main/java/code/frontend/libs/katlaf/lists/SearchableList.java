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

package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.inputfields.SearchableUI;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * A list UI component which responds to changes in a
 * {@link code.frontend.libs.katlaf.inputfields.SearchField}
 *
 * The members of this list would be rearranged, and possibly,
 * hidden based on comparisons made through the use of a
 * Levenshtein distance algorithm.
 */
public class SearchableList extends SimpleList implements SearchableUI {
    private boolean hideBadMatches;

    public SearchableList(ArrayList<Listable> listables) {
        super(listables);
        this.hideBadMatches = false;
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    /**
     * @return true if all the characters in the provided {@link Listable}
     * need to be replaced or removed due to the provided `search`.
     */
    private boolean isBadMatch(Listable listable, String search) {
        return getLevenshteinDistance(listable.getDisplayString(), search)
            == 2 * listable.getDisplayString().length();
    }

    /**
     * Edit distance algorithm which measures how closely two strings match. The smaller the
     * distance, the closer the match of the two provided strings. Method has a bias, where
     * having to append characters for a match of the two strings is penalised less than
     * replacement or removal since this method is often called when the user is in the
     * process of typing something.
     *
     * @param input first <code>String</code>
     * @param target second <code>String</code>
     * @return <b>int</b> the levenshtein distance distance between the two strings provided
     *     in the parameters
     */
    private int getLevenshteinDistance(String input, String target) {
        int[][] dp = new int[input.length() + 1][target.length() + 1];

        for (int i = 0; i < dp.length; i++) // scenario where second str is empty
            dp[i][0] = i;

        for (int i = 0; i < dp[0].length; i++) // scenario where first str is empty
            dp[0][i] = i;

        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                int append = dp[i][j - 1] + 1; // append to input
                int remove = dp[i - 1][j] + 2; // remove from input (double penalty)
                int replace = dp[i - 1][j - 1]; // assume no replacement

                if (input.charAt(i - 1) != target.charAt(j - 1)) // if chars at pos i dont
                                                                 // match
                    replace += 2; // add replacement penalty (double penalty)

                dp[i][j] = Math.min(append,
                    Math.min(remove,
                        replace)); // find min of append, remove and replace ops
            }
        }

        return dp[input.length()][target.length()]; // returns the last element of the 2d
                                                    // array (bottom right element)
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Bad matches for the Levenshtein distance algorithm
     * imply that all characters of the {@link Listable}
     * display string needs to be removed or replaced.
     * By default, hideBadMatches is `false`.
     */
    public void setHideBadMatches(boolean hideBadMatches) {
        this.hideBadMatches = hideBadMatches;
    }

    /**
     * This method is executed whenever there is a change in the
     * {@link code.frontend.libs.katlaf.inputfields.SearchField}
     * that holds its reference to this {@link SearchableList}.
     *
     * If `search` is empty, this will be repopulated.
     * Otherwise, this list will be sorted using the
     * Levenshtein distance algorithm. If a {@link Listable}
     * is a bad match, and `hideBadMatches` is set to `true` it,
     * will be hidden.
     */
    @Override
    public void onSearchChange(String search) {
        if (search.isEmpty()) {
            repopulate();
            return;
        }

        this.getListables().sort(new Comparator<Listable>() {
            @Override // sort by levenstein dist
            public int compare(Listable o1, Listable o2) {
                return getLevenshteinDistance(o1.getDisplayString(), search)
                    - getLevenshteinDistance(o2.getDisplayString(), search);
            }
        });

        if (hideBadMatches) { // do not add bad matches
            resetNextListable();
            this.getListables().forEach(listable -> {
                if (!isBadMatch(listable, search))
                    addNextListable();
                else
                    nextListable();
            });
        } else {
            repopulate();
        }
    }
}
