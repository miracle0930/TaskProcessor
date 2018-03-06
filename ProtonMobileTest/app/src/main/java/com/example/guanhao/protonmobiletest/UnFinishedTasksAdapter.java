package com.example.guanhao.protonmobiletest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guanhao on 2018/2/7.
 */

public class UnFinishedTasksAdapter extends BaseAdapter {

    private List<Task> taskList;
    private LayoutInflater taskInflater;
    private Context context;


    public UnFinishedTasksAdapter(Context context, List<Task> taskList, LayoutInflater taskInflater) {
        this.context = context;
        this.taskList = taskList;
        this.taskInflater = taskInflater;

    }


    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return taskList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"})
        final View taskView = taskInflater.inflate(R.layout.activity_task_cell, null);
        final Task taskCell = taskList.get(position);
        TextView taskCellName = taskView.findViewById(R.id.taskCellName);
        final TextView taskCellStatus = taskView.findViewById(R.id.taskCellStatus);
        final Button taskCellButton = taskView.findViewById(R.id.taskCellCancel);
        taskCellName.setText(taskCell.getName());
        taskCellStatus.setText(taskCell.getStatus());
        taskCellButton.setText(taskCell.getButtonText());
        taskCellButton.setBackgroundColor(taskCell.getButtonColor());
        taskCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskCellButton.getText().toString().equals("Cancel")) {
                    taskCell.setStatus("Canceled");
                    taskCell.setButtonColor(Color.GREEN);
                    taskCell.setButtonText("Restart");
                    taskCell.getTimer().cancel();
                    taskCellStatus.setText(taskCell.getStatus());
                    taskCellButton.setText(taskCell.getButtonText());
                    taskCellButton.setBackgroundColor(taskCell.getButtonColor());
                } else {
                    taskCell.setStatus("Ready");
                    taskCell.setButtonColor(Color.RED);
                    taskCell.setButtonText("Cancel");
                    taskCellStatus.setText(taskCell.getStatus());
                    taskCellButton.setText(taskCell.getButtonText());
                    taskCellButton.setBackgroundColor(taskCell.getButtonColor());
                }
                taskView.refreshDrawableState();
            }
        });
        return taskView;
    }
}
