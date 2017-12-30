package com.example.allenrajumathew.firebasechatapp.UI;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.allenrajumathew.firebasechatapp.Adapter.IndividualAdapter;
import com.example.allenrajumathew.firebasechatapp.Model.IndividualData;
import com.example.allenrajumathew.firebasechatapp.R;

import java.util.ArrayList;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */


//**To Chage remove listData, it is not used
public class IndividualActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView recView;
    private IndividualAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.recording_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Recordings List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recView = (RecyclerView) findViewById(R.id.rec_recording_list);

        //****
        //LayoutManager: LinearLayoutManager or GridLayoutManage or StaggeredGridLayoutManage

        recView.setLayoutManager(new LinearLayoutManager(this));

        //instance

        adapter = new IndividualAdapter(IndividualData.getListData(this), this);

        recView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recView.setAdapter(adapter);



    }
}
