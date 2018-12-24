package bg.sofia.uni.fmi.mjt.sentiment;

import bg.sofia.uni.fmi.mjt.sentiment.interfaces.SentimentAnalyzer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {
    private static final double DELTA = 0.0001;
    private static final int SENTIMENT_VALUE_OFFSET = 0;
    private static final char MIN_RATING = '0';
    private static final char MAX_RATING = '4';
    private static final double SENTIMENT_VALUE_OF_UNKNOWN = -1.0;
    private static final int NEGATIVE = 0;
    private static final int SOMEWHAT_NEGATIVE = 1;
    private static final int NEUTRAL = 2;
    private static final int SOMEWHAT_POSITIVE = 3;
    private static final int POSITIVE = 4;

    private StringBuilder normalizedReviews;
    private Map<String, Double> reviews;
    private Map<String, Double> sentimentDictionary;
    private Set<String> stopwords;
    private OutputStream output;

    public MovieReviewSentimentAnalyzer(InputStream stopwordsInput, InputStream reviewsInput,
                                        OutputStream reviewsOutput) {
        stopwords = new HashSet<>(readStopwords(stopwordsInput));

        String bufferedReviews = readInputReviews(reviewsInput);

        normalizedReviews = new StringBuilder(normalizeReviews(bufferedReviews));

        String[] toWords = normalizedReviews.toString().split("\\W+");
        sentimentDictionary = new HashMap<>(createSentimentDictionary(toWords, normalizedReviews.toString()));

        reviews = new HashMap<>(getReviewsWithPreciseSentimentValue(bufferedReviews));

        output = reviewsOutput;
    }

    public double getReviewSentiment(String review) {
        String normalized = removeStopwordsAndSymbols(review);
        String[] toWords = normalized.split("\\W");

        double score = 0;
        int counter = 0;

        for (String word : toWords) {
            if (sentimentDictionary.containsKey(word)) {
                score += sentimentDictionary.get(word);
                counter++;
            }
        }
        return counter != 0 ? score / counter : SENTIMENT_VALUE_OF_UNKNOWN;
    }

    public String getReviewSentimentAsName(String review) {
        int score = (int) Math.round(getReviewSentiment(review));

        switch (score) {
            case NEGATIVE: return "negative";
            case SOMEWHAT_NEGATIVE: return "somewhat negative";
            case NEUTRAL: return "neutral";
            case SOMEWHAT_POSITIVE: return "somewhat positive";
            case POSITIVE: return "positive";
            default: return "unknown";
        }
    }

    public double getWordSentiment(String word) {
        return sentimentDictionary.getOrDefault(word, SENTIMENT_VALUE_OF_UNKNOWN);
    }

    public String getReview(double sentimentValue) {
        return reviews.entrySet()
                .stream()
                .filter(r -> Math.abs(r.getValue() - sentimentValue) < DELTA)
                .findFirst()
                .map(r -> r.getKey())
                .orElse(null);
    }

    public Collection<String> getMostFrequentWords(int n) {
        if (n >= 0) {
            Map<String, Integer> wordFrequencies = new TreeMap<>();
            String[] toWords = normalizedReviews.toString().split("\\W+");

            for (String word : toWords) {
                if (!isRating(word)) {
                    wordFrequencies.put(word, getWordCount(word, normalizedReviews.toString()));
                }
            }

            Map<String, Integer> sorted = new TreeMap<>(new ValueComparator<>(wordFrequencies));
            sorted.putAll(wordFrequencies);

            sorted = ((TreeMap<String, Integer>) sorted).descendingMap();

            return sorted.keySet().stream().limit(n).collect(Collectors.toSet());
        }
        throw new IllegalArgumentException("Parameter N Must Be Non-negative");
    }


    public Collection<String> getMostNegativeWords(int n) {
        Map<String, Double> sorted = new TreeMap<>(new ValueComparator<>(sentimentDictionary));
        sorted.putAll(sentimentDictionary);

        return sorted.keySet().stream().limit(n).collect(Collectors.toSet());
    }

    public Collection<String> getMostPositiveWords(int n) {
        Map<String, Double> sorted = new TreeMap<>(new ValueComparator<>(sentimentDictionary));
        sorted.putAll(sentimentDictionary);
        sorted = ((TreeMap<String, Double>) sorted).descendingMap();

        return sorted.keySet().stream().limit(n).collect(Collectors.toSet());
    }

    public void appendReview(String review, int sentimentValue) {
        try (OutputStreamWriter writer = new OutputStreamWriter(output)) {
            writer.write("\n" + sentimentValue + " " + review);
            writer.flush();

            if (!reviews.containsKey(review)) {
                reviews.put(review, getReviewSentiment(review));
            }

            normalizedReviews.append("\n")
                        .append(sentimentValue)
                        .append(" ")
                        .append(removeStopwordsAndSymbols(review));

            String[] toWords = removeStopwordsAndSymbols(review).split("\\W");
            for (String word : toWords) {
                if (!isRating(word)) {
                    sentimentDictionary.put(word, getWordScore(word, normalizedReviews.toString()));
                }
            }
            reviews.entrySet().forEach(r -> updateValue(r.getKey()));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public int getSentimentDictionarySize() {
        return sentimentDictionary.size();
    }

    public boolean isStopWord(String word) {
        return stopwords.contains(word.toLowerCase());
    }

    private String readInputReviews(InputStream input) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return reader.lines()
                    .distinct()
                    .collect(Collectors.joining("\n"));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Set<String> readStopwords(InputStream input) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return reader.lines().collect(Collectors.toSet());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Map<String, Double> getReviewsWithPreciseSentimentValue(String reviews) {
        return reviews.lines()
                .map(r -> getWithoutScore(r))
                .collect(Collectors.toMap(r -> r, r -> getReviewSentiment(r)));
    }

    private String removeStopwordsAndSymbols(String line) {
        StringBuilder normalized = new StringBuilder();
        String[] words = line.toLowerCase().split("[\\W]");

        for (String word : words) {
            if (!word.equals("") && !stopwords.contains(word)) {
                normalized.append(word).append(" ");
            }
        }
        return normalized.toString();
    }

    private double getWordScore(String word, String reviews) {
        String[] lines = reviews.split("\n");

        int counter = 0;
        int score = 0;

        for (String line : lines) {
            if (line.contains(" " + word + " ")) {
                score += Character.getNumericValue(line.charAt(SENTIMENT_VALUE_OFFSET));
                counter++;
            }
        }
        return (double)score / counter;
    }

    private int getWordCount(String word, String reviews) {
        String[] lines = reviews.split("\n");
        int counter = 0;

        for (String line : lines) {
            if (line.contains(" " + word + " ")) {
                counter++;
            }
        }
        return counter;
    }

    private boolean isRating(String word) {
        if (word.length() == 1) {
            char c = word.toCharArray()[0];
            return c >= MIN_RATING && c <= MAX_RATING;
        }
        return false;
    }

    private String getWithoutScore(String review) {
        StringBuilder sb = new StringBuilder(review);
        sb.deleteCharAt(SENTIMENT_VALUE_OFFSET);
        return sb.toString().trim();
    }

    private Map<String, Double> createSentimentDictionary(String[] words, String reviews) {
        Map<String, Double> dictionary = new HashMap<>();
        for (String word : words) {
            if (!isRating(word)) {
                dictionary.put(word, getWordScore(word, reviews));
            }
        }
        return dictionary;
    }

    private String normalizeReviews(String reviews) {
        return reviews.lines()
                .map(r -> removeStopwordsAndSymbols(r))
                .collect(Collectors.joining("\n"));
    }

    private void updateValue(String key) {
        if (reviews.containsKey(key)) {
            reviews.replace(key, getReviewSentiment(key));
        }
    }
}