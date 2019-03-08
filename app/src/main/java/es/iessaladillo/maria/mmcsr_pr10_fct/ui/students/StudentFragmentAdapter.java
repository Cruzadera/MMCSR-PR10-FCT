package es.iessaladillo.maria.mmcsr_pr10_fct.ui.students;

import android.text.TextUtils;
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
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

public class StudentFragmentAdapter extends ListAdapter<Student, StudentFragmentAdapter.ViewHolder> {

    public interface OnEditableListener{
        void onEdit(int position);
    }


    private OnEditableListener onEditableListener;


    void setOnEditableListener(OnEditableListener onEditableListener) {
        this.onEditableListener = onEditableListener;
    }

    StudentFragmentAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getIdStudent() == newItem.getIdStudent();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName()) &&
                        TextUtils.equals(oldItem.getGrade(), newItem.getGrade()) &&
                        TextUtils.equals(oldItem.getPhone(), newItem.getPhone()) &&
                        TextUtils.equals(oldItem.getEmail(), newItem.getEmail()) &&
                        TextUtils.equals(oldItem.getLaborTutorName(), newItem.getLaborTutorPhone()) &&
                        TextUtils.equals(oldItem.getLaborTutorPhone(), newItem.getLaborTutorPhone());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentFragmentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    protected Student getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getIdStudent();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lblName;
        private final TextView lblPhone;
        private final TextView lblGrade;
        private final TextView lblCompanyName;
        private final TextView lblTutorName;
        private final TextView lblTutorPhone;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblName = ViewCompat.requireViewById(itemView, R.id.lblNameStudent);
            lblPhone = ViewCompat.requireViewById(itemView, R.id.lblPhoneStudent);
            lblGrade = ViewCompat.requireViewById(itemView, R.id.lblGrade);
            lblCompanyName = ViewCompat.requireViewById(itemView, R.id.lblCompany);
            lblTutorName = ViewCompat.requireViewById(itemView, R.id.lblTutorName);
            lblTutorPhone = ViewCompat.requireViewById(itemView, R.id.lblTutorPhone);
            itemView.setOnClickListener(v -> onEditableListener.onEdit(getAdapterPosition()));
        }

        void bind(Student student){
            lblName.setText(student.getName());
            lblPhone.setText(student.getPhone());
            lblGrade.setText(student.getGrade());
            lblCompanyName.setText(student.getNameCompany());
            lblTutorPhone.setText(student.getLaborTutorPhone());
            lblTutorName.setText(student.getLaborTutorName());
        }
    }
}
