package com.rajuthapa.todoapp.data.task;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rajuthapa.todoapp.data.category.Category;
import com.rajuthapa.todoapp.data.category.CategoryDao;

import java.util.Date;

@Entity(tableName = "tasks",foreignKeys = @ForeignKey(
        entity = Category.class,
        parentColumns = "id",
        childColumns = "cat_id",
        onDelete = ForeignKey.CASCADE),indices = {@Index("cat_id")})

public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;

    private String description;

    private int priority;

    private int cat_id;

    @ColumnInfo(name = "created_date")
    private Date createdDate;

    public Task(String title, String description, int priority, Date createdDate, int cat_id) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.createdDate = createdDate;
        this.cat_id = cat_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    public int getCat_id() {
        return cat_id;
    }
}
