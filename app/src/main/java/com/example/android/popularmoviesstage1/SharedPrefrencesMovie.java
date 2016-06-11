package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popularmoviesstage1.Data.PopularMovies;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KUNAL on 23-03-2016.
 */
public class SharedPrefrencesMovie {

    public static final String PREFS_NAME = "MOVIE_APP";
    public static final String FAVORITES = "Movie_Favorite";

    public SharedPrefrencesMovie() {
        super();
    }

    public void saveFavorites(Context context, List<PopularMovies> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = context.getSharedPreferences("",Context.)
        settings = context.getSharedPreferences(PREFS_NAME,
               Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

            editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, PopularMovies movies) {
        List<PopularMovies> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<PopularMovies>();
        favorites.add(movies);
        saveFavorites(context, favorites);
    }

    public ArrayList<PopularMovies> getFavorites(Context context) {
        SharedPreferences settings;
        List<PopularMovies> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            PopularMovies[] favoriteItems = gson.fromJson(jsonFavorites,
                    PopularMovies[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<PopularMovies>(favorites);
        } else
            return null;

        return (ArrayList<PopularMovies>) favorites;
    }

    public void removeFromFavourites(Context context, PopularMovies popularMovies) {
            ArrayList<PopularMovies> movies = getFavorites(context);


            for(int i = 0; i < movies.size(); i ++ ){
                PopularMovies movie = movies.get(i);
                if(movie.getMovieId().equals(popularMovies.getMovieId())){
                    movies.remove(i);
                }
            }
            // movies = new ArrayList<PopularMovies>();
            //list size is getting zero here
            // boolean test = movies.remove(popularMovies.toString().equals(movies));
            saveFavorites(context, (List<PopularMovies>) movies);


        int size = movies.size();
    }
}
