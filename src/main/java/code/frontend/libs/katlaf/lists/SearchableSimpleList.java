package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.inputfields.SearchableUI;
import java.util.ArrayList;
import java.util.Comparator;

public class SearchableSimpleList extends SimpleList implements SearchableUI {
    private boolean hideBadMatches;

    public SearchableSimpleList(ArrayList<Listable> listables) {
        super(listables);
        this.hideBadMatches = false;
    }

    @Override
    public void onSearchChange(String search) {
        if (search.isEmpty()) {
            this.getListables().sort(null);
        }

        this.getListables().sort(new Comparator<Listable>() {
            @Override
            public int compare(Listable o1, Listable o2) {
                return getLevenshteinDistance(o1.getDisplayString(), search)
                    - getLevenshteinDistance(o2.getDisplayString(), search);
            }
        });

        if (hideBadMatches) {
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

    public void setHideBadMatches(boolean hideBadMatches) {
        this.hideBadMatches = hideBadMatches;
    }
}
