package mpikula.goodjob;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mpikula.goodjob.R;
import com.twotoasters.jazzylistview.effects.CardsEffect;
import com.twotoasters.jazzylistview.effects.CurlEffect;
import com.twotoasters.jazzylistview.effects.FadeEffect;
import com.twotoasters.jazzylistview.effects.FanEffect;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.twotoasters.jazzylistview.effects.ReverseFlyEffect;
import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.effects.TiltEffect;
import com.twotoasters.jazzylistview.effects.TwirlEffect;
import com.twotoasters.jazzylistview.effects.WaveEffect;
import com.twotoasters.jazzylistview.effects.ZipperEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnLongClickListener {

    private Button mButtonSzukaj;
    private EditText mEditTextPraca;
    private EditText mEditTextMiejsce;
    private ImageView imageJobs;
    private RoundedImageView circleImageBackground;
    public static String nazwaStanowiska;
    public static String nazwaMiejscowosci;
    public static int animationNumber = 1;
    public static int animationNumberToPass = 1;
    ArrayList<String> arrayFav = new ArrayList<String>();
    ArrayList<String> arrayLin = new ArrayList<String>();
    private ListView mDrawerList;
    public ArrayAdapter<String> mAdapter;
    //private Settings mSettings;


    Menu subMenu;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mEditTextPraca = (EditText)findViewById(R.id.editTextPraca);
        //mEditTextPraca.setText("junior developer");

        mEditTextMiejsce = (EditText)findViewById(R.id.editTextMiejsce);
        //mEditTextMiejsce.setText("Wrocław");

        mButtonSzukaj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count++;
                //Toast.makeText(MainActivity.this, "Licznik: " + count, Toast.LENGTH_LONG).show();

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
                }
                else{
                    Toast.makeText(MainActivity.this, "Brak połączenia z siecią!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                            Toast.makeText(MainActivity.this, "Otwieranie oferty... " + count, Toast.LENGTH_SHORT).show();
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
                        intent.putExtra(Intent.EXTRA_TEXT, arrayFav.toString() + "" + arrayLin.toString());
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
                                pw.println(arrayFav.get(i).toString() + "" + arrayLin.toString());
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