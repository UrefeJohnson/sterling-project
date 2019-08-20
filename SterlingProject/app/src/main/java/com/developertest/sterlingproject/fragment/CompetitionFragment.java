package com.developertest.sterlingproject.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developertest.sterlingproject.Adapter.MyPagerAdapter;
import com.developertest.sterlingproject.Adapter.RecyclerItemClickListener;
import com.developertest.sterlingproject.Model.Competition;
import com.developertest.sterlingproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.PortUnreachableException;
import java.net.URL;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionFragment extends Fragment {

    RecyclerView recyclerView;
    JSONArray competitions;
    List<Competition> competitionList = new ArrayList<>();

    public CompetitionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: check for internet connection first

        String URLString = getString(R.string.APIBase_URL) + "/competitions";
        GetAllCompetitionAsync getAllCompetitionAsync = new GetAllCompetitionAsync();
        getAllCompetitionAsync.execute(URLString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_competition, container, false);
        recyclerView = rootView.findViewById(R.id.rv_competitions);
        return rootView;
    }

    public void ShowRecyclerView(){

        final CompetitionRecyclerViewAdapter competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LeagueFragment leagueFragment = new LeagueFragment();
                Competition currentItem = competitionList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("competition", currentItem);
                leagueFragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, leagueFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                //ft.replace(android.R.id.content, ltf);
                ft.addToBackStack(null);
                ft.commit();
            }
        }));
        recyclerView.setAdapter(competitionRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public class GetAllCompetitionAsync extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HttpURLConnection connection;
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Auth-Token", getString(R.string.API_Key));
                int response = connection.getResponseCode();

                if(response == connection.HTTP_OK){
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                    return new JSONObject(builder.toString());
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(jsonObject != null){
                    competitions = (JSONArray) jsonObject.getJSONArray("competitions");
                    for(int i = 0; i < competitions.length(); i++){
                        if(competitions.getJSONObject(i).getString("currentSeason") != "null"){
                            JSONObject season = ((JSONObject) competitions.getJSONObject(i)).getJSONObject("currentSeason");
                            Competition competition = new Competition();
                            competition.Id = ((JSONObject) competitions.getJSONObject(i)).getString("id");
                            competition.Name = ((JSONObject) competitions.getJSONObject(i)).getString("name");
                            competitionList.add(competition);
                        }
                    }
                    //TODO: sort in descending order
                    //java.util.Collections.sort(competitionList);
                    ShowRecyclerView();
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public class CompetitionRecyclerViewAdapter extends RecyclerView.Adapter<CompetitionRecyclerViewAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.competition_content, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if(competitionList != null){
                Competition competition = competitionList.get(position);
                viewHolder.competitionName.setText(competition.Name);
            }
        }

        @Override
        public int getItemCount() {
            return competitionList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            final TextView competitionName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                competitionName = (TextView) itemView.findViewById(R.id.tv_competition);
            }
        }

    }

}
