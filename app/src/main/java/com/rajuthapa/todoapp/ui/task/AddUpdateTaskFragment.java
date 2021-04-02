package com.rajuthapa.todoapp.ui.task;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajuthapa.todoapp.MainActivity;
import com.rajuthapa.todoapp.R;
import com.rajuthapa.todoapp.data.category.Category;
import com.rajuthapa.todoapp.data.category.CategoryViewModel;
import com.rajuthapa.todoapp.ui.category.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateTaskFragment extends Fragment{
    private EditText editTextTitle;
    private  EditText editTextDescription;
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
        View v = inflater.inflate(R.layout.add_update_fragment,container,false);
        editTextTitle = v.findViewById(R.id.etNewTask);
        editTextDescription = v.findViewById(R.id.etDisplayDescription);
        radioPriorityGroup = v.findViewById(R.id.radio_priority);
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
            radioPriorityGroup.check(priority == 1 ? R.id.radio0 : R.id.radio1);
        }
        cancel = v.findViewById(R.id.cancelBtn);
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