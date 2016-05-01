package com.pji.de.awareway;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pji.de.awareway.activity.LoginActivity;
import com.pji.de.awareway.activity.SignInActivity;
import com.pji.de.awareway.activity.UserPanelActivity;
import com.pji.de.awareway.fragments.AboutFragment;
import com.pji.de.awareway.fragments.HomeFragment;
import com.pji.de.awareway.fragments.MapFragment;
import com.pji.de.awareway.fragments.NotesFragment;
import com.pji.de.awareway.fragments.PreferencesFragment;
import com.pji.de.awareway.utilitaires.AwarePreferences;
import com.pji.de.awareway.utilitaires.UserManager;
import com.pji.de.awareway.webbridge.AABridge;

import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Boolean DEBUG = false;

    public static String preferenceVue="Carte";

    public static UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerView.findViewById(R.id.header_view);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserView();
            }
        });

        userManager = new UserManager();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return TODO;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, HomeFragment.newInstance())
                .commit();

        AwarePreferences.init(this);
        if(AwarePreferences.getBooleanPreference(AwarePreferences.GOOGLE_AUTHENTIFIED)){
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }
    }

    public void showUserView(){
        if(userManager.isAuthentified()){
            Intent intent = new Intent(this,UserPanelActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Se connecter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(!userManager.isAlreadyUpdated()){
            updateUserHeader();
            userManager.notifyUpdate();
        }
    }

    private void updateUserHeader() {
        Log.d(MainActivity.class.getName(), "updateUserHeader start");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerView.findViewById(R.id.header_view);
        header = (LinearLayout) headerView.findViewById(R.id.header_view);
        CircleImageView imageView = (CircleImageView) header.findViewById(R.id.profile_image);

        TextView textGras = (TextView) header.findViewById(R.id.header_text_bold);
        Log.d(MainActivity.class.getName(), "User  : " + userManager.getUser());
        Log.d(MainActivity.class.getName(), "Authentificated  : " +MainActivity.userManager.isAuthentified());
        if (userManager.isAuthentified()) {
            Log.d(MainActivity.class.getName(), "is Authentified");
            textGras.setText(R.string.account);
            if (userManager.isImaged()) {
                BitmapLoaderTask bitmapLoaderTask = new BitmapLoaderTask(userManager.getProfilImageURL(), imageView);
                bitmapLoaderTask.execute((Void) null);
            } else {
                Log.d(MainActivity.class.getName(), "without profil pic");
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        } else {
            Log.d(MainActivity.class.getName(), "not Authentified");
            textGras.setText(R.string.tologin);
            //TODO change image
            int id = getResources().getIdentifier("@android:drawable/ic_menu_edit", null, null);
            imageView.setImageResource(id);
        }
        Log.d(MainActivity.class.getName(), "updateUserHeader end");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_notes) {
            fragment = new NotesFragment();
        } else if (id == R.id.nav_map) {
            fragment = new MapFragment();
        } else if (id == R.id.nav_manage) {
            fragment = new PreferencesFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_website) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AABridge.CONTEXT_PATH));
            startActivity(browserIntent);
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setHeaderImage(Bitmap image, CircleImageView view){
        view.setImageBitmap(image);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class BitmapLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        private final CircleImageView imageView;
        private final String urlImage;

        BitmapLoaderTask(Uri urlImage, CircleImageView imageView) {
            this.imageView = imageView;
            this.urlImage = urlImage.toString();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap profilPic = null;
            try {
                URL imageURL = new URL(urlImage);
                profilPic = AABridge.getBitmapFromURL(imageURL);
            } catch (MalformedURLException e) {
                Log.d("Parse image url",e.getMessage());
            }
            return profilPic;
        }

        @Override
        protected void onPostExecute(final Bitmap success) {
            setHeaderImage(success, imageView);
        }

    }
}
