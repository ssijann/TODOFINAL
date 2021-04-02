package com.sij.todoapp.database;

import androidx.lifecycle.LiveData;

import com.sij.todoapp.model.ETodo;

import java.util.List;

import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.OnConflictStrategy;
        import androidx.room.Query;
        import androidx.room.Update;

@Dao
public interface TodoDAO {
    @Insert
    void insert(ETodo todo);
    @Delete
    void deleteById(ETodo todo);
    @Query("DELETE FROM todo_table")
    void deleteAll();
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ETodo... todos);

    @Query("SELECT * FROM todo_table ORDER BY todo_date, priority desc")
    LiveData<List<ETodo>> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE id=:id")
    ETodo getTodoById(int id);

    @Query("Delete From todo_table where is_completed=1")
    void deleteCompleted();

}
