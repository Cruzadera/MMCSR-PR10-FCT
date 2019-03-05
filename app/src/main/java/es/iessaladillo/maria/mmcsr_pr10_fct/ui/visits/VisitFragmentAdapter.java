package es.iessaladillo.maria.mmcsr_pr10_fct.ui.visits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;

public class VisitFragmentAdapter extends ListAdapter<Visit, VisitFragmentAdapter.ViewHolder> {

    interface OnEditableListener{
        void onEdit(int position);
    }


    private OnEditableListener onEditableListener;


    void setOnEditableListener(OnEditableListener onEditableListener) {
        this.onEditableListener = onEditableListener;
    }

    VisitFragmentAdapter(){
        super(new DiffUtil.ItemCallback<Visit>() {
            @Override
            public boolean areItemsTheSame(@NonNull Visit oldItem, @NonNull Visit newItem) {
                return oldItem.getIdVisit() == newItem.getIdVisit();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Visit oldItem, @NonNull Visit newItem) {
                return oldItem.getDate().equals(newItem.getDate()) &&
                        oldItem.getStartTime().equals(newItem.getStartTime()) &&
                        oldItem.getEndTime().equals(newItem.getEndTime());
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VisitFragmentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    protected Visit getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getIdVisit();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView lblDate;
        private final TextView lblEndTime;
        private final TextView lblStartTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblDate = ViewCompat.requireViewById(itemView, R.id.lblDate);
            lblEndTime = ViewCompat.requireViewById(itemView, R.id.lblEndTime);
            lblStartTime = ViewCompat.requireViewById(itemView, R.id.lblStartTime);
        }

        void bind(Visit visit){
            lblDate.setText(visit.getDate());
            lblStartTime.setText(visit.getStartTime());
            lblEndTime.setText(visit.getEndTime());
        }
    }
}
