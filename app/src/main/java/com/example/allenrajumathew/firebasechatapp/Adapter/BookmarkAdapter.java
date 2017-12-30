package com.example.allenrajumathew.firebasechatapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allenrajumathew.firebasechatapp.R;
import com.example.allenrajumathew.firebasechatapp.Model.BookmarkListItem;
import com.example.allenrajumathew.firebasechatapp.UI.BookmarkActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {

    private List<BookmarkListItem> bookmarkListData;
    private LayoutInflater inflater;

    public BookmarkAdapter(List<BookmarkListItem> bookmarklistData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.bookmarkListData = bookmarklistData;
    }

    @Override
    public BookmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_bookmark_list, parent, false);
        return new BookmarkHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkHolder holder, int position) {

        BookmarkListItem item = bookmarkListData.get(position);

        int recDurationInMillSec = Integer.parseInt(item.getBookmarkTimeSubTitle());

        String recDuration = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(recDurationInMillSec),
                TimeUnit.MILLISECONDS.toSeconds(recDurationInMillSec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(recDurationInMillSec))
        );

//        Log.d("Allen", "onBindViewHolder: " + item.getBookmarkTimeSubTitle());
//        Log.d("Allen", "onBindViewHolder: " + item.getBookmarkNotes());

        holder.bookmarkTimeSubTitle.setText(recDuration);
        holder.bookmarkNotes.setText(item.getBookmarkNotes());
    }

    @Override
    public int getItemCount() {
        return bookmarkListData.size();
    }

    public class BookmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView bookmarkTimeSubTitle;
        private TextView bookmarkNotes;

        public BookmarkHolder(View itemView) {
            super(itemView);

            bookmarkTimeSubTitle = (TextView) itemView.findViewById(R.id.bookmark_sub_title_lbl);

            bookmarkNotes = (TextView) itemView.findViewById(R.id.bookmark_note_lbl);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            BookmarkListItem bookmarkItem = bookmarkListData.get(getAdapterPosition());

            Toast.makeText(v.getContext(), bookmarkTimeSubTitle.getText(), Toast.LENGTH_SHORT).show();

            BookmarkActivity.soundPlayer.seekTo(Integer.parseInt(bookmarkItem.getBookmarkTimeSubTitle()));

        }
    }
}
