package mpikula.goodjob;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
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

import com.mpikula.goodjob.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnLongClickListener {

    private Button mButtonSzukaj;
    private EditText mEditTextPraca;
    private EditText mEditTextMiejsce;
    private ImageView imageJobs;

    public static String nazwaStanowiska;
    public static String nazwaMiejscowosci;

    ArrayList<String> arrayFav = new ArrayList<String>();
    ArrayList<String> arrayLin = new ArrayList<String>();

    Menu subMenu;

    private ListView mDrawerList;
    public ArrayAdapter<String> mAdapter;
    private Settings mSettings;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList = (ListView)findViewById(R.id.navList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Daj znać, czy Ci się podoba! :)"+"  Marcin Pikula", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu mainMenu = navigationView.getMenu();
        subMenu = mainMenu.addSubMenu("Ulubione oferty: ");


        imageJobs = (ImageView)findViewById(R.id.imageJob);
        mButtonSzukaj = (Button) findViewById(R.id.buttonSzukaj);

        mEditTextPraca = (EditText)findViewById(R.id.editTextPraca);
        //mEditTextPraca.setText("junior developer");

        mEditTextMiejsce = (EditText)findViewById(R.id.editTextMiejsce);
        //mEditTextMiejsce.setText("Wroclaw");




        int min = 1;
        int max = 6;

        Random r = new Random();
        int randomizer = r.nextInt(max - min + 1) + min;

        switch(randomizer){
            case 1: imageJobs.setBackgroundResource(R.drawable.profil1);
                break;
            case 2: imageJobs.setBackgroundResource(R.drawable.profil2);
                break;
            case 3: imageJobs.setBackgroundResource(R.drawable.profil3);
                break;
            case 4: imageJobs.setBackgroundResource(R.drawable.profil4);
                break;
            default: imageJobs.setBackgroundResource(R.drawable.profil5);

        mButtonSzukaj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count++;
                //Toast.makeText(MainActivity.this, "Licznik: " + count, Toast.LENGTH_LONG).show();

                if (TextUtils.isEmpty(mEditTextPraca.getText().toString()) && (TextUtils.isEmpty(mEditTextMiejsce.getText().toString()))) {
                    mEditTextPraca.setError("Pole obowiązkowe!");
                    mEditTextMiejsce.setError("Pole obowiązkowe!");
                    return;
                } else if (TextUtils.isEmpty(mEditTextPraca.getText().toString())) {
                    mEditTextPraca.setError("Pole obowiązkowe!");
                    return;
                } else if (TextUtils.isEmpty(mEditTextMiejsce.getText().toString())) {
                    mEditTextMiejsce.setError("Pole obowiązkowe!");
                    return;
                } else {
                    nazwaStanowiska = mEditTextPraca.getText().toString();
                    nazwaMiejscowosci = mEditTextMiejsce.getText().toString();
                    Intent myIntent = new Intent(MainActivity.this, ListviewActivity.class);
                    MainActivity.this.startActivityForResult(myIntent, 1);

                }
            }
        });
    }}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "onActivityResult", Toast.LENGTH_LONG).show();

        super.onActivityResult(requestCode, resultCode, data);

        subMenu.clear();

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                int click  = 1;
                click++;

                ArrayList<String> passedText = data.getStringArrayListExtra("text");
                ArrayList<String> passedLink = data.getStringArrayListExtra("link");

                arrayFav.addAll(passedText);
                arrayLin.addAll(passedLink);

                for (int i = 0; i < arrayFav.size(); i++) {
                    MenuItem item = subMenu.add(arrayFav.get(i));

                    final int count = i;
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(MainActivity.this, "THIS IS A TEST: " + count, Toast.LENGTH_SHORT).show();

                            /*Intent myBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayLin.get(count)));
                            myBrowserIntent.putExtra("paramPosition", count);
                            startActivity(myBrowserIntent);*/

                            //Toast.makeText(MainActivity.this, arrayLin.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, arrayFav.size(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, arrayLin.size(), Toast.LENGTH_LONG).show();

                            return false;
                        }
                    });

                    item.hasSubMenu();
                }
            }
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

                        it.putExtra("sms_body", arrayFav.toString() + " " + arrayLin.toString());

                        startActivity(it);

                        /*for (int i = 0; i < arrayFav.size(); i++) {
                            it.putExtra("sms_body", i + 1 + ". " + arrayFav.get(i).toString() + ": " + arrayLin.get(i).toString());
                            startActivity(it);
                        }*/


                        break;

                    case 1: //Email
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Oferty pracy");
                        intent.putExtra(Intent.EXTRA_TEXT, arrayFav.toString()+""+arrayLin.toString());
                        startActivity(Intent.createChooser(intent, "Send Email"));
                        break;

                    case 2: //Plik
                        File root = android.os.Environment.getExternalStorageDirectory();

                        File dir = new File(root.getAbsolutePath() + "/download");

                        Toast.makeText(getApplicationContext(), "Eksport do pliku: " + dir.toString(), Toast.LENGTH_LONG).show();

                        dir.mkdirs();
                        File file = new File(dir, "myData.txt");

                        try {
                            FileOutputStream f = new FileOutputStream(file);
                            PrintWriter pw = new PrintWriter(f);

                            for (int i = 0; i < arrayFav.size(); i++) {
                                pw.println(arrayFav.get(i).toString()+""+arrayLin.toString());
                            }
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