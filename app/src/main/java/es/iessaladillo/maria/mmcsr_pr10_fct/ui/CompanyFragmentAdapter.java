package es.iessaladillo.maria.mmcsr_pr10_fct.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class CompanyFragmentAdapter extends ListAdapter<Company, CompanyFragmentAdapter.ViewHolder> {

    interface OnEditableListener{
        void onEdit(int position);
    }


    private OnEditableListener onEditableListener;


    void setOnEditableListener(OnEditableListener onEditableListener) {
        this.onEditableListener = onEditableListener;
    }


    CompanyFragmentAdapter() {
        super(new DiffUtil.ItemCallback<Company>() {
            @Override
            public boolean areItemsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
                return oldItem.getIdCompany() == newItem.getIdCompany();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName()) &&
                        TextUtils.equals(oldItem.getAddress(), newItem.getAddress()) &&
                        TextUtils.equals(oldItem.getPhone(), newItem.getPhone()) &&
                        TextUtils.equals(oldItem.getEmail(), newItem.getEmail()) &&
                        TextUtils.equals(oldItem.getNameContactPerson(), newItem.getNameContactPerson());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    protected Company getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getIdCompany();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView lblName;
        private final TextView lblAddress;
        private final TextView lblPhone;
        private final ImageView imgLogo;
        private final TextView lblEmail;
        private final TextView lblPersonNameContact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            lblName = ViewCompat.requireViewById(itemView, R.id.lblName);
            lblPhone = ViewCompat.requireViewById(itemView, R.id.lblPhone);
            imgLogo = ViewCompat.requireViewById(itemView, R.id.imgLogo);
            lblEmail = ViewCompat.requireViewById(itemView, R.id.lblEmail);
            lblAddress = ViewCompat.requireViewById(itemView, R.id.lblAddress);
            lblPersonNameContact = ViewCompat.requireViewById(itemView, R.id.lblContactPersonName);
            itemView.setOnClickListener(v -> onEditableListener.onEdit(getAdapterPosition()));
        }

        void bind(Company company){
            lblName.setText(company.getName());
            lblPhone.setText(company.getPhone());
            lblEmail.setText(company.getEmail());
            lblAddress.setText(company.getAddress());
            lblPersonNameContact.setText(company.getNameContactPerson());
            Picasso.with(itemView.getContext()).load(company.getUrlLogo())
                    .error(R.drawable.ic_enterprise)
                    .resize(200, 200)
                    .into(imgLogo);
        }
    }
}
