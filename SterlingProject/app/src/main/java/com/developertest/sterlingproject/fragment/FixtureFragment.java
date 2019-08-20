package com.developertest.sterlingproject.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developertest.sterlingproject.Model.MatchFixture;
import com.developertest.sterlingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixtureFragment extends Fragment {

    JSONArray matchArray;
    List<JSONObject> homeTeam = new ArrayList<>();
    List<JSONObject> awayTeam = new ArrayList<>();
    List<JSONObject> scores = new ArrayList<>();
    List<JSONObject> homeTeamScore = new ArrayList<>();
    List<JSONObject> awayTeamScore = new ArrayList<>();
    List<MatchFixture> matchFixtures = new ArrayList();
    RecyclerView recyclerView;

    public FixtureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: check for internet connection first
        String URLString = getString(R.string.APIBase_URL) + "/matches";
        GetMatchFixturesAync getMatchFixturesAync = new GetMatchFixturesAync();
        getMatchFixturesAync.execute(URLString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fixture, container, false);
        initControls(rootView);
        return rootView;
    }

    private void initControls(View rootView) {
        recyclerView = rootView.findViewById(R.id.rv_fixtures);
    }

    public class GetMatchFixturesAync extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Auth-Token", getString(R.string.API_Key));

                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (jsonObject != null){
                try {
                    matchArray = jsonObject.getJSONArray("matches");
                    for(int i = 0; i < matchArray.length(); i++){
                        MatchFixture matchFixture = new MatchFixture();
                        homeTeam.add(((JSONObject) matchArray.get(i)).getJSONObject("homeTeam"));
                        awayTeam.add(((JSONObject) matchArray.get(i)).getJSONObject("awayTeam"));
                        scores.add((((JSONObject) matchArray.get(i)).getJSONObject("score")).getJSONObject("fullTime"));
                        matchFixture.AwayTeamName = awayTeam.get(i).getString("name");
                        matchFixture.AwayTeamScore = scores.get(i).getString("awayTeam") == "null" ?
                                "0" : scores.get(i).getString("awayTeam");
                        matchFixture.HomeTeamName = homeTeam.get(i).getString("name");
                        matchFixture.HomeTeamScore = scores.get(i).getString("homeTeam")== "null" ?
                                "0" : scores.get(i).getString("homeTeam");
                        matchFixture.MatchTime = ((JSONObject) matchArray.get(i)).getString("utcDate").substring(12, 19);
                        //matchFixture.AwayTeamName = awayTeam.get(i).getString("name");
                        matchFixtures.add(matchFixture);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    final FixtureRecyclerViewAdaper adapter = new FixtureRecyclerViewAdaper();
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public class FixtureRecyclerViewAdaper extends RecyclerView.Adapter<FixtureRecyclerViewAdaper.ViewHolder>{

        @NonNull
        @Override
        public FixtureRecyclerViewAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fixture_content, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FixtureRecyclerViewAdaper.ViewHolder viewHolder, int position) {
            if (matchFixtures != null) {
                MatchFixture current = matchFixtures.get(position);
                viewHolder.tvStartTime.setText(current.MatchTime);
                viewHolder.tvHomeTeam.setText(current.HomeTeamName);
                viewHolder.tvAwayTeam.setText(current.AwayTeamName);
                viewHolder.tvHomeTeamScore.setText(current.HomeTeamScore);
                viewHolder.tvAwayTeamScore.setText(current.AwayTeamScore);
            }
        }

        @Override
        public int getItemCount() {
            return matchFixtures.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvStartTime;
            final TextView tvHomeTeam;
            final TextView tvAwayTeam;
            final TextView tvHomeTeamScore;
            final TextView tvAwayTeamScore;

            ViewHolder(View view) {
                super(view);
                tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
                tvHomeTeam = (TextView) view.findViewById(R.id.tv_home_team);
                tvAwayTeam = (TextView) view.findViewById(R.id.tv_away_team);
                tvHomeTeamScore = (TextView) view.findViewById(R.id.tv_home_team_score);
                tvAwayTeamScore = (TextView) view.findViewById(R.id.tv_away_team_score);
            }
        }
    }
}
