package com.rajuthapa.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rajuthapa.todoapp.data.Task;
import com.rajuthapa.todoapp.data.TaskViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddUpdateFragment.fragAddUpdateListener {

    private TaskViewModel taskViewModel;
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private AddUpdateFragment fragment;
    private FloatingActionButton buttonAddTask;
    private FrameLayout task_fragment;
    private Menu mn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task_fragment  = (FrameLayout)findViewById(R.id.fragment_container);
        buttonAddTask = findViewById(R.id.fabBtn);
        hideTaskFragment();
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
//                startActivityForResult(intent,ADD_NOTE_REQUEST);
                showTaskFragment();
                fragment = new AddUpdateFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commit();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);


        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                //update recycle view place
                adapter.setTasks(tasks);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Task Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
//                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
//                intent.putExtra(AddEditTaskActivity.EXTRA_ID,task.getId());
//                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE,task.getTitle());
//                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION,task.getDescription());
//                intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY,task.getPriority());
//                startActivityForResult(intent,EDIT_NOTE_REQUEST);
//
                Bundle bundle = new Bundle();
                bundle.putInt("id", task.getId());
                bundle.putString("title", task.getTitle());
                bundle.putString("description", task.getDescription());
                bundle.putInt("priority", task.getPriority());
                showTaskFragment();
                fragment = new AddUpdateFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.mn = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.delete_all_tasks:
                taskViewModel.deleteAllTasks();
                Toast.makeText(this,"All Tasks deleted",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInputSend(int id,String title, String description, int priority) {
        this.setTitle("Todo App");
        Task task = new Task(title,description,priority,new Date());
        if(id != -1){
            task.setId(id);
            taskViewModel.update(task);
            Toast.makeText(this,"Task Updated",Toast.LENGTH_SHORT).show();
        }
        else{
            taskViewModel.insert(task);
            Toast.makeText(this,"Task Saved",Toast.LENGTH_SHORT).show();
        }
        hideTaskFragment();
        mn.findItem(R.id.delete_all_tasks).setVisible(true);
        mn.findItem(R.id.save_task).setVisible(false);
    }
    public void hideTaskFragment(){
        this.setTitle("Todo App");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        task_fragment.setVisibility(View.GONE);
        if(!buttonAddTask.isShown()){
            buttonAddTask.show();
        }
    }
    public void cancelAddUpdate(){
        this.setTitle("Todo App");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        task_fragment.setVisibility(View.GONE);
        task_fragment.setVisibility(View.GONE);
        if(!buttonAddTask.isShown()){
            buttonAddTask.show();
        }
        mn.findItem(R.id.delete_all_tasks).setVisible(true);
        mn.findItem(R.id.save_task).setVisible(false);
    }

    public void showTaskFragment(){
        this.setTitle("Add Task");
        task_fragment.setVisibility(View.VISIBLE);
        buttonAddTask.hide();
    }

}