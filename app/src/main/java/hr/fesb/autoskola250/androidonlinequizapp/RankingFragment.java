package hr.fesb.autoskola250.androidonlinequizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.fesb.autoskola250.androidonlinequizapp.Common.Common;
import hr.fesb.autoskola250.androidonlinequizapp.Interface.ItemClickListener;
import hr.fesb.autoskola250.androidonlinequizapp.Interface.RankingCallBack;
import hr.fesb.autoskola250.androidonlinequizapp.Model.QuestionScore;
import hr.fesb.autoskola250.androidonlinequizapp.Model.Ranking;
import hr.fesb.autoskola250.androidonlinequizapp.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {
    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTable;

    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTable = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        //Init view
        rankingList = (RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        //rankingList.setHasFixedSize(true); //pripazit
        //OrderByChild-Firebase metoda će sortirati silazno->moramo zamijeniti Recycler podatke
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        //implementiranje callbacka
        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTable.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking();

            }
        });

        //postavljanje adaptera
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTable.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {
              viewHolder.txt_name.setText(model.getUserName());
              viewHolder.txt_score.setText(String.valueOf(model.getScore()));

              viewHolder.setItemClickListener(new ItemClickListener() {
                  @Override
                  public void OnClick(View view, int position, boolean isLongClick) {

                  }
              });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;

    }




    private void updateScore(final String userName, final RankingCallBack callBack) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());
                        }
                        //Firebase je asinkrona DB i ako procesiuramo sumu izvan naša suma će se restirat na nulu
                        Ranking ranking = new Ranking(userName,sum);
                        callBack.callBack(ranking); //nakon uploada ide sortiranje tablice i prikaz rezultata
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
