package android.appdevgenie.voyage.adapter;

import android.appdevgenie.voyage.R;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VoyageAdapter extends RecyclerView.Adapter<VoyageAdapter.EntryViewHolder> {

    private Context context;
    private ItemClickListener itemClickListener;
    private List<NewEntry> newEntries;

    public VoyageAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.entry_list_item, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {

        NewEntry newEntry = newEntries.get(position);
        String date = newEntry.getEntryDate();
        String time = newEntry.getEntryTime();
        String info = newEntry.getThoughts();

        holder.tvDate.setText(date);
        holder.tvTime.setText(time);
        holder.tvThoughts.setText(info);

    }

    @Override
    public int getItemCount() {
        if(newEntries == null) {
            return 0;
        }
        return newEntries.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDate;
        private TextView tvTime;
        private TextView tvThoughts;

        public EntryViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvItemDate);
            tvTime = itemView.findViewById(R.id.tvItemTime);
            tvThoughts = itemView.findViewById(R.id.tvItemInfo);
        }
    }

    public void setEntries(List<NewEntry> newEntriesList){
        newEntries = newEntriesList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

}
