package com.example.android.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Adapters.PopularMovieAdapter;
import com.example.android.popularmoviesstage1.AsyncTasks.FetchMoviesTask;
import com.example.android.popularmoviesstage1.Data.PopularMovies;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    //public static PopularMovies popularMovies;
    //static PopularMovieAdapter popularMovieAdapter;
    static GridView gridView;
    private boolean isSavedInstanceState = false;
    Activity activity;
    SharedPrefrencesMovie prefrencesMovie;
    String val;
    View rootView;
    private FragmentInterface fragmentInterface;
    private boolean mTablet;
    //private ArrayList<PopularMovies> movies = new ArrayList<PopularMovies>();


    public static MainActivityFragment newInstance(FragmentInterface fragmentInterface) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("km", FetchMoviesTask.popularMovies);
        fragment.setArguments(bundle);
        fragment.fragmentInterface = fragmentInterface;

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("data")) {
            isSavedInstanceState = true;
            FetchMoviesTask.movies = savedInstanceState.getParcelableArrayList("data");
            Log.v(LOG_TAG, "isSavedInstanceState2" + isSavedInstanceState);
        }

        activity = getActivity();
        prefrencesMovie = new SharedPrefrencesMovie();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("data", FetchMoviesTask.movies);
        super.onSaveInstanceState(outState);
        Log.v(LOG_TAG, "isSavedInstanceState1" + isSavedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FetchMoviesTask.popularMovieAdapter = new PopularMovieAdapter(getActivity(),
                new ArrayList<PopularMovies>());
        gridView = (GridView) rootView.findViewById(R.id.movies_grid);

        gridView.setAdapter(FetchMoviesTask.popularMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTablet = ((MainActivity) getActivity()).isTablet();

                PopularMovies movies = FetchMoviesTask.popularMovieAdapter.getItem(position);
                if (!mTablet) {

                    Intent movieDetailActivityIntent = new Intent(getActivity(),
                            MovieDetailActivity.class);
                    movieDetailActivityIntent.putExtra("MOVIE_DATA_KEY", movies);
                    startActivity(movieDetailActivityIntent);
                } else {
                    fragmentInterface.replaceFragment(movies);
                }
            }
        });

        return rootView;
    }

    public boolean isConnectingToInternet() {

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public void onStart() {

        super.onStart();
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(getActivity());

        val = new String();
        val = preferences.getString(getString(R.string.pref_sort_key)
                , getString(R.string.pref_units_popular));
        if (isConnectingToInternet()) {
            Log.v("Connected to internet", "Connected to internet");

            //checking confition for favourites to stop async task
            if (!val.equals("favourites")) {



                    FetchMoviesTask moviesTask = new FetchMoviesTask(getContext());
                    moviesTask.execute();



                    FetchMoviesTask.popularMovieAdapter = new PopularMovieAdapter(getActivity(),
                            FetchMoviesTask.movies);
                    //popularMovieAdapter.notifyDataSetChanged();

                    gridView.setAdapter(FetchMoviesTask.popularMovieAdapter);



            }

            if (val.equals("favourites")) {
                ArrayList<PopularMovies> movie = prefrencesMovie.getFavorites(getContext());

                if (movie == null || movie.size() == 0) {
                    Toast.makeText(getActivity(), "Oops!! No Favourite Movies present," +
                                    " Please change sorting criteria",
                            Toast.LENGTH_LONG).show();
                    FetchMoviesTask.popularMovieAdapter.clear();

                    FetchMoviesTask.popularMovieAdapter.notifyDataSetChanged();
                } else {

                    FetchMoviesTask.popularMovieAdapter = new PopularMovieAdapter(getActivity(), movie);
                    //popularMovieAdapter.notifyDataSetChanged();

                    gridView.setAdapter(FetchMoviesTask.popularMovieAdapter);
                    // popularMovieAdapter.notifyDataSetChanged();
                }

            }
        } else {

            Log.v("Connected to internet", " not Connected to internet");
            Toast.makeText(getContext(), "You are not connected to Internet!!" +
                    "Please connect to Internet and try again", Toast.LENGTH_SHORT).show();
        }


    }

    public interface FragmentInterface {
        void replaceFragment(PopularMovies movies);
    }



}
