package com.example.android.popularmoviesstage1.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.BuildConfig;
import com.example.android.popularmoviesstage1.MovieDetailActivityFragment;
import com.example.android.popularmoviesstage1.Data.PopularMovies;
import com.example.android.popularmoviesstage1.Data.Trailer;

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
public class FetchTrailerVideo extends AsyncTask<PopularMovies, Void, ArrayList<String>> {

    public final String LOG_TAG = FetchTrailerVideo.class.getSimpleName();
    public Context mContext;
    public Activity activity;


    public FetchTrailerVideo(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<String> doInBackground(PopularMovies... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerData = null;


        //Intent intent = getActivity().getIntent();

        PopularMovies movie = null;




        Integer movieId = params[0].getMovieId();
        try {

            final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/videos";

            final String API_KEY = "api_key";

            Uri uri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(uri.toString());

            Log.v(LOG_TAG, "URLVideos" + url);

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
            trailerData = stringBuffer.toString();

            Log.v(LOG_TAG, "movieTrailerString>>>" + trailerData);

            // Log.v(LOG_TAG, "movieReviewString>>>" + reviewData);
        } catch (IOException exception) {

            Log.e(LOG_TAG, "Error " + exception);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
            if (reader != null) {
                try {
                    reader.close();
                    //readerReview.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {

        } catch (Exception e) {

        }

        return getTrailerData(trailerData);

    }


    public ArrayList<String> getTrailerData(String trailerdata) {

        final String MOVIE_LIST_RESULT = "results";

        ArrayList<String> data = new ArrayList<String>();

        try {
            JSONObject jsonObject = new JSONObject(trailerdata);

            JSONArray movieArray = jsonObject.getJSONArray(MOVIE_LIST_RESULT);
            Log.v(LOG_TAG, "json array>>" + movieArray);

            for (int i = 0; i < movieArray.length(); i++) {
                String key;

                JSONObject dataJson = movieArray.getJSONObject(i);

                key = dataJson.getString("key");

                Trailer trailer = new Trailer(key);
                data.add(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);

        if (result != null) {

            MovieDetailActivityFragment.videoAdapter.clear();
            MovieDetailActivityFragment.videoAdapter.addAll(result);
        } else {
            Toast.makeText(mContext, "No Trailers present for this Movie!",
                    Toast.LENGTH_SHORT).show();
        }


    }

}
