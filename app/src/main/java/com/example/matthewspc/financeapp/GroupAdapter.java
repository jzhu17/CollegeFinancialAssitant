package com.example.matthewspc.financeapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Christine on 12/3/2017.
 */

public class GroupAdapter extends ArrayAdapter<GroupObject> {
    private Activity context;
    private List<GroupObject> groupList;

    public GroupAdapter(Activity context, List<GroupObject> groupList) {
        super(context, R.layout.grouplist_layout, groupList);
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.grouplist_layout, null ,true);
        TextView textViewGroupName = (TextView) listViewItem.findViewById(R.id.textViewGroupName);
        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.textViewCost);

        GroupObject groupObject = groupList.get(position);
        textViewGroupName.setText(groupObject.getGroupName());
        textViewCost.setText(groupObject.getGroupGoal());
        return listViewItem;
    }

}