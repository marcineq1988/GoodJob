package mpikula.goodjob;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mpikula.goodjob.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnLongClickListener {

    OrientationEventListener mOrientationListener;

    private Button mButtonSzukaj, mButtonKasuj;
    private EditText mEditTextPraca;
    private EditText mEditTextMiejsce;
    private FloatingActionButton fab;
    private RoundedImageView circleImageBackground;
    public static String nazwaStanowiska;
    public static String nazwaMiejscowosci;
    public static int animationNumber = 1;
    public static int animationNumberToPass = 1;
    ArrayList<String> arrayFav = new ArrayList<String>();
    ArrayList<String> arrayLin = new ArrayList<String>();
    ArrayList<String> arrayFavAndLin = new ArrayList<String>();
    private ListView mDrawerList;
    public ArrayAdapter<String> mAdapter;

    Menu subMenu;
    int count = 0;

    private SharedPreferences preferences;
    private static final String PREFERENCES_NAME = "myPreferences";
    private static final String PREFERENCES_TEXT_FIELD_JOB = "textFieldJob";
    private static final String PREFERENCES_TEXT_FIELD_PLACE = "textFieldPlace";
    private static final String IS_EMPTY = "isEmpty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawerList = (ListView)findViewById(R.id.navList);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu mainMenu = navigationView.getMenu();
        subMenu = mainMenu.addSubMenu("Ulubione oferty: ");
        circleImageBackground = (RoundedImageView)findViewById(R.id.imageCircleBackground);
        mButtonSzukaj = (Button) findViewById(R.id.buttonSzukaj);
        mButtonKasuj = (Button) findViewById(R.id.buttonKasuj);
        mEditTextPraca = (EditText)findViewById(R.id.editTextPraca);
        //mEditTextPraca.setText("junior developer");

        mEditTextMiejsce = (EditText)findViewById(R.id.editTextMiejsce);
        //mEditTextMiejsce.setText("Wrocław");

        mButtonSzukaj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count++;

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    if (TextUtils.isEmpty(mEditTextPraca.getText().toString()) && (TextUtils.isEmpty(mEditTextMiejsce.getText().toString()))) {
                        Toast.makeText(MainActivity.this, "Uzupełnij puste pola!", Toast.LENGTH_LONG).show();
                        return;
                    } else if (TextUtils.isEmpty(mEditTextPraca.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Uzupełnij puste pola!", Toast.LENGTH_LONG).show();
                        return;
                    } else if (TextUtils.isEmpty(mEditTextMiejsce.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Uzupełnij puste pola!", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        nazwaStanowiska = mEditTextPraca.getText().toString();
                        nazwaMiejscowosci = mEditTextMiejsce.getText().toString();
                        animationNumberToPass = animationNumber;
                        Intent myIntent = new Intent(MainActivity.this, ListviewActivity.class);
                        MainActivity.this.startActivityForResult(myIntent, 1);
                        ListviewActivity.mListaTest1.clear();
                        ListviewActivity.mListaTest2.clear();
                        ListviewActivity.mListaLinki.clear();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Brak połączenia z siecią!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonKasuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextPraca.setText("");
                mEditTextMiejsce.setText("");
                Toast.makeText(MainActivity.this, "Skasowano wprowadzone dane...", Toast.LENGTH_LONG).show();
            }
        });
        restoreData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void saveData() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        String editTextDataJob = mEditTextPraca.getText().toString();
        preferencesEditor.putString(PREFERENCES_TEXT_FIELD_JOB, editTextDataJob);
        String editTextDataPlace = mEditTextMiejsce.getText().toString();
        preferencesEditor.putString(PREFERENCES_TEXT_FIELD_PLACE, editTextDataPlace);
        preferencesEditor.commit();
    }

    private void restoreData() {
        String textFromPreferencesJob = preferences.getString(PREFERENCES_TEXT_FIELD_JOB, "");
        mEditTextPraca.setText(textFromPreferencesJob);
        String textFromPreferencesPlace = preferences.getString(PREFERENCES_TEXT_FIELD_PLACE, "");
        mEditTextMiejsce.setText(textFromPreferencesPlace);
    }



    public void onDataRestoredAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if(mEditTextMiejsce.getText().toString().length() > 0 && mEditTextPraca.getText().toString().length() > 0){
            builder.setMessage("Zapamiętano poprzednie parametry wyszukiwania: "
                    + mEditTextPraca.getText().toString().trim() +", "+ mEditTextMiejsce.getText().toString().trim()
                    +". Czy przywrócić zapamiętane parametry?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mEditTextPraca.setText("");
                    mEditTextMiejsce.setText("");
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(mEditTextPraca.getText().toString().length() > 0){
            builder.setMessage("Zapamiętano poprzednie parametry wyszukiwania: "
                    + mEditTextPraca.getText().toString().trim()
                    +". Czy przywrócić zapamiętane parametry?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mEditTextPraca.setText("");
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(mEditTextMiejsce.getText().toString().length() > 0){
            builder.setMessage("Zapamiętano poprzednie parametry wyszukiwania: "
                    + mEditTextMiejsce.getText().toString().trim()
                    +". Czy przywrócić zapamiętane parametry?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mEditTextMiejsce.setText("");
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        subMenu.clear();
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                ArrayList<String> passedText = data.getStringArrayListExtra("text");
                ArrayList<String> passedLink = data.getStringArrayListExtra("link");

                arrayFav.addAll(passedText);
                arrayLin.addAll(passedLink);

                onArrayMerge();

                for (int i = 0; i < arrayFav.size(); i++) {
                    MenuItem item = subMenu.add(arrayFav.get(i));

                    final int count = i;
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(MainActivity.this, "Otwieranie oferty...", Toast.LENGTH_SHORT).show();
                            Intent myBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayLin.get(count)));
                            myBrowserIntent.putExtra("paramPosition", count);
                            startActivity(myBrowserIntent);
                            return false;
                        }
                    });
                    item.hasSubMenu();
                }
            }
        }
    }

    public void onArrayMerge(){
        arrayFavAndLin.clear();
        for(int i=0; i<arrayFav.size();i++){
            arrayFavAndLin.add(arrayFav.get(i).toString()+arrayLin.get(i).toString());
        }
    }

    public void onExportPressed(){
        CharSequence colors[] = new CharSequence[] {"SMS", "E-mail", "Plik", "Wstecz"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eksportuj jako:");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0: //Sms
                        Uri uri = Uri.parse("smsto:");
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", arrayFavAndLin.toString());
                        startActivity(it);
                        break;
                    case 1: //Email
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Oferty pracy");
                        intent.putExtra(Intent.EXTRA_TEXT, arrayFavAndLin.toString());
                        startActivity(Intent.createChooser(intent, "Send Email"));
                        break;
                    case 2: //Plik
                        File root = android.os.Environment.getExternalStorageDirectory();
                        File dir = new File(root.getAbsolutePath() + "/download");
                        Toast.makeText(getApplicationContext(), "Eksport do pliku: " + dir.toString() + "/GoodJob.txt", Toast.LENGTH_LONG).show();
                        dir.mkdirs();
                        File file = new File(dir, "GoodJob.txt");
                        try {
                            FileOutputStream f = new FileOutputStream(file);
                            PrintWriter pw = new PrintWriter(f);
                            pw.print("");
                            pw.flush();
                            pw.print(arrayFavAndLin.toString());
                            pw.flush();
                            pw.close();
                            f.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3: //Wstecz
                        break;
                }
            }
        });
        builder.show();
    }

    public void onExitPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Koniec szukania pracy?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClearPressed(){
        DrawerLayout mDrawerLayout;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();
        subMenu.clear();
        arrayFav.clear();
        arrayLin.clear();
    }

    public void onInfoPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.info);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        int[] images = new int[] {R.drawable.mini_profile1, R.drawable.mini_profile2, R.drawable.mini_profile3,R.drawable.mini_profile4, R.drawable.mini_profile5};
        RoundedImageView mImageView = (RoundedImageView)findViewById(R.id.imageCircleBackground);
        int imageId = (int)(Math.random() * images.length);
        mImageView.setBackgroundResource(images[imageId]);
        super.onResume();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_menu_name) {
            return true;
        }
        if (id == R.id.action_menu_item1) {
            animationNumber = 1;
            return true;
        }
        if (id == R.id.action_menu_item2) {
            animationNumber = 2;
            return true;
        }
        if (id == R.id.action_menu_item3) {
            animationNumber = 3;
            return true;
        }
        if (id == R.id.action_menu_item4) {
            animationNumber = 4;
            return true;
        }
        if (id == R.id.action_menu_item5) {
            animationNumber = 5;
            return true;
        }
        if (id == R.id.action_menu_item6) {
            animationNumber = 6;
            return true;
        }
        if (id == R.id.action_menu_item7) {
            animationNumber = 7;
            return true;
        }
        if (id == R.id.action_menu_item8) {
            animationNumber = 8;
            return true;
        }
        if (id == R.id.action_menu_item9) {
            animationNumber = 9;
            return true;
        }
        if (id == R.id.action_menu_item10) {
            animationNumber = 10;
            return true;
        }
        if (id == R.id.action_menu_item11) {
            animationNumber = 11;
            return true;
        }
        if (id == R.id.action_menu_item12) {
            animationNumber = 12;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_back) {}
        else if (id == R.id.nav_export) {
            onExportPressed();
        }
        else if (id == R.id.nav_exit) {
            onExitPressed();
        }
        else if (id == R.id.nav_clear) {
            onClearPressed();
        }
        else if (id == R.id.nav_info) {
            onInfoPressed();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}