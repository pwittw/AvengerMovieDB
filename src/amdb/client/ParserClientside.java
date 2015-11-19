package amdb.client;

import amdb.shared.Movie;
import amdb.shared.MovieCollection;

import com.google.gwt.core.shared.GWT;

/**
 * This class contains a method that takes a string that was preprocessed by <code>ParserPreprocessing.parse(InputStream in)</code>
 *  and creates a {@link MovieCollection} from it.
 * @author petrawittwer
 * @history 2015-11-16 PW first version
 * @version 2015-11-16 PW 1.0
 * @responsibilities This class is responsible for creating a <code>MovieCollection</code> from a given <code>String</code>
 * 						that was preprocessed by <code>ParserPreprocessing.parse(InputStream in)</code>.
 */
public class ParserClientside {

	/**
	 * This class takes a string that was preprocessed by <code>ParserPreprocessing.parse()</code>
	 *  and creates a {@link MovieCollection} from it.
	 * @pre source is the output of <code>ParserPreprocessing.parse()</code> as a String.
	 * @post return value contains the <code>Movie</code>-objects specified in source.
	 * @param source The preprocessed file as a <code>String</code>
	 * @return A <code>MovieCollection</code> filled with <code>Movie</code>-objects specified in <code>source</code>.
	 */
	public static MovieCollection stringToMovieCollection(String source){

		String[] movieAttributes;
		MovieCollection movies = new MovieCollection();

		for (String line : source.split("\n")) {
			movieAttributes = line.split("\t");
			// Create Movie from name, length, releaseDate, genres, languages, countries
			movies.addMovie(new Movie(movieAttributes[0], 
					Float.parseFloat(movieAttributes[2]), 
					Integer.parseInt(movieAttributes[1]), 
					movieAttributes[5].split("\\|") , 
					movieAttributes[3].split("\\|"), 
					movieAttributes[4].split("\\|")));			
		}
		GWT.log("parsed "+movies.getMovies().size()+" movies");

		return movies;

	}

}