/*package com.google.gwt.sample.moviebase.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.sample.moviebase.client.ParserInterface;*/

package amdb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amdb.shared.Movie;
import amdb.shared.MovieCollection;

/**
 * This class makes it possible to read a Stream consisting of movie data convert it to a MovieCollection.
 * 
 * @author petrawittwer
 * @author selinfabel
 * @history 2015-11-03 first complete version
 * @version 2015-11-03 1.0
 * @responsibilities Convert an input Stream to a MovieCollection.
 */
public class Parser {
	
	/**
	 * Takes an input Stream in the same format as the original source file and turns it into a MovieCollection.
	 * 
	 * @param in The movie data as an {@link InputStream}
	 * @return A movieBase containing Movie elements whose fields are specified in the input stream.
	 * @throws IOException
	 * @see {@link MovieCollectionService}
	 */
	public static MovieCollection parse(InputStream in) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

		ArrayList<Movie> resultArray = new ArrayList<Movie>();

		String[] lineArray; // holds the splitted lines

		// variable for each value of the Movie class
		String name;
		ArrayList<String> intermediateResult = new ArrayList<String>();
		String[] genres;
		String[] languages;
		String[] countries;
		int releaseDate = -1;
		int length = -1;
		
		// Pattern to match all the expressions of the form {"someTag":
		// "actualValue", "someTag": "actualValue" ...}
		// the actualValue is always at the index [1] of the returned array
		Pattern keyValuePattern = Pattern.compile("(?:\".*?\": \"(.*?)\")+");
		// Pattern to match the actualValue in the line
		Matcher keyValueMatcher;

		String line;
		while ((line = br.readLine()) != null) {
			releaseDate = -1;
			length = -1;

			// split the line by the tabs.
			lineArray = line.split("\\t", 9);
			if (lineArray.length != 9) {
				throw new RuntimeException("invalid line, incorrect amount of values: "
						+ line);
			}

			// set name
			name = lineArray[2];

			// set length
			if(!lineArray[5].isEmpty()){
				length = (int) Float.parseFloat(lineArray[5]);
			}

			// set releaseDate
			if (lineArray[3] != null && lineArray[3].length() >= 4) {
				releaseDate = Integer.parseInt(lineArray[3].substring(0, 4));
			}

			// set genres
			intermediateResult.clear();
			keyValueMatcher = keyValuePattern.matcher(lineArray[8]);
			while (keyValueMatcher.find()) {
				intermediateResult.add(keyValueMatcher.group(1));
			}
			genres = intermediateResult.toArray(new String[0]);

			// set languages
			intermediateResult.clear();
			keyValueMatcher = keyValuePattern.matcher(lineArray[6]);
			while (keyValueMatcher.find()) {
				intermediateResult.add(keyValueMatcher.group(1));
			}
			languages = intermediateResult.toArray(new String[0]);

			// set countries
			intermediateResult.clear();
			keyValueMatcher = keyValuePattern.matcher(lineArray[7]);
			while (keyValueMatcher.find()) {
				intermediateResult.add(keyValueMatcher.group(1));
			}
			countries = intermediateResult.toArray(new String[0]);

			resultArray.add(new Movie(name, length, releaseDate, genres, languages,
					countries));

		}

		br.close();
		return new MovieCollection(resultArray);
	}

}