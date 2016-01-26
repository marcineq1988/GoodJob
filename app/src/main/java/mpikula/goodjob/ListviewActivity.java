package mpikula.goodjob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mpikula.goodjob.R;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.FanEffect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListviewActivity extends ActionBarActivity {

    static final String ZAPISANE = "zapisane";

    int global_position =0;
    boolean longClick = false;
    static String wybranaOferta = "";
    ArrayList<String> choosedOffer = new ArrayList<String>();
    ArrayList<String> choosedLink = new ArrayList<String>();
    MainActivity mainActiv;
    static List<String> mLista = new ArrayList<>();
    static ArrayList<String> positionArr = new ArrayList<String>();
    static List<String> mListaTest1 = new ArrayList<>();
    static List<String> mListaTest2 = new ArrayList<>();
    static List<String> mListaLinki = new ArrayList<>();

    //Listy zapisujące i przekazujące dane do aktywności ze szczegółami danej oferty
    static List<String> mListaNazwy = new ArrayList<>();
    static List<String> mListaFirmy = new ArrayList<>();

    private JobListAdapter mAdapter;
    public Elements jobName, jobName2, jobNameComp, jobName2Comp;
    private ProgressBar mProgress;
    private Context context;

    public ArrayList<String> workList = new ArrayList<String>();
    public ArrayList<String> companyList = new ArrayList<String>();
    public ArrayList<String> jobList = new ArrayList<String>();

    private TextView mSingleJobName;

    public ImageView mImageView;

    private ArrayAdapter<String> adapter;
    private JazzyListView mListView;
    //public String doURLpraca = MainActivity.nazwaStanowiska;
    //public String doURLmiejsce = MainActivity.nazwaMiejscowosci;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);


        mListView = (JazzyListView) findViewById(R.id.list);
        mListView.setTransitionEffect(new FanEffect());
        mListView.setItemsCanFocus(true);
        //Progress bar
        mListView.setEmptyView(findViewById(R.id.progressBarLoading));

        Toast.makeText(getApplicationContext(), "Wyszukiwanie ofert...", Toast.LENGTH_LONG).show();

        new NewThread().execute();
        mAdapter = new JobListAdapter(this, jobList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mListaLinki.get(position)));
                myBrowserIntent.putExtra("paramPosition", position);
                startActivity(myBrowserIntent);
            }
        });

        mSingleJobName = (TextView)findViewById(R.id.job_name);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    choosedOffer.add(mListaTest1.get(position).toString());
                    choosedLink.add(mListaLinki.get(position).toString());
                    Toast.makeText(getApplicationContext(), "Dodano do ulubionych!", Toast.LENGTH_SHORT).show();
                    return true;
                }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(ListviewActivity.this, MainActivity.class);
        intent.putStringArrayListExtra("text", choosedOffer);
        intent.putStringArrayListExtra("link", choosedLink);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
            String doURLwork = mainActiv.nazwaStanowiska;
            String doURLplace = mainActiv.nazwaMiejscowosci;


            Document doc, doc2;
            Elements classs, lins;
            String uerele;
            try {
                doc = (Document) Jsoup.connect("http://www.infopraca.pl/praca?q=" + doURLwork + "&lc=" + doURLplace)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();
                doc2 = (Document) Jsoup.connect("http://www.pracuj.pl/praca/" + doURLwork + ";kw/" + doURLplace + ";wp")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();

                //Oferty
                jobName = doc.select("h2.p-job-title a[href]"); //Infopraca
                jobName2 = doc2.select("h2.offer__list_item_link a[href]");  //pracuj.pl

                //Firmy
                jobNameComp = doc.select("h3.p-name.company a[href]"); //Infopraca
                jobName2Comp = doc2.select("h3.offer__list_item_link a[href]");  //pracuj.pl

                //Oferty pracy
                //Infopraca
                mListaTest1.clear();
                for (Element jobNames : jobName) {
                    mListaTest1.add(jobNames.text() + "\n");
                }

                //Pracuj.pl
                for (Element jobNames2 : jobName2) {
                    mListaTest1.add(jobNames2.text() + "\n");
                }
                if(mListaTest1.size()==0){
                    Toast.makeText(getApplicationContext(), "Zmień parametry wyszukiwania!", Toast.LENGTH_LONG).show();
                }

                //Firmy
                //Infopraca
                mListaTest2.clear();
                for (Element jobNames : jobNameComp) {
                    mListaTest2.add(jobNames.text() + "\n");
                }

                //Pracuj.pl
                for (Element jobNames2 : jobName2Comp) {
                    mListaTest2.add(jobNames2.text() + "\n");
                }

                //Linki do ofert
                //Infopraca
                for (Element link : jobName) {
                    mListaLinki.add(link.attr("abs:href"));
                }

                //Pracuj.pl
                for (Element link : jobName2) {
                    mListaLinki.add(link.attr("abs:href"));
                }

                //Łączenie wyników - oferta + nazwa firmy
                jobList.clear();
                for(int i=0; i<mListaTest1.size(); i++){
                    jobList.add(mListaTest1.get(i)+"\n"+mListaTest2.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute(String result) {
        }

        public void onBackPressed() {
            Intent intent = new Intent(ListviewActivity.this, MainActivity.class);
            intent.putStringArrayListExtra("text", choosedOffer);
            intent.putStringArrayListExtra("link", choosedLink);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        protected void onPostExecute(String result) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
