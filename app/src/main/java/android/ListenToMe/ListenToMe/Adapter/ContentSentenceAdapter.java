package android.ListenToMe.ListenToMe.Adapter;

import android.ListenToMe.ListenToMe.CardBean;
import android.ListenToMe.R;
import android.annotation.SuppressLint;
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

public class ContentSentenceAdapter extends RecyclerView.Adapter<ContentSentenceAdapter.ContentHolder> {

    private List<CardBean> list;
    private Context context;

    private setItemParamsAndClick setItemParamsAndClick;
    private final int param;

    /**
     * param == 1 表示 content视图
     * @param list
     * @param context
     * @param param
     */
    public ContentSentenceAdapter(List<CardBean> list, Context context,int param) {
        this.list = list;
        this.param = param;
        this.context = context;
    }

    public interface setItemParamsAndClick {
        void onItemClick(View v,int position);
    }

    public void setParamsAndClick(setItemParamsAndClick setItemParamsAndClick){
        this.setItemParamsAndClick = setItemParamsAndClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<CardBean> newList){
        list = newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_sentence_listentome_activity,parent,false);
        if (param == 1){
            inflate = LayoutInflater.from(context).inflate(R.layout.item_content_listentome_activity,parent,false);
        }
        return new ContentHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {

        String engStr = list.get(position).getEngStr();
        String cardName = "card_" + engStr.toLowerCase();
        int drawableId = context.getResources().getIdentifier(cardName,"drawable",context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context,drawableId);

        holder.imageView.setBackground(drawable);
        holder.textView.setText(list.get(position).getName());

        if (setItemParamsAndClick != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemParamsAndClick.onItemClick(v,holder.getAdapterPosition());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ContentHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_card_content_or_sentence_item);
            textView = itemView.findViewById(R.id.tv_name_content_or_sentence_item);
        }

    }
}
