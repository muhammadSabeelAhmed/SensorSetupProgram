package com.discoveritech.sensorsetupprogram.GeneralClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.discoveritech.sensorsetupprogram.R;
import com.unnamed.b.atv.model.TreeNode;

public class MyChildHolder extends TreeNode.BaseNodeViewHolder<IconTreeItem> {
    public MyChildHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.child_items, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.child_value);
        TextView tvName = (TextView) view.findViewById(R.id.child_name);
        tvValue.setText(value.value);
        tvName.setText(value.name);

        return view;
    }

}