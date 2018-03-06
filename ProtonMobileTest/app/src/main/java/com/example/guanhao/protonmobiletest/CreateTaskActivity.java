package com.example.guanhao.protonmobiletest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private ListView keywordsListView;
    private final List<String> keywordsList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Set<String> keywordsSet;
    private String oldName = "";
    private Task passTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setTitle("Create New Task");
        taskNameEditText = findViewById(R.id.taskNameEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        keywordsListView = findViewById(R.id.keywordListView);
        keywordsSet = new HashSet<>();
        configureKeywordsListView(keywordsListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keywordsList);
        keywordsListView.setAdapter(adapter);
        passDataIfExisted();
    }

    public void passDataIfExisted() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("task")) {
            setTitle("Modify Task");
            passTask = (Task) bundle.getSerializable("task");
            taskNameEditText.setText(passTask.getName());
            oldName = passTask.getName();
            taskDescriptionEditText.setText(passTask.getDescription());
            keywordsList.addAll(passTask.getKeywordsList());
            keywordsSet.addAll(passTask.getKeywordsSet());
            adapter.notifyDataSetChanged();
        }
    }

    public void configureKeywordsListView(ListView listView) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String keyword = keywordsList.get(position);
                keywordsSet.remove(keyword);
                keywordsList.remove(keyword);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String keyword = keywordsList.get(position);
                final EditText keywordUpdate = new EditText(CreateTaskActivity.this);
                final int index = position;
                keywordUpdate.setText(keyword);
                keywordUpdate.setInputType(InputType.TYPE_CLASS_TEXT);
                new AlertDialog.Builder(CreateTaskActivity.this)
                        .setTitle("Update Keyword")
                        .setView(keywordUpdate)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newKeyword = keywordUpdate.getText().toString();
                                if (!newKeyword.equals(keyword)) {
                                    keywordsSet.remove(keyword);
                                    keywordsSet.add(newKeyword);
                                    keywordsList.set(index, newKeyword);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Discard", null).show();
            }
        });
    }

    public void uploadFile(View view) {
        System.out.println("start uploading");
    }

    public void startTask(View view) {

        String taskName = taskNameEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();
        Task task;
        if (passTask != null) {
            task = passTask;
            task.setName(taskName);
        } else {
            task = new Task(taskName);
        }
        task.setDescription(taskDescription);
        task.setKeywordsList(keywordsList);
        task.setKeywordsSet(keywordsSet);
        Intent intent = new Intent(CreateTaskActivity.this, MainTasksActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("name", oldName == null ? taskName : oldName);
        if (getTitle().toString().equals("Create New Task")) {
            intent.putExtra("status", "new");
        } else {
            intent.putExtra("status", "old");
        }
        if ((Util.UNFINISHED_SET.contains(taskName) || Util.FINISHED_SET.contains(taskName)) && !oldName.equals(taskName) ) {
            Toast.makeText(getApplicationContext(), "Task name has been used!", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("Task Status: " + task.getStatus());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void addKeyword(View view) {
        System.out.println("new keyword added");
        final EditText keywordInput = new EditText(this);
        keywordInput.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("New Keyword")
                .setView(keywordInput)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String keyword = keywordInput.getText().toString().trim();
                        if (keyword.length() != 0 && !keywordsSet.contains(keyword)) {
                            keywordsList.add(keyword);
                            keywordsSet.add(keyword);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Discard", null).show();
    }

    public void cancelTask(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure to Cancel?")
                .setMessage("You will lose all stuff on this page.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateTaskActivity.this, MainTasksActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null).show();
    }
}
