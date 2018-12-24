package bg.sofia.uni.fmi.mjt.movies;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MoviesExplorerTest {
    private static final String MOVIE_1 = "18 Again! (1988)/Baker, " +
                                            " Benny/Baldwin, Stephanie/Bass, Emory";
    private static final String MOVIE_2 = "12 Angry Men (1988)/Allen, " +
                                            "Tyress/Cronyn, Hume/Danza, Tony";
    private static final String MOVIE_3 = "13 Conversations About One Thing " +
                                            "(2001)/Arkin, Alan/Burns, Alex";
    private static final String MOVIE_4 = "Heads in a Duffel Bag " +
                                            "(1997)/Baker, Benny/Basile, Joe";

    private static final int COUNT_MOVIES_RELEASED_IN_1988 = 2;
    private static final int FIRST_RELEASE_YEAR = 1988;
    private static final int LEAST_MOVIES_RELEASED_YEAR = 2001;

    static private MoviesExplorer explorer;

    @Before
    public void setup() {
        explorer = new MoviesExplorer(new ByteArrayInputStream((MOVIE_1 +
                '\n' + MOVIE_2 + '\n' + MOVIE_3 + '\n' + MOVIE_4).getBytes()));
    }

    @Test
    public void testGetMoviesMethod() {
        List<Movie> movies = new ArrayList<>();
        movies.add(Movie.createMovie(MOVIE_1));
        movies.add(Movie.createMovie(MOVIE_2));
        movies.add(Movie.createMovie(MOVIE_3));
        movies.add(Movie.createMovie(MOVIE_4));

        assertEquals(movies, explorer.getMovies());
    }

    @Test
    public void testCountMoviesReleasedInMethod() {
        assertEquals(COUNT_MOVIES_RELEASED_IN_1988, explorer.countMoviesReleasedIn(1988));
    }

    @Test
    public void testFindFirstMovieWithTitleMethodWithValidTitle() {
        assertEquals(Movie.createMovie(MOVIE_2), explorer.findFirstMovieWithTitle("Angry Men"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMovieWithTitleMethodWithInvalidTitle() {
        explorer.findFirstMovieWithTitle("No Such Title");
    }

    @Test
    public void testGetAllActorsMethod() {
        MoviesExplorer anotherExplorer = new MoviesExplorer(new ByteArrayInputStream((MOVIE_1 +
                '\n' + MOVIE_4).getBytes()));

        Set<Actor> actors = new HashSet<>();
        actors.add(new Actor("Benny", "Baker"));
        actors.add(new Actor("Stephanie", "Baldwin"));
        actors.add(new Actor("Emory", "Bass"));
        actors.add(new Actor("Joe", "Basile"));

        assertEquals(actors, anotherExplorer.getAllActors());
    }

    @Test
    public void testGetFirstYearMethod() {
        assertEquals(FIRST_RELEASE_YEAR, explorer.getFirstYear());
    }

    @Test
    public void testGetAllMoviesByMethod() {
        List<Movie> moviesByBaker = new ArrayList<>();
        moviesByBaker.add(Movie.createMovie(MOVIE_1));
        moviesByBaker.add(Movie.createMovie(MOVIE_4));

        assertEquals(moviesByBaker, explorer.getAllMoviesBy(new Actor("Benny", "Baker")));
    }

    @Test
    public void testGetMoviesSortedByReleaseYearMethod() {
        List<Movie> sorted = new ArrayList<>();
        sorted.add(Movie.createMovie(MOVIE_1));
        sorted.add(Movie.createMovie(MOVIE_2));
        sorted.add(Movie.createMovie(MOVIE_4));
        sorted.add(Movie.createMovie(MOVIE_3));

        assertEquals(sorted, explorer.getMoviesSortedByReleaseYear());
    }

    @Test
    public void testFindYearWithLeastNumberOfReleasedMoviesMethod() {
        assertEquals(LEAST_MOVIES_RELEASED_YEAR, explorer.findYearWithLeastNumberOfReleasedMovies());
    }

    @Test
    public void testFindMovieWithGreatestNumberOfActorsMethod() {
        assertEquals(Movie.createMovie(MOVIE_1), explorer.findMovieWithGreatestNumberOfActors());
    }
}
