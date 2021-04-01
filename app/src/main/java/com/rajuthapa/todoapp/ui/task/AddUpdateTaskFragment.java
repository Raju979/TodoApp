package com.rajuthapa.todoapp.ui.task;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rajuthapa.todoapp.MainActivity;
import com.rajuthapa.todoapp.R;

public class AddUpdateTaskFragment extends Fragment{
    private EditText editTextTitle;
    private  EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private RadioGroup radioPriorityGroup;
    private int priority = 1;
    private Button cancel;
    private fragTaskAddUpdateListener listener;

    public AddUpdateTaskFragment(){

    }
    public interface fragTaskAddUpdateListener{
        void onInputSend(int id, String title, String description, int priority);
    }
    public static AddUpdateTaskFragment newInstance() {
        return new AddUpdateTaskFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_update_task,container,false);
        editTextTitle = v.findViewById(R.id.edit_text_title_task);
        editTextDescription = v.findViewById(R.id.edit_text_description_task);
        radioPriorityGroup = v.findViewById(R.id.radio_priority);
//        numberPickerPriority = v.findViewById(R.id.number_picker_priority);
//        numberPickerPriority.setMaxValue(10);
//        numberPickerPriority.setMinValue(1);
        radioPriorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.radio0:
                            // do operations specific to this selection
                            priority = 1;
                            break;
                        case R.id.radio1:
                            // do operations specific to this selection
                            priority = 2;
                            break;
                    }
            }
        });


        if(getArguments() != null){
            String title = getArguments().getString("title");
            String description = getArguments().getString("description");
            int priority = getArguments().getInt("priority");
            editTextTitle.setText(title);
            editTextDescription.setText(description);
            numberPickerPriority.setValue(priority);
        }
        cancel = v.findViewById(R.id.button_cancel_task);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).closeTaskFragment();
            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof fragTaskAddUpdateListener){
            listener = (fragTaskAddUpdateListener) context;
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
        menu.findItem(R.id.delete_all_categories).setVisible(false);
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
        int id = -1;
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please fill title and description",Toast.LENGTH_SHORT).show();
            return;
        }
        if(getArguments() != null){
            id = getArguments().getInt("id");
        }
        listener.onInputSend(id,title,description,priority);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}