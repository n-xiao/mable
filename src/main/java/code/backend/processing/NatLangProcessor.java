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

package code.backend.processing;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

/**
 * A factory class which provides Natual Language Processing capabilities
 * to Mable. At the moment, only English is supported.
 *
 * @since v3.1.0
 */
public final class NatLangProcessor {
    private String text;

    public NatLangProcessor(final String text) {
        this.text = text;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * Applies the trim method to the input text to remove any leading or trailing spaces.
     * Then, any repeated whitespace characters between non-whitespace characters are also removed.
     * In other words, any continuous repetition of whitespace characters will be reduced to
     * a single whitespace character.
     *
     * @param text              the input text to clean
     * @return String           the cleaned text
     */
    private static String reduceStringWhitespace(String text) {
        text = text.trim();
        final StringBuilder clean = new StringBuilder();
        final char[] chars = text.toCharArray();

        int spaceCounter = 0;
        for (char c : chars) {
            final boolean isSpace = Character.isWhitespace(c);

            if (spaceCounter == 0 && isSpace || !isSpace)
                clean.append(c);

            spaceCounter = isSpace ? spaceCounter + 1 : 0;
        }

        return clean.toString();
    }

    /**
     * Removes a target String from an existing String. This method calls the cleanString
     * method.
     *
     * @param text          the String with the target word to remove
     * @param target        the String that should be removed from text
     *
     * @param String        the resulting String
     */
    private static String deleteFromString(final String text, final String target) {
        final String regex = "\\s*\\b" + target + "\\b\\s*";
        return reduceStringWhitespace(text.replaceAll(regex, ""));
    }

    private static LocalDate parseDuration(final String raw, final LocalDate now) {
        final char[] chars = raw.toCharArray();

        if (chars.length < 1) {
            System.err.println("Unable to parse duration.");
            return null;
        }

        final char type = chars[chars.length - 1];
        final String amountString = raw.substring(1, chars.length - 1);
        final int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            System.err.println("Unable to parse duration amount: " + amountString);
            return null;
        }
        return switch (type) {
            case 'D' -> now.plusDays(amount);
            case 'M' -> now.plusMonths(amount);
            case 'Y' -> now.plusYears(amount);
            default -> null;
        };
    }

    private static LocalDate parseDuration(final List<String> raws, final LocalDate now) {
        LocalDate result = now;
        for (String string : raws) result = parseDuration(string, result);
        return result;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Extracts a {@link LocalDate} from the provided text using CoreNLP's SUTime
     * capabilities with the last mention of a date.
     * Note that, to get the current date, LocalDate.now() is called within this method.
     * <p>
     * For example, if the provided String contains the text "next Friday", the LocalDate
     * of the next Friday would be returned, relative to the return value of LocalDate.now().
     * <p>
     * If a date cannot be extracted, null will be returned.
     *
     * @param removeDate    whether or not the detected date should be removed from the input
     *                      text. This is useful to prevent the date from being detected
     *                      as the topic of the sentence.
     *
     * @return LocalDate    the date that has been extracted
     */
    public LocalDate extractLocalDate(final boolean removeDate) {
        final Properties props = new Properties();
        props.setProperty("sutime.teRelHeurLevel", "NONE");

        final AnnotationPipeline pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new TokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));

        final Annotation annotation = new Annotation(this.text);
        final LocalDate now = LocalDate.now();
        annotation.set(CoreAnnotations.DocDateAnnotation.class, now.toString());
        pipeline.annotate(annotation);
        final List<CoreMap> allTimex = annotation.get(TimeAnnotations.TimexAnnotations.class);

        if (allTimex == null || allTimex.isEmpty())
            return null;

        final ArrayList<String> durations = new ArrayList<String>();
        allTimex.forEach(cm -> {
            if (cm.get(TimeExpression.Annotation.class).getValue().getType().equals("DURATION")) {
                durations.add(cm.get(TimeExpression.Annotation.class).getTemporal().toISOString());
                this.text = deleteFromString(this.text, cm.toString());
            }
        });

        if (!durations.isEmpty()) {
            return parseDuration(durations, now);
        }

        final CoreMap cm = allTimex.getLast();
        final Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();
        final String isoString = temporal.toISOString();

        try {
            return LocalDate.parse(isoString);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(isoString).toLocalDate();
            } catch (DateTimeParseException e2) {
                System.err.println("Cannot parse LocalDate from: " + isoString);
                System.err.println("Full input: " + text);
                return null;
            }
        } finally {
            if (removeDate)
                this.text = deleteFromString(this.text, cm.toString());
        }
    }

    public String extractTopicPhrase() {
        final Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos");
        final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        final CoreDocument document = pipeline.processToCoreDocument(this.text);

        String result = "";

        final Function<String, Boolean> tagWhitelist = tag -> {
            return tag.contains("NN") || tag.equals("VB") || tag.equals("RP");
        };

        final Function<String, Boolean> tagBlacklist = tag -> {
            return tag.contains("PRP");
        };

        boolean appending = false;
        for (CoreLabel tok : document.tokens()) {
            if (tagWhitelist.apply(tok.tag()))
                appending = true;

            if (appending && !tagBlacklist.apply(tok.tag()))
                result += " " + tok.word();

            // System.out.println(String.format("%s\t%s", tok.word(), tok.tag()));
        }

        for (CoreLabel tok : document.tokens().reversed()) {
            if (tok.tag().contains("NN"))
                break;
            result = deleteFromString(result, tok.word());
        }

        return reduceStringWhitespace(result.toString());
    }
}
