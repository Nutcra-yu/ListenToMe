package android.ListenToMe.ListenToMe.Adapter;


import android.ListenToMe.ListenToMe.CardBean;
import android.ListenToMe.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<CardBean> list;
    private Context context;
    private onItemClickListener onItemClickListener;

    public MenuAdapter(List<CardBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public interface onItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_menu_listentome_activity, parent, false);
        return new MenuViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        String engStr = list.get(position).getEngStr();
        String drawableName = "cardmenu_" + engStr.toLowerCase() ;
        int drawableId = context.getResources().getIdentifier(drawableName,"drawable",context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context,drawableId);
        holder.imageView.setBackground(drawable);
        holder.textView.setText(list.get(position).getName());

        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_card_menu_item);
            textView = itemView.findViewById(R.id.tv_name_menu_item);
        }
    }
}