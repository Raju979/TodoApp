package com.rajuthapa.todoapp.data.task;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        TodoDatabase database = TodoDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }
    public void insert(Task task){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insert(task);
            }
        });
    }
    public void update(Task task){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.update(task);
            }
        });
    }
    public void delete(Task task){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.delete(task);
            }
        });
    }
    public void deleteAllTasks(){
        TodoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.deleteAllTasks();
            }
        });
    }
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }
}
