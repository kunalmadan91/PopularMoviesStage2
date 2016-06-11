package com.example.android.popularmoviesstage1.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesstage1.BuildConfig;
import com.example.android.popularmoviesstage1.MovieDetailActivity;
import com.example.android.popularmoviesstage1.MovieDetailActivityFragment;
import com.example.android.popularmoviesstage1.Data.PopularMovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KUNAL on 09-04-2016.
 */
public class FetchReviews extends AsyncTask<PopularMovies, Void, ArrayList<String>> {

    public Context mContext;

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    public FetchReviews(Context context) {
        mContext = context;
    }
    @Override
    protected ArrayList<String> doInBackground(PopularMovies... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewData = null;

        // Intent intent = getActivity().getIntent();
        PopularMovies movie = null;
        /*if (getArguments() != null) {
            movie = getArguments().getParcelable("key");
        }*/

        Integer movieId = params[0].getMovieId();
        try {

            final String REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews";

            final String API_KEY = "api_key";

            Uri uri = Uri.parse(REVIEW_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(uri.toString());

            Log.v(LOG_TAG, "URLReviews" + url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {

                Log.v(LOG_TAG, "input stream null");
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
            reviewData = stringBuffer.toString();

            Log.v(LOG_TAG, "movieReviewString>>>" + reviewData);
        } catch (Exception e) {

        }

        return getReviews(reviewData);
    }


    public ArrayList<String> getReviews(String reviewData) {

        final String MOVIE_LIST_RESULT = "results";

        ArrayList<String> data = new ArrayList<String>();

        try {
            JSONObject jsonObject = new JSONObject(reviewData);

            JSONArray movieArray = jsonObject.getJSONArray(MOVIE_LIST_RESULT);
            Log.v(LOG_TAG, "json array>>" + movieArray);


            Log.v(LOG_TAG, "moviearray" + movieArray.length());
            for (int i = 0; i < movieArray.length(); i++) {
                String content;

                JSONObject dataJson = movieArray.getJSONObject(i);

                content = dataJson.getString("content");

                data.add(content);
            }

            int a = data.size();

            Log.v(LOG_TAG, "size>>" + a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<String> results) {
        super.onPostExecute(results);

        if (results != null && results.size() != 0) {

            Log.v(LOG_TAG, "Hello");
            MovieDetailActivityFragment.mreviewAdapter.clear();
            Log.v(LOG_TAG, "results>>" + results.size());


            for (String review : results) {
                MovieDetailActivityFragment.mreviewAdapter.add(review);
            }

        } else {
            //mreviewAdapter = new ArrayAdapter<String>();
        }
        Log.v(LOG_TAG, "adapterkunal>>" + MovieDetailActivityFragment.mreviewAdapter.getCount());

    }
}
