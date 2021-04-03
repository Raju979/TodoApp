package com.rajuthapa.todoapp.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rajuthapa.todoapp.data.category.Category;
import com.rajuthapa.todoapp.data.category.CategoryDao;
import com.rajuthapa.todoapp.data.task.Task;
import com.rajuthapa.todoapp.data.task.TaskDao;
import com.rajuthapa.todoapp.utilities.DateConverter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Category.class},version = 5,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase instance;

    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized TodoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TodoDatabase.class,"todo_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private TaskDao taskDao;
        private PopulateDbAsyncTask(TodoDatabase db){
            taskDao = db.taskDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
//            taskDao.insert(new Task(
//                    "Title 1","Description 1",1,new Date()
//            ));
//            taskDao.insert(new Task(
//                    "Title 2","Description 2",2,new Date()
//            ));
            return null;
        }
    }
}
