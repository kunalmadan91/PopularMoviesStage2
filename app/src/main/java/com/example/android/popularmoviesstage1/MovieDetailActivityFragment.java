package com.example.android.popularmoviesstage1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Adapters.PopularMovieAdapter;
import com.example.android.popularmoviesstage1.Adapters.VideoAdapter;
import com.example.android.popularmoviesstage1.AsyncTasks.FetchMoviesTask;
import com.example.android.popularmoviesstage1.AsyncTasks.FetchReviews;
import com.example.android.popularmoviesstage1.AsyncTasks.FetchTrailerVideo;
import com.example.android.popularmoviesstage1.Data.PopularMovies;
import com.example.android.popularmoviesstage1.Data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {


    public static   ArrayAdapter<String> mreviewAdapter;

    public static PopularMovies movie = null;

    public static VideoAdapter videoAdapter;
    Activity activity;
    SharedPrefrencesMovie prefrencesMovie;
    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Bind(R.id.detail_title) TextView titleView;

    @Bind(R.id.detail_date) TextView release;

    @Bind(R.id.detail_image) ImageView imageView;

    @Bind(R.id.detail_rating) TextView avg;

    @Bind(R.id.detail_summary) TextView summary;

    @Bind(R.id.favourite)  ImageView button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        prefrencesMovie = new SharedPrefrencesMovie();
    }

    public MovieDetailActivityFragment() {
    }

    public String getLOG_TAG() {

        return LOG_TAG;
    }


    public void onStart() {
        //PopularMovies movie = null;
        movie = getArguments().getParcelable("key");
        super.onStart();
        FetchTrailerVideo fetchTrailerVideo = new FetchTrailerVideo(getContext());
        FetchReviews fetchReviews = new FetchReviews(getContext());
        fetchReviews.execute(movie);
        fetchTrailerVideo.execute(movie);
    }


    public boolean checkFavouriteItem(PopularMovies movies) {
        boolean check = false;
        List<PopularMovies> favourites = prefrencesMovie.getFavorites(getContext());

        Log.v(LOG_TAG, "movies" + movies.getTitle());
        if (favourites != null) {
            for (PopularMovies movies1 : favourites) {
                if (movies1.getTitle().equals(movies.getTitle())) {
                    Log.v(LOG_TAG, "movies1" + movies1.getTitle());
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Intent intent = getIntent();
        // Movie movie = getArguments().getParcelable("Movie");
       /* if(mreviewAdapter.getCount() == 0) {
            Toast.makeText(getContext(), "Hello kunal", Toast.LENGTH_SHORT).show();
        }*/




        mreviewAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.review_item, R.id.textview
                , new ArrayList<String>());


        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //ButterKnife.bind(this, rootView);

        ButterKnife.bind(this,rootView);

        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        //setListViewHeightBasedOnChildren(listView.setAdapter(mreviewAdapter));

        listView.setAdapter(mreviewAdapter);


        //Intent intent = getActivity().getIntent();

        videoAdapter = new VideoAdapter(getActivity(), new ArrayList<String>());

        ListView view = (ListView) rootView.findViewById(R.id.video);

        view.setAdapter(videoAdapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = videoAdapter.getItem(position);
                Trailer trailer = null;

                Log.v(LOG_TAG, "VideoPos>>" + position + " " + key);

                Log.v(LOG_TAG, "sval" + key);
                String url = "http://www.youtube.com/watch?v=" + key;
                Log.v(LOG_TAG, "keyclass>" + key);
                Uri uri = Uri.parse(url);

                startActivity(new Intent(Intent.ACTION_VIEW, uri));

            }
        });


        PopularMovies movie = null;


        if (getArguments() != null) {
            movie = getArguments().getParcelable("key");


            String thumbNailUrl = movie.getThumbNail();
            String title = movie.getTitle();
            String releaseDate = movie.getReleaseDate();
           // Integer movieId = movie.getMovieId();
            String average = movie.getAverage();
            String overView = movie.getOverView();

            //@Bind(R.id.detail_title) TextView titleView;

            /*TextView titleView = (TextView) rootView.findViewById(R.id.detail_title);
            TextView release = (TextView) rootView.findViewById(R.id.detail_date);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
            TextView avg = (TextView) rootView.findViewById(R.id.detail_rating);
            TextView summary = (TextView) rootView.findViewById(R.id.detail_summary);*/


            //for favourites
            final ImageView button = (ImageView) rootView.findViewById(R.id.favourite);

            // button.setImageResource(R.drawable.heart_grey);

            final PopularMovies finalMovie = movie;


            if (checkFavouriteItem(finalMovie)) {
                button.setImageResource(R.drawable.heart_red);
                button.setTag("red");
            } else {
                button.setImageResource(R.drawable.heart_grey);
                button.setTag("grey");
            }

            button.setOnClickListener(new View.OnClickListener() {
                                          @Override
                          public void onClick(View v) {


                              String tag = button.getTag().toString();
                              if (tag.equalsIgnoreCase("grey")) {

                                  prefrencesMovie.addFavorite(activity, finalMovie);


                                  SharedPreferences preferences = PreferenceManager.
                                          getDefaultSharedPreferences(getActivity());
                                  String val = new String();
                                  val = preferences.getString(getString(R.string.pref_sort_key)
                                          , getString(R.string.pref_units_popular));

                                  if (val.equals("favourites")) {
                                      ArrayList<PopularMovies> movie = prefrencesMovie.getFavorites(getContext());

                                      if (movie == null || movie.size() == 0) {
                                          Toast.makeText(getActivity(), "Oops!! No Favourite Movies present," +
                                                          " Please change sorting criteria",
                                                  Toast.LENGTH_LONG).show();
                                          FetchMoviesTask.popularMovieAdapter.clear();
                                          // popularMovieAdapter.remove(movie.get(i));
                                          // popularMovieAdapter = new PopularMovieAdapter(getContext(),movie);
                                          // popularMovieAdapter = new PopularMovieAdapter(getContext(),new ArrayList<PopularMovies>());
                                          FetchMoviesTask.popularMovieAdapter.notifyDataSetChanged();
                                      } else {

                                          FetchMoviesTask.popularMovieAdapter = new
                                                  PopularMovieAdapter(getActivity(), movie);
                                          //popularMovieAdapter.notifyDataSetChanged();

                                          MainActivityFragment.gridView.
                                                  setAdapter(FetchMoviesTask.popularMovieAdapter);
                                          // popularMovieAdapter.notifyDataSetChanged();
                                      }


                                      button.setTag("grey");
                                      button.setImageResource(R.drawable.heart_grey);
                                      Toast.makeText(getActivity(), "Movie removed from Favourite list successfully!!",
                                              Toast.LENGTH_SHORT).show();
                                  }

                                  //temp


                                  Toast.makeText(getActivity(), "Movie added to Favourites successfully!!",
                                          Toast.LENGTH_SHORT).show();

                                  button.setTag("red");
                                  button.setImageResource(R.drawable.heart_red);
                              } else if (tag.equals("red")) {

        /*MainActivityFragment mainActivityFragment = new MainActivityFragment();

        mainActivityFragment.displayFavorites();*/
                                  prefrencesMovie.removeFromFavourites(activity, finalMovie);


                                  String val = new String();
                                  SharedPreferences preferences = PreferenceManager.
                                          getDefaultSharedPreferences(getActivity());

                                  val = preferences.getString(getString(R.string.pref_sort_key)
                                          , getString(R.string.pref_units_popular));
                                  if (val.equals("favourites")) {


                                      ArrayList<PopularMovies> movie = prefrencesMovie.getFavorites(getContext());

                                      if (movie == null || movie.size() == 0) {
                                          Toast.makeText(getActivity(), "Oops!! No Favourite Movies present," +
                                                          " Please change sorting criteria",
                                                  Toast.LENGTH_LONG).show();
                                          FetchMoviesTask.popularMovieAdapter.clear();
                                          FetchMoviesTask.popularMovieAdapter.notifyDataSetChanged();
                                      } else {

                                          FetchMoviesTask.popularMovieAdapter = new
                                                  PopularMovieAdapter(getActivity(), movie);
                                          //popularMovieAdapter.notifyDataSetChanged();

                                          MainActivityFragment.gridView.
                                                  setAdapter(FetchMoviesTask.popularMovieAdapter);
                                          // popularMovieAdapter.notifyDataSetChanged();
                                      }


                                  }
                                  button.setTag("grey");
                                  button.setImageResource(R.drawable.heart_grey);
                                  Toast.makeText(getActivity(), "Movie removed from Favourite list successfully!!",
                                          Toast.LENGTH_SHORT).show();

                              }
                          }
                      }

            );

            avg.setText(average);
            titleView.setText(title);
            summary.setText(overView);
            release.setText(releaseDate);

            final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185/";

            String url = THUMBNAIL_BASE_URL.concat(thumbNailUrl);

            Picasso.with(getContext()).load(url).into(imageView);

            Picasso.with(getContext()).load(url).placeholder(R.drawable.icon).error(R.drawable.icon)
                    .into(imageView);

            Log.v(LOG_TAG, "movie detal data " + thumbNailUrl + title + releaseDate);
        }
        return rootView;
    }





}
