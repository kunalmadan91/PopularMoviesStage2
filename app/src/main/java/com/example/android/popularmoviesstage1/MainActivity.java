package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmoviesstage1.Data.PopularMovies;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.FragmentInterface {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Boolean mTabletMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // PopularMovies movie = MainActivityFragment.getMovie();

        // Log.v(LOG_TAG,"movieObj"+movie);


        if (savedInstanceState == null) {
            MainActivityFragment mainActivityFragment = MainActivityFragment.newInstance(this);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.main, mainActivityFragment).commit();

            if (findViewById(R.id.movie_detail_container) != null) {
                mTabletMode = true;



                BlankFragment blankFragment = new BlankFragment();


                getSupportFragmentManager().beginTransaction().
                        replace(R.id.movie_detail_container,
                                blankFragment).commit();
            }


        }

    }


    public boolean isTablet() {
        return mTabletMode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void replaceFragment(PopularMovies movies) {

        if (movies.getMovieId() == null) {
            Log.i("tab", "replace");
        }
        Bundle args = new Bundle();
        args.putParcelable("key", movies);
        // args.putString("ARGUMENTS", "Created from MainActivity");


        MovieDetailActivityFragment detailFragment = new MovieDetailActivityFragment();
        detailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.movie_detail_container, detailFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivityIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}
