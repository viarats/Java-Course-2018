package bg.sofia.uni.fmi.mjt.movies;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MoviesExplorer {
    private List<Movie> movies;

    public MoviesExplorer(InputStream dataInput) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataInput))) {
            movies = reader.lines()
                    .map(Movie::createMovie)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public Collection<Movie> getMovies() {
        return movies;
    }

    public int countMoviesReleasedIn(int year) {
        return (int) movies.stream()
                .filter(movie -> movie.getYear() == year)
                .count();
    }

    public Movie findFirstMovieWithTitle(String title) {
        return movies.stream()
                .filter(movie -> movie.getTitle().contains(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie Not Found"));
    }

    public Collection<Actor> getAllActors() {
        return movies.stream()
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toSet());
    }

    public int getFirstYear() {
        return movies.stream()
                .mapToInt(Movie::getYear)
                .min()
                .getAsInt();
    }

    public Collection<Movie> getAllMoviesBy(Actor actor) {
        return movies.stream()
                .filter(movie -> movie.getActors().contains(actor))
                .collect(Collectors.toList());
    }

    public Collection<Movie> getMoviesSortedByReleaseYear() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getYear))
                .collect(Collectors.toList());
    }

    public int findYearWithLeastNumberOfReleasedMovies() {
        return movies.stream()
                .min(Comparator.comparing(movie -> countMoviesReleasedIn(movie.getYear())))
                .map(Movie::getYear)
                .get();
    }

    public Movie findMovieWithGreatestNumberOfActors() {
        return movies.stream()
                .max(Comparator.comparing(movie -> movie.getActors().size()))
                .get();
    }
}