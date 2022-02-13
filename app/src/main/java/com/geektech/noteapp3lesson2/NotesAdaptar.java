package com.geektech.noteapp3lesson2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class NotesAdaptar extends RecyclerView.Adapter<NotesAdaptar.NoteViewHolder> {

    private List<NotesModel> list = new ArrayList<>();
    private FragmentActivity activity;
    private NoteDatabase db;

    public void getNote(List<NotesModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addNote(NotesModel model){
        db = Room.databaseBuilder(activity.getApplicationContext(), NoteDatabase.class,
                "database").allowMainThreadQueries().build();
        db.dao().addNote(model);
    }

    public void editNote(NotesModel model, int position){
        this.list.set(position, model);
        notifyDataSetChanged();
    }

    public void deleteNote(int position){
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public NotesAdaptar(FragmentActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new NoteViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.txtTitle.setText(list.get(position).getTitle());
        holder.txtDescription.setText(list.get(position).getDescription());
        holder.txtDate.setText(list.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = list.get(position).getTitle();
                String description = list.get(position).getDescription();

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putInt("position", position);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AddNoteFragment.class, bundle, "").addToBackStack("EditNote").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtDescription, txtDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_note_title);
            txtDescription = itemView.findViewById(R.id.txt_note_description);
            txtDate = itemView.findViewById(R.id.txt_note_date);
        }
    }
}
