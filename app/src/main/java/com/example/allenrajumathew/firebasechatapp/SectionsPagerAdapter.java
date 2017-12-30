package com.example.allenrajumathew.firebasechatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.allenrajumathew.firebasechatapp.Fragments.ChatsFragment;
import com.example.allenrajumathew.firebasechatapp.Fragments.FriendsFragment;
import com.example.allenrajumathew.firebasechatapp.Fragments.RequestsFragment;

/**
 * Created by Allen Raju Mathew on 8/8/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        //int position ==> Returns position of each tab

        switch(position){
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

            case 1:
                ChatsFragment chatsFragment= new ChatsFragment();
                return chatsFragment;

            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        //Total Number Of Tabs ==> Requests,Chats,Friends
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "UPLOAD";
            case 1:
                return "START";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
