package com.geektech.noteapp3lesson2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    private RecyclerView rvNotes;
    private NotesAdaptar adaptar;
    private FloatingActionButton btnOpenAddNote;
    private NoteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptar = new NotesAdaptar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvNotes = view.findViewById(R.id.rv_notes);
        btnOpenAddNote = view.findViewById(R.id.btn_open_add_fragment);
        rvNotes.setAdapter(adaptar);
        db = Room.databaseBuilder(requireContext(), NoteDatabase.class,
                "database").allowMainThreadQueries().build();
        adaptar.getNote(db.dao().getALL());
        delete();
        btnOpenAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, new AddNoteFragment());
                transaction.addToBackStack("AddNoteFragment");
                transaction.commit();
            }
        });

        listenNoteData();

        return view;
    }

    private void delete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adaptar.deleteNote(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvNotes);
    }

    private void listenNoteData() {
        getActivity().getSupportFragmentManager().setFragmentResultListener("noteIsAdding", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                 if (requestKey.equals("noteIsAdding")){
                     NotesModel notesModel = new NotesModel(result.getString("title"), result.getString("description"), result.getString("dateTime"));
                     adaptar.addNote(notesModel);
                 }
            }
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("noteIsEdit", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("noteIsEdit")){
                    NotesModel notesModel = new NotesModel(result.getString("title"), result.getString("description"), result.getString("dateTime"));
                    adaptar.editNote(notesModel, result.getInt("position"));
                }
            }
        });
    }
}