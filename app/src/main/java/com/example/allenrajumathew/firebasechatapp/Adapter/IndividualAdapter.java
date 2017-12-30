package com.example.allenrajumathew.firebasechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Model.IndividualListItem;
import com.example.allenrajumathew.firebasechatapp.R;
import com.example.allenrajumathew.firebasechatapp.UI.BookmarkActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class IndividualAdapter extends RecyclerView.Adapter <IndividualAdapter.IndividualHolder>{

    private List<IndividualListItem> individualListData;
    private LayoutInflater inflater;
    Context context ;

    public IndividualAdapter(List<IndividualListItem> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.individualListData = listData;
        context=c;
    }

    @Override
    public IndividualHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_recording_list,parent,false);
        return new IndividualHolder(view);
    }

    @Override
    public void onBindViewHolder(IndividualHolder holder, int position) {

        IndividualListItem item = individualListData.get(position);

        String filePath = item.getRecordingTitle();

        //**To Change holder.recordingTitle.item.getTitle();

        String a = filePath.replace(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BookMark_AudioFiles/", "");
        String fileName = a.replace(".3gp", "");
        holder.recordingTitle.setText(fileName );

//        int recDurationInMillSec = Integer.parseInt(item.getRecordingTimeSubTitle());
//
//        String recDuration = String.format("%02d:%02d",
//                TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(item.getRecordingTimeSubTitle())),
//                TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(item.getRecordingTimeSubTitle())) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(item.getRecordingTimeSubTitle())))
//        );


        //**To Change by using Class GrtTimeAgo
        holder.recordingDateSubTitle.setText(item.getRecordingDateSubTitle() +" | "+
                String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(item.getRecordingTimeSubTitle())),
                        TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(item.getRecordingTimeSubTitle())) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(item.getRecordingTimeSubTitle())))
                ));

    }

    @Override
    public int getItemCount() {
        return individualListData.size();
    }

    class IndividualHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView recordingTitle;
        private TextView recordingDateSubTitle;

        public IndividualHolder(View itemView) {
            super(itemView);

            recordingTitle = (TextView) itemView.findViewById(R.id.lbl_rec_title_text);
            recordingDateSubTitle = (TextView) itemView.findViewById(R.id.lbl_rec__sub_title_text);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            IndividualListItem item = individualListData.get(getAdapterPosition());

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(context, BookmarkActivity.class);
            intent.putExtra("ID",item.getRecordingID());
            intent.putExtra("Audio",item.getRecordingTitle());
            context.startActivity(intent);

        }
    }
}
