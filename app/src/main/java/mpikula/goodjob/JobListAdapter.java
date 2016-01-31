package mpikula.goodjob;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpikula.goodjob.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Marcin on 2015-05-10.
 */
public class JobListAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;

    static List<String> mUlubione = new ArrayList<>();

    //layout z elementami wiersza listy
    public static class PracaViewHolder {
        public TextView mNazwaOferty;
        public ImageView mImageAndroKorpo;
    }
    //Konstruktor
    public JobListAdapter(Context mContext, List<String> mDane) {
        //layout z elementami wiersza listy
        super(mContext, R.layout.list_element_job, mDane);
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PracaViewHolder mHolder;
        if(convertView == null) {
            //layout z elementami wiersza listy
            convertView = mInflater.inflate(R.layout.list_element_job, parent, false);
            mHolder = new PracaViewHolder();

            //id wzięte z layoutu pojedynczego wiersza listy
            TextView mNazwaOferty = (TextView) convertView.findViewById(R.id.job_name);
           // ImageView mImageAndroKorpo = (ImageView) convertView.findViewById(R.id.imageViewX);

            mHolder.mNazwaOferty = mNazwaOferty;
            //mHolder.mImageAndroKorpo = mImageAndroKorpo;

            convertView.setTag(mHolder);
        } else {
            mHolder = (PracaViewHolder)convertView.getTag();
        }
        final String mPracaPosition = getItem(position); // Wyszukanie elementu z danymi

        // Ustawienie danych w widoku
        mHolder.mNazwaOferty.setText(mPracaPosition);


        int min = 1;
        int max = 19;  //ma byc 19

        Random r = new Random();
        int randomizer = r.nextInt(max - min + 1) + min;


        switch(randomizer){
            case 1: convertView.setBackgroundResource(R.drawable.job1b);
                break;
            case 2: convertView.setBackgroundResource(R.drawable.job2b);
                break;
            case 3: convertView.setBackgroundResource(R.drawable.job3b);
                break;
            case 4: convertView.setBackgroundResource(R.drawable.job4b);
                break;
            case 5: convertView.setBackgroundResource(R.drawable.job5b);
                break;
            case 6: convertView.setBackgroundResource(R.drawable.job6b);
                break;
            case 7: convertView.setBackgroundResource(R.drawable.job7b);
                break;
            case 8: convertView.setBackgroundResource(R.drawable.job8b);
                break;
            case 9: convertView.setBackgroundResource(R.drawable.job9b);
                break;
            case 10: convertView.setBackgroundResource(R.drawable.job10b);
                break;
            case 11: convertView.setBackgroundResource(R.drawable.job11b);
                break;
            case 12: convertView.setBackgroundResource(R.drawable.job12b);
                break;
            case 13: convertView.setBackgroundResource(R.drawable.job13b);
                break;
            case 14: convertView.setBackgroundResource(R.drawable.job14b);
                break;
            case 15: convertView.setBackgroundResource(R.drawable.job15b);
                break;
            case 16: convertView.setBackgroundResource(R.drawable.job16b);
                break;
            case 17: convertView.setBackgroundResource(R.drawable.job17b);
                break;
            default: convertView.setBackgroundResource(R.drawable.job18b); //ma byc 18
        }
        return convertView;
    }
}
