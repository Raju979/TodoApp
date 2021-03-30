package com.rajuthapa.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class  AddUpdateFragment extends Fragment{
    private EditText editTextTitle;
    private  EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private Button cancel;
    private fragAddUpdateListener listener;

    public interface fragAddUpdateListener{
        void onInputSend(String title, String description, int priority);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_update,container,false);
        editTextTitle = v.findViewById(R.id.edit_text_title);
        editTextDescription = v.findViewById(R.id.edit_text_description);
        numberPickerPriority = v.findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMaxValue(10);
        numberPickerPriority.setMinValue(1);

        cancel = v.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideTaskFragment();
            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof fragAddUpdateListener){
            listener = (fragAddUpdateListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
            +" must implement fragment listener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.delete_all_tasks).setVisible(false);
        inflater.inflate(R.menu.add_task_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveTask(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please fill title and description",Toast.LENGTH_SHORT).show();
            return;
        }
        listener.onInputSend(title,description,priority);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}