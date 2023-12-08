package domain.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.music.MainActivity;
import com.example.music.R;

import java.io.File;
import java.util.List;

public class TrackAdapter extends BaseAdapter {
    private Context context;
    private List<Result> items;
    public ViewHolder viewHolder = null;

    public TrackAdapter(Context context, List<Result> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final int ROW_RESOURCE = R.layout.layout_list_view_row_item;
        //ViewHolder viewHolder = null;

        if (convertView == null){
            LayoutInflater layout = LayoutInflater.from(context);
            convertView = layout.inflate(ROW_RESOURCE, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Result result = items.get(pos);
        String fullFilename = context.getCacheDir() + "/" + result.getLocalArtworkFilename();

        if(new File(fullFilename).exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(fullFilename);
            viewHolder.imgArtwork.setImageBitmap(bitmap);
        }else{
            viewHolder.imgArtwork.setImageBitmap(null);
        }

        viewHolder.labTrackName.setText(result.getTrackName());
        viewHolder.labArtistName.setText(result.getArtistName());

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imgArtwork;
        public TextView labTrackName;
        public TextView labArtistName;
        public ImageButton button;

        public ViewHolder(View view){
            imgArtwork = (ImageView) view.findViewById(R.id.imgArtwork);
            labTrackName = (TextView) view.findViewById(R.id.labTrackName);
            labArtistName = (TextView) view.findViewById(R.id.labArtistName);
            button = (ImageButton) view.findViewById(R.id.button);
        }
    }


}
