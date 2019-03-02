package es.iessaladillo.maria.mmcsr_pr10_fct.ui.students;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

public class StudentFragmentAdapter extends ListAdapter<Student, StudentFragmentAdapter.ViewHolder> {

    protected StudentFragmentAdapter(@NonNull DiffUtil.ItemCallback<Student> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lblName;
        private final TextView lblPhone;
        private final TextView lblGrade;
        private final TextView lblCompanyName;
        private final TextView lblTutorName;
        private final TextView lblTutorPhone;
        public ViewHolder(@NonNull View itemView, TextView lblName, TextView lblPhone, TextView lblGrade, TextView lblCompanyName, TextView lblTutorName, TextView lblTutorPhone) {
            super(itemView);
            this.lblName = lblName;
            this.lblPhone = lblPhone;
            this.lblGrade = lblGrade;
            this.lblCompanyName = lblCompanyName;
            this.lblTutorName = lblTutorName;
            this.lblTutorPhone = lblTutorPhone;
        }
    }
}
