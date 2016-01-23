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
        int max = 7;

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
            default: convertView.setBackgroundResource(R.drawable.job6b);
        }
        return convertView;
    }
}
