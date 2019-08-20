package com.developertest.sterlingproject.fragment;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developertest.sterlingproject.Model.Competition;
import com.developertest.sterlingproject.Model.Standings;
import com.developertest.sterlingproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.xml.transform.stream.StreamResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueTableFragment extends Fragment {

    JSONArray standings;
    List<Standings> standingList = new ArrayList<>();
    RecyclerView recyclerView;
    protected ImageView teamImage;

    public LeagueTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        try{
            Competition obj= (Competition) bundle.getSerializable("competition");
            String URLString = getString(R.string.APIBase_URL) + "/competitions/" + obj.Id + "/standings";
            GetLeagueTableAsync getLeagueTableAsync = new GetLeagueTableAsync();
            getLeagueTableAsync.execute(URLString);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_league_table, container, false);
        recyclerView = view.findViewById(R.id.rv_legue_table);
        //teamImage = view.findViewById(R.id.rv_legue_table);
        return view;
    }

    public class GetLeagueTableAsync extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            try{
                HttpURLConnection connection;
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Auth-Token", getString(R.string.API_Key));

                int response = connection.getResponseCode();
                if(response == connection.HTTP_OK){
                    String line;
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
            if(jsonObject != null){
                try{
                    standings = (JSONArray) jsonObject.getJSONArray("standings");
                    for(int i = 0; i < standings.length(); i++){
                        JSONArray statistics = (JSONArray) standings.getJSONObject(i).getJSONArray("table");
                        Standings standing = new Standings();
                        JSONObject jobj = statistics.getJSONObject(i).getJSONObject("team");
                        standing.CrestUrl = jobj.getString("crestUrl");
                        standing.GoalDifference = statistics.getJSONObject(i).getString("goalDifference");
                        standing.Name = jobj.getString("name");
                        standing.PlayedGames = statistics.getJSONObject(i).getString("playedGames");
                        standing.Points = statistics.getJSONObject(i).getString("points");
                        standing.SerialNumber = String.valueOf(i + 1);

                        standingList.add(standing);
                    }
                    //TODO: sort in descending order
                    //java.util.Collections.sort(competitionList);
                    ShowRecyclerView();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    private void ShowRecyclerView() {
        LeagueTableRecyclerViewAdapter leagueTableRecyclerViewAdapter = new LeagueTableRecyclerViewAdapter();
        recyclerView.setAdapter(leagueTableRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public class LeagueTableRecyclerViewAdapter extends RecyclerView.Adapter<LeagueTableRecyclerViewAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.league_table_content, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if(standingList != null){
                Standings standing = standingList.get(position);
                viewHolder.SerialNumber.setText(standing.SerialNumber);
                viewHolder.Name.setText(standing.Name);
                viewHolder.PlayedGames.setText(standing.PlayedGames);
                viewHolder.GoalDifference.setText(standing.GoalDifference);
                viewHolder.Points.setText(standing.Points);
            }
        }

        @Override
        public int getItemCount() {
            return standingList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView SerialNumber;
            //final TextView CrestUrl;
            final TextView Name;
            final TextView PlayedGames;
            final TextView GoalDifference;
            final TextView Points;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                SerialNumber = itemView.findViewById(R.id.tv_serial_number);
                //CrestUrl = itemView.findViewById(R.id.im_league_team_image);
                teamImage = itemView.findViewById(R.id.im_league_team_image);
                Name = itemView.findViewById(R.id.tv_name);
                PlayedGames = itemView.findViewById(R.id.tv_played_games);
                GoalDifference = itemView.findViewById(R.id.tv_goal_difference);
                Points = itemView.findViewById(R.id.tv_point);

            }
        }
    }

}
