package com.geektech.noteapp3lesson2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {

    private Button btnAdd;
    private EditText etTitle;
    private EditText etDescription;

    public AddNoteFragment(){

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        btnAdd = view.findViewById(R.id.btn_add);

        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);

        listenEditData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String dateTime = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss", Locale.getDefault()).format(new Date());
                NotesModel model = new NotesModel(title, description, dateTime);
                NoteDatabase db = Room.databaseBuilder(requireContext(), NoteDatabase.class,
                        "databse").allowMainThreadQueries().build();
                db.dao().addNote(model);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putString("dateTime", dateTime);
                Toast.makeText(requireContext(), model
                        .getDescription(), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().setFragmentResult("noteIsAdding", bundle);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void listenEditData() {
        if (getArguments() != null){
            btnAdd.setText("Edit");

            etTitle.setText(requireArguments().getString("title"));
            etDescription.setText(requireArguments().getString("description"));

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = etTitle.getText().toString();
                    String description = etDescription.getText().toString();
                    String dateTime = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss", Locale.getDefault()).format(new Date());
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    bundle.putString("dateTime", dateTime);
                    bundle.putInt("position", requireArguments().getInt("position"));
                    getActivity().getSupportFragmentManager().setFragmentResult("noteIsEdit", bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
    }
}