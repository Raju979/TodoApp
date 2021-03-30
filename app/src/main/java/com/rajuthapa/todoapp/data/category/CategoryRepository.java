package com.rajuthapa.todoapp.data.category;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rajuthapa.todoapp.data.TodoDatabase;
import com.rajuthapa.todoapp.data.task.Task;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    public CategoryRepository(Application application){
        TodoDatabase database = TodoDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategory();
    }
    public void insert(Category category){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.insert(category);
            }
        });
    }
    public void update(Category category){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.update(category);
            }
        });
    }
    public void delete(Category category){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.delete(category);
            }
        });
    }
    public void deleteAllTasks(){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.deleteAllCategories();
            }
        });
    }
    public LiveData<List<Category>> getAllCategories(){
        return allCategories;
    }
}
