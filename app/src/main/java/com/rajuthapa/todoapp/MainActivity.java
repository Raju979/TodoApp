package com.rajuthapa.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rajuthapa.todoapp.data.category.Category;
import com.rajuthapa.todoapp.data.category.CategoryViewModel;
import com.rajuthapa.todoapp.data.task.Task;
import com.rajuthapa.todoapp.data.task.TaskViewModel;
import com.rajuthapa.todoapp.ui.category.AddUpdateCatFragment;
import com.rajuthapa.todoapp.ui.category.CategoryAdapter;
import com.rajuthapa.todoapp.ui.task.AddUpdateTaskFragment;
import com.rajuthapa.todoapp.ui.task.TaskAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddUpdateTaskFragment.fragTaskAddUpdateListener, AddUpdateCatFragment.fragCatAddUpdateListener {

    private TaskViewModel taskViewModel;
    private CategoryViewModel categoryViewModel;
    private FloatingActionButton buttonAddTask;
    private Button addCategoryButton;
    private Menu mn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTaskFragment(null);
            }
        });
        addCategoryButton = findViewById(R.id.button_add_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCategoryFragment(null);
            }
        });
        manageCategoryView();
        manageTaskView();
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

    public void displayCategoryFragment(Bundle bundle) {
        buttonAddTask.hide();
        addCategoryButton.setVisibility(View.GONE);
        AddUpdateCatFragment taskAddUpdateCatFragment = AddUpdateCatFragment.newInstance();
        if(bundle != null){
            setTitle("Edit Category");
            taskAddUpdateCatFragment.setArguments(bundle);
        }
        else{
            setTitle("Add Category");
        }
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment_container_category, taskAddUpdateCatFragment).addToBackStack(null).commit();
    }

    public void displayTaskFragment(Bundle bundle) {
        buttonAddTask.hide();
        addCategoryButton.setVisibility(View.GONE);
        AddUpdateTaskFragment taskAddUpdateTaskFragment = AddUpdateTaskFragment.newInstance();
        if(bundle != null){
            setTitle("Edit Tasks");
            taskAddUpdateTaskFragment.setArguments(bundle);
        }
        else{
            setTitle("Add Tasks");
        }
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment_container_task, taskAddUpdateTaskFragment).addToBackStack(null).commit();
    }
    public void closeCategoryFragment() {
        //close keyboard if opened
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddUpdateCatFragment taskAddUpdateCategoryFragment = (AddUpdateCatFragment) fragmentManager
                .findFragmentById(R.id.fragment_container_category);
        if (taskAddUpdateCategoryFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(taskAddUpdateCategoryFragment).commit();
        }
        //change menu icons and title
        setTitle("Todo App | What For Today?");
        mn.findItem(R.id.delete_all_tasks).setVisible(true);
        mn.findItem(R.id.save_task).setVisible(false);
        addCategoryButton.setVisibility(View.VISIBLE);
        //show floating action button
        buttonAddTask.show();
    }
    public void closeTaskFragment() {
        //close keyboard if opened
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddUpdateTaskFragment taskAddUpdateTaskFragment = (AddUpdateTaskFragment) fragmentManager
                .findFragmentById(R.id.fragment_container_task);
        if (taskAddUpdateTaskFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(taskAddUpdateTaskFragment).commit();
        }
        //change menu icons and title
        setTitle("Todo App | What For Today?");
        mn.findItem(R.id.delete_all_tasks).setVisible(true);
        mn.findItem(R.id.save_task).setVisible(false);
        addCategoryButton.setVisibility(View.VISIBLE);
        //show floating action button
        buttonAddTask.show();
    }

    @Override
    public void onInputSend(int id,String title, String description, int priority) {
        this.setTitle("Todo App | What For Today?");

        Task task = new Task(title,description,priority,new Date(),1);
        if(id != -1){
            task.setId(id);
            taskViewModel.update(task);
            Toast.makeText(this,"Task Updated",Toast.LENGTH_SHORT).show();
        }
        else{
            taskViewModel.insert(task);
            Toast.makeText(this,"Task Saved",Toast.LENGTH_SHORT).show();
        }
        closeTaskFragment();
    }

    public void manageTaskView(){
        RecyclerView recyclerViewTask = findViewById(R.id.recycler_view_task);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTask.setHasFixedSize(true);
        final TaskAdapter taskAdapter = new TaskAdapter();
        recyclerViewTask.setAdapter(taskAdapter);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                //update recycle view place
                taskAdapter.setTasks(tasks);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(taskAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Task Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewTask);
        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", task.getId());
                bundle.putString("title", task.getTitle());
                bundle.putString("description", task.getDescription());
                bundle.putInt("priority", task.getPriority());
                displayTaskFragment(bundle);
            }
        });
    }

    public void manageCategoryView(){
        RecyclerView recyclerViewCategory = findViewById(R.id.recycler_view_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setHasFixedSize(true);
        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        recyclerViewCategory.setAdapter(categoryAdapter);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                //update recycle view place
                categoryAdapter.setCategories(categories);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                categoryViewModel.delete(categoryAdapter.getCategoryAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Category Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewCategory);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Category category) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", category.getId());
                bundle.putString("title", category.getCat_name());
                bundle.putString("description", category.getCat_des());
                displayCategoryFragment(bundle);
            }
        });
    }

    @Override
    public void onInputCategorySend(int id, String title, String description) {
        this.setTitle("Todo App | What For Today?");
        Category category = new Category(title,description);
        if(id != -1){
            category.setId(id);
            categoryViewModel.update(category);
            Toast.makeText(this,"Category Updated",Toast.LENGTH_SHORT).show();
        }
        else{
            categoryViewModel.insert(category);
            Toast.makeText(this,"Category Saved",Toast.LENGTH_SHORT).show();
        }
        closeCategoryFragment();
    }
}