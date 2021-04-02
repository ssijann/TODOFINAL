package com.sij.todoapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sij.todoapp.model.ETodo;
import com.sij.todoapp.model.ViewModel;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditFragment extends Fragment {

    View rootView;
    EditText txtTitle, txtDescription, txtDate;
    RadioGroup rgPriority;
    RadioButton rbHigh, rbMedium, rbLow, rbSelected;
    CheckBox chkIsCompleted;
    Button btnSave;
    AlertDialog.Builder mAlterDialog;
    DatePickerDialog mDatePicker;

    public static final int HIGH_PRIORITY=1;
    public static final int MEDIUM_PRIORITY=2;
    public static final int LOW_PRIORITY=3;

    private ViewModel mTodoViewModel;

    private int todoId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.activity_edit_fragment, container, false);
        mTodoViewModel= ViewModelProviders.of(this).get(ViewModel.class);
        txtTitle=rootView.findViewById(R.id.edit_txt_title);
        txtDescription=rootView.findViewById(R.id.edit_txt_description);
        txtDate=rootView.findViewById(R.id.edit_txt_date);
        rgPriority=rootView.findViewById(R.id.edit_rg_priority);
        rbHigh=rootView.findViewById(R.id.edit_rb_high);
        rbMedium=rootView.findViewById(R.id.edit_rb_medium);
        rbLow=rootView.findViewById(R.id.edit_rb_low);
        chkIsCompleted=rootView.findViewById(R.id.edit_chk_iscomplete);
        btnSave=rootView.findViewById(R.id.edit_btn_save);
        txtDate=rootView.findViewById(R.id.edit_txt_date);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTodo();
            }
        });

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    DispalyTodoDate();
                }

                return false;
            }
        });
        todoId=getActivity().getIntent().getIntExtra("TodoId",-1);
        if (todoId!=-1){
            btnSave.setText(getText(R.string.edit_update));
            ETodo todo = mTodoViewModel.getTodoById(todoId);
            txtTitle.setText(todo.getTitle());
            txtDescription.setText(todo.getDescription());
            DateFormat formatter;
            formatter=new SimpleDateFormat("YYYY-MM-DD");
            txtDate.setText(formatter.format(todo.getTodo_date()));
            switch(todo.getPriority()){
                case 1:
                    rgPriority.check(R.id.edit_rb_high);
                    break;
                case 2:
                    rgPriority.check(R.id.edit_rb_medium);
                    break;
                case 3:
                    rgPriority.check(R.id.edit_rb_low);
            }
            chkIsCompleted.setSelected(todo.isIs_completed());
        }
        return rootView;

    }
    void DisplayAlerDialog(){
        mAlterDialog = new AlertDialog.Builder(getContext());
        mAlterDialog.setMessage(getString(R.string.edit_cancel_promt))
                .setCancelable(false)
                .setTitle(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher);
        mAlterDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAlterDialog.show();
    }
    void DispalyTodoDate(){
        Calendar calendar = Calendar.getInstance();
        int cDay=calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth=calendar.get(Calendar.MONTH);
        int cYear=calendar.get(Calendar.YEAR);

        mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDate.setText(year+"-"+month+"-"+dayOfMonth);
            }
        },cYear, cMonth, cDay);
        mDatePicker.show();
    }
    void SaveTodo(){
        boolean validate = true;
        ETodo todo= new ETodo();
        Date todoDate;
        int priority=1;
        int checkedPriority=-1;
        if (txtTitle.getText().toString().trim().equals("")){
            txtTitle.setError("Title cannot be empty");
            txtTitle.requestFocus();
            validate = false;
        }
        if (txtDescription.getText().toString().trim().equals("")){
            txtDescription.setError("Description cannot be empty");
            txtTitle.requestFocus();
            validate = false;
        }
        if (txtDate.getText().toString().trim().equals("")){
            txtDate.setError("Date cannot be empty");
            txtTitle.requestFocus();
            validate = false;
        }

        todo.setTitle(txtTitle.getText().toString());
        todo.setDescription(txtDescription.getText().toString());
        try {
            DateFormat formatter;
            formatter=new SimpleDateFormat("YYYY-MM-DD");
            todoDate=(Date)formatter.parse(txtDate.getText().toString());
            todo.setTodo_date(todoDate);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        checkedPriority=rgPriority.getCheckedRadioButtonId();
        switch (checkedPriority){
            case R.id.edit_rb_high:
                priority = HIGH_PRIORITY;
                break;
            case R.id.edit_rb_medium:
                priority=MEDIUM_PRIORITY;
                break;
            case R.id.edit_rb_low:
                priority=LOW_PRIORITY;
                break;
        }
        todo.setPriority(priority);
        todo.setIs_completed(chkIsCompleted.isChecked());
        if (validate){
            if (todoId!=-1){
                todo.setId(todoId);
                mTodoViewModel.update(todo);
                Toast.makeText(getActivity(),getText(R.string.crud_update),Toast.LENGTH_SHORT).show();
            }else{

                mTodoViewModel.insert(todo);
                Toast.makeText(getActivity(),getText(R.string.crud_save),Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }
    }
}
