package com.sij.todoapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sij.todoapp.database.Repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private Repository mTodoRepository;
    private LiveData<List<ETodo>> mAllTodos;

    public ViewModel(@NonNull Application application) {
        super(application);

        mTodoRepository = new Repository(application);
        mAllTodos=mTodoRepository.getmAllTodoList();
    }
    public void insert(ETodo todo){
        mTodoRepository.insert(todo);
    }
    public void update(ETodo todo){
        mTodoRepository.update(todo);
    }
    public LiveData<List<ETodo>> getAllTodos() {
        return mAllTodos;
    }
    public ETodo getTodoById(int id){
        return mTodoRepository.getTodoById(id);
    }

    public void deleteById(ETodo todo){
        mTodoRepository.deleteById(todo);
    }
    public void deleteAll(){
        mTodoRepository.deleteAll();
    }
    public void deleteCompleted(){
        mTodoRepository.deleteCompleted();
    }
}
