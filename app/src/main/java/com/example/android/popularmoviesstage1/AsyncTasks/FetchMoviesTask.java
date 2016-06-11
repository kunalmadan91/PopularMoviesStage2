package com.example.android.popularmoviesstage1.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.BuildConfig;
import com.example.android.popularmoviesstage1.MovieDetailActivityFragment;
import com.example.android.popularmoviesstage1.Adapters.PopularMovieAdapter;
import com.example.android.popularmoviesstage1.Data.PopularMovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KUNAL on 09-04-2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<PopularMovies>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private Context context;
    public static ArrayList<PopularMovies> movies = new ArrayList<PopularMovies>();
    //SharedPrefrencesMovie prefrencesMovie;
    public static PopularMovies popularMovies;
    public static PopularMovieAdapter popularMovieAdapter;


    public FetchMoviesTask(Context mcontext) {

        context = mcontext;

    }

    @Override
    protected ArrayList<PopularMovies> doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieDataString = null;


        //getting sort value which are popular and top_rated and passing to the API as shown below
        String sortVal = getSortparam();

        try {

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + sortVal + "?";

            final String API_KEY = "api_key";

            Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(uri.toString());

            Log.v(LOG_TAG, "URL>>>" + url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }


            if (stringBuffer.length() == 0) {
                return null;
            }
            Log.v(LOG_TAG, "stringBuffer>>" + stringBuffer);
            movieDataString = stringBuffer.toString();

            Log.v(LOG_TAG, "movieDataString>>>" + movieDataString);

        } catch (IOException exception) {

            Log.e(LOG_TAG, "Error " + exception);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieDataString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSortparam() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String sortType = preferences.getString("sort"
                , "popular");

        Log.v(LOG_TAG, "sorttype " + sortType);

        return sortType;
    }

    public ArrayList<PopularMovies> getMovieDataFromJson(String movieData) throws JSONException {

        final String MOVIE_LIST_RESULT = "results";

        JSONObject jsonObject = new JSONObject(movieData);

        JSONArray movieArray = jsonObject.getJSONArray(MOVIE_LIST_RESULT);

        Log.v(LOG_TAG, "json array>>" + movieArray);

        if (movieArray == null) {
            Toast.makeText(context, "No Data Exists", Toast.LENGTH_SHORT).show();

            return null;
        }


        ArrayList<PopularMovies> pictureCollection = new ArrayList<PopularMovies>();

        for (int i = 0; i < movieArray.length(); i++) {
            String posterPath;
            Integer movieId;
            String title;
            String thumbNail;
            String releaseDate;
            String overView;
            Double average;


            JSONObject data = movieArray.getJSONObject(i);

            posterPath = data.getString("poster_path");
            movieId = data.getInt("id");
            title = data.getString("title");
            thumbNail = data.getString("backdrop_path");
            releaseDate = data.getString("release_date");
            overView = data.getString("overview");
            average = data.getDouble("vote_average");

            String avgRating = average.toString().concat("/10");


            String date[] = releaseDate.split("-");
            String year = date[0];

            Log.v(LOG_TAG, "average data>>" + average);
            PopularMovies movies = new
                    PopularMovies(movieId, posterPath, title, thumbNail, overView, avgRating, year);


            Log.v(LOG_TAG, "image id>>" + movieId);
            Log.v(LOG_TAG, "Movies data>>" + movies);
            pictureCollection.add(movies);
        }

        return pictureCollection;
    }


    @Override
    protected void onPostExecute(ArrayList<PopularMovies> result) {
        super.onPostExecute(result);

        movies = result;
        if (movies.size() != 0) {
            popularMovieAdapter.clear();
            popularMovieAdapter.addAll(movies);

            MovieDetailActivityFragment detailActivityFragment =
                    new MovieDetailActivityFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("kn", result.get(0));
            detailActivityFragment.setArguments(bundle);
            popularMovies = result.get(0);

        } else
            Toast.makeText(context, "No data fetched", Toast.LENGTH_LONG).show();
    }
}
