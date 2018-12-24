package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MovieReviewSentimentAnalyzerTest {
    private static final double DELTA = 1e-15;
    private static final double SENTIMENT_VALUE_OF_REVIEW = 4.0;
    private static final double SENTIMENT_VALUE_OF_UNKNOWN = -1.0;
    private static final double SENTIMENT_VALUE_OF_REVIEW_WITH_WORDS_FROM_OTHER_REVIEWS = 1.5;
    private static final double SENTIMENT_VALUE_OF_THE_WORD_LIKE = 1.5;
    private static final int SENTIMENT_VALUE_OFFSET = 0;
    private static final String POSITIVE = "positive";
    private static final String UNKNOWN = "unknown";
    private static final String FOR_APPENDING = "3 Nice play";

    private MovieReviewSentimentAnalyzer analyzer;
    private OutputStream output;

    @Before
    public void setup() {
        try (InputStream stopwordsInput = new FileInputStream("resources/stopwords.txt");
             InputStream reviewsInput = new FileInputStream("resources/reviews_sample.txt")) {

            output = new FileOutputStream("resources/reviews_sample.txt", true);

            analyzer = new MovieReviewSentimentAnalyzer(stopwordsInput, reviewsInput, output);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @After
    public void closeOutputStream() {
        try {
            output.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Test
    public void sentimentValueOfExistingReviewAndUniqueWordsShouldBeTheSameAsInTheFile() {
        assertEquals(SENTIMENT_VALUE_OF_REVIEW, analyzer.getReviewSentiment("Tells a fascinating , " +
                "compelling story ."), DELTA);
    }

    @Test
    public void sentimentValueOfReviewWithWordsFromOtherReviewsShouldBeAverage() {
        assertEquals(SENTIMENT_VALUE_OF_REVIEW_WITH_WORDS_FROM_OTHER_REVIEWS,
                analyzer.getReviewSentiment("Like it"), DELTA);
    }

    @Test
    public void sentimentValueOfReviewWithUnknownWordsOnlyShouldBeNegativeOne() {
        assertEquals(SENTIMENT_VALUE_OF_UNKNOWN, analyzer.getReviewSentiment("None"), DELTA);
    }

    @Test
    public void sentimentAsNameOfReviewWithSentimentValueOf4ShouldBePositive() {
        assertEquals(POSITIVE, analyzer.getReviewSentimentAsName("Tells a fascinating , compelling story ."));
    }

    @Test
    public void sentimentAsNameOfReviewWithUnknownWordsOnlyShouldBeUnknown() {
        assertEquals(UNKNOWN, analyzer.getReviewSentimentAsName("None"));
    }

    @Test
    public void sentimentValueOfWordFromTheSentimentDictionaryShouldMatchTheExistingValue() {
        assertEquals(SENTIMENT_VALUE_OF_THE_WORD_LIKE, analyzer.getWordSentiment("like"), DELTA);
    }

    @Test
    public void sentimentValueOfUnknownWordShouldBeNegativeOne() {
        assertEquals(SENTIMENT_VALUE_OF_UNKNOWN, analyzer.getWordSentiment("none"), DELTA);
    }

    @Test
    public void returnedReviewShouldHaveTheSameSentimentValueAsTheGiven() {
        try (BufferedReader input = new BufferedReader(new FileReader("resources/reviews_sample.txt"))) {

            String review = analyzer.getReview(SENTIMENT_VALUE_OF_REVIEW);
            String line;
            int rating = -1;

            while ((line = input.readLine()) != null) {
                if (line.contains(review)) {
                    rating = Character.getNumericValue(line.charAt(SENTIMENT_VALUE_OFFSET));
                }
            }
            assertEquals(SENTIMENT_VALUE_OF_REVIEW, rating, DELTA);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getReviewWithInexistantSentimentValueShouldReturnNull() {
        assertNull(analyzer.getReview(1.5));
    }

    @Test
    public void appendReviewMethodShouldAppendTheGivenReviewToTheEndOfTheFile() {
        try (BufferedReader input = new BufferedReader(new FileReader("resources/reviews_sample.txt"))) {
            analyzer.appendReview("Nice play", 3);

            String currentReview;
            String lastReview = null;

            while ((currentReview = input.readLine()) != null) {
                lastReview = currentReview;
            }
            assertEquals(FOR_APPENDING, lastReview);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMostFrequentWordsShouldReturnTheFollowingSet() {
        Set<String> mostFrequent = new HashSet<>();
        mostFrequent.add("like");
        mostFrequent.add("self");
        mostFrequent.add("action");
        mostFrequent.add("aggressive");
        mostFrequent.add("aims");
        mostFrequent.add("bartlett");
        mostFrequent.add("betrayal");
        mostFrequent.add("combination");
        mostFrequent.add("compelling");
        mostFrequent.add("day");

        assertEquals(mostFrequent, analyzer.getMostFrequentWords(10));
    }

    @Test
    public void getMostNegativeWordsShouldReturnTheFollowingSet() {
        Set<String> mostNegative = new HashSet<>();
        mostNegative.add("satire");
        mostNegative.add("hampered");
        mostNegative.add("paralyzed");
        mostNegative.add("aims");
        mostNegative.add("poetry");
        mostNegative.add("ends");
        mostNegative.add("indulgent");
        mostNegative.add("script");
        mostNegative.add("sounding");
        mostNegative.add("self");

        assertEquals(mostNegative, analyzer.getMostNegativeWords(10));
    }

    @Test
    public void getMostPositiveWordsShouldReturnTheFollowingSet() {
        Set<String> mostPositive = new HashSet<>();
        mostPositive.add("entertaining");
        mostPositive.add("independent");
        mostPositive.add("compelling");
        mostPositive.add("worth");
        mostPositive.add("quiet");
        mostPositive.add("fascinating");
        mostPositive.add("seeking");
        mostPositive.add("tells");
        mostPositive.add("introspective");
        mostPositive.add("story");

        assertEquals(mostPositive, analyzer.getMostPositiveWords(10));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMostFrequentWordsWithNegativeArgumentShouldThrowException() {
        analyzer.getMostFrequentWords(-1);
    }
}