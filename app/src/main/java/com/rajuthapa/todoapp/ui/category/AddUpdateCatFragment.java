package com.rajuthapa.todoapp.ui.category;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.rajuthapa.todoapp.MainActivity;
import com.rajuthapa.todoapp.R;
import com.rajuthapa.todoapp.ui.task.AddUpdateTaskFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUpdateCatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUpdateCatFragment extends Fragment {

    private EditText editTextTitleCat;
    private  EditText editTextDescriptionCat;
    private Button cancelCat;
    private AddUpdateCatFragment.fragCatAddUpdateListener listener;

    public AddUpdateCatFragment(){

    }
    public interface fragCatAddUpdateListener{
        void onInputCategorySend(int id, String title, String description);
    }
    public static AddUpdateCatFragment newInstance() {
        return new AddUpdateCatFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_update_cat,container,false);
        editTextTitleCat = v.findViewById(R.id.edit_text_title_category);
        editTextDescriptionCat = v.findViewById(R.id.edit_text_description_category);

        if(getArguments() != null){
            String title = getArguments().getString("title");
            String description = getArguments().getString("description");
            editTextTitleCat.setText(title);
            editTextDescriptionCat.setText(description);
        }
        cancelCat = v.findViewById(R.id.button_cancel_category);
        cancelCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).closeCategoryFragment();
            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof AddUpdateCatFragment.fragCatAddUpdateListener){
            listener = (AddUpdateCatFragment.fragCatAddUpdateListener) context;
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
                saveCategory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveCategory(){
        int id = -1;
        String title = editTextTitleCat.getText().toString();
        String description = editTextDescriptionCat.getText().toString();
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please fill category name and description",Toast.LENGTH_SHORT).show();
            return;
        }
        if(getArguments() != null){
            id = getArguments().getInt("id");
        }
        listener.onInputCategorySend(id,title,description);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}