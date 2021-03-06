package android.example.com.ratescurrencyapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder>{
    private ArrayList<Rate> mDataSet;
    private final Context mContext;
    private final RateClickListener listener;

    public RatesAdapter(Context context, RateClickListener listener){
        mDataSet = new ArrayList<>();
        mContext = context;
        this.listener = listener;
    }

    public void updateData(ArrayList<Rate> rates){
        this.mDataSet = rates;
        notifyDataSetChanged();
    }

    @Override
    public RatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //creates a new view
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.mTextView.setText(mDataSet.get(position).getSymbol());
    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View view){
            super(view);
            mTextView = view.findViewById(R.id.tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRateItemClick(mDataSet.get(getAdapterPosition()));
        }
    }

    public interface RateClickListener{
        void onRateItemClick(Rate rate);
    }
}