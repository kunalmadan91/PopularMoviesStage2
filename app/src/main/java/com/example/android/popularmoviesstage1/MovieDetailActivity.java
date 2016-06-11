package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.popularmoviesstage1.Data.PopularMovies;

public class MovieDetailActivity extends AppCompatActivity {

    PopularMovies popularMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        if(getIntent() != null) {

            popularMovies = getIntent().getExtras().getParcelable("MOVIE_DATA_KEY");

            Bundle args = new Bundle();

            args.putParcelable("key",popularMovies);


            MovieDetailActivityFragment detailActivityFragment = new MovieDetailActivityFragment();

            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.movie_detail_container,detailActivityFragment).commit();
        }
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
