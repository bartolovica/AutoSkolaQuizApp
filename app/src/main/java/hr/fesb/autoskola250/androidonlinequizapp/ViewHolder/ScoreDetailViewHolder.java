package hr.fesb.autoskola250.androidonlinequizapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import hr.fesb.autoskola250.androidonlinequizapp.R;

/**
 * Created by Antun on 27.1.2018..
 */

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_name;
    public TextView txt_score;



    public ScoreDetailViewHolder(View itemView) {
        super(itemView);

        txt_name = (TextView)itemView.findViewById(R.id.txt_name);
        txt_score = (TextView)itemView.findViewById(R.id.txt_score);

    }
}
