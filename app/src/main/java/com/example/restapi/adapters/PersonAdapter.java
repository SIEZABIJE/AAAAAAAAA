package com.example.restapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restapi.PersonDetailActivity;
import com.example.restapi.R;
import com.example.restapi.models.Person;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<Person> personList;
    private Context context;

    public PersonAdapter(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = personList.get(position);

        Picasso.get().load(person.getImage()).into(holder.imageView);

        holder.nameView.setText(person.getTitle() + " " + person.getName());
        holder.phoneView.setText(person.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PersonDetailActivity.class);
            intent.putExtra("image", person.getImage());
            intent.putExtra("title", person.getTitle());
            intent.putExtra("name", person.getName());
            intent.putExtra("gender", person.getGender());
            intent.putExtra("location", person.getLocation());
            intent.putExtra("email", person.getEmail());
            intent.putExtra("phone", person.getPhone());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void updateList(List<Person> newList) {
        this.personList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView phoneView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.personImage);
            nameView = view.findViewById(R.id.personName);
            phoneView = view.findViewById(R.id.personPhone);
        }
    }
} 