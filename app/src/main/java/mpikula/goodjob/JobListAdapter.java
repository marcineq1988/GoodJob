package mpikula.goodjob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpikula.goodjob.R;

import java.util.ArrayList;
import java.util.List;

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
            ImageView mImageAndroKorpo = (ImageView) convertView.findViewById(R.id.imageViewX);

            mHolder.mNazwaOferty = mNazwaOferty;
            mHolder.mImageAndroKorpo = mImageAndroKorpo;

            convertView.setTag(mHolder);
        } else {
            mHolder = (PracaViewHolder)convertView.getTag();
        }
        final String mPracaPosition = getItem(position); // Wyszukanie elementu z danymi

        // Ustawienie danych w widoku
        mHolder.mNazwaOferty.setText(mPracaPosition);


/*        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }*/


        return convertView;
    }
}
