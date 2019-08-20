package com.developertest.sterlingproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developertest.sterlingproject.Model.Competition;
import com.developertest.sterlingproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueTeamFragment extends Fragment {

    public LeagueTeamFragment() {
        // Required empty public constructor
    }

    GridView gridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        Competition obj= (Competition) bundle.getSerializable("competition");
        String URLString = getString(R.string.APIBase_URL) + "/competitions/" + obj.Id + "/teams";
        //GetLeagueTableAsync getAllCompetitionAsync = new GetLeagueTableAsync();
        //getAllCompetitionAsync.execute(URLString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_league_team, container, false);

        //finding listview
        gridView = rootView.findViewById(R.id.gv_teams);

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity().getApplicationContext(),fruitNames[i],Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity().getApplicationContext(), GridItemActivity.class);
//                intent.putExtra("name",fruitNames[i]);
//                intent.putExtra("image",fruitImages[i]);
//                startActivity(intent);
            }
        });

        return rootView;
    }

    String[] fruitNames = {"Apple","Orange","strawberry","Melon","Kiwi","Banana"};
    int[] fruitImages = {R.drawable.ic_launcher_foreground_arrow,R.drawable.ic_launcher_foreground_arrow, R.drawable.ic_launcher_foreground_arrow,
            R.drawable.ic_launcher_foreground_arrow,R.drawable.ic_launcher_foreground_arrow,R.drawable.ic_launcher_foreground_arrow};

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fruitImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.team_grid, null);
            //getting view in row_data
            TextView name = view1.findViewById(R.id.tv_league_team_name);
            ImageView image = view1.findViewById(R.id.im_league_team_image);

            name.setText(fruitNames[i]);
            image.setImageResource(fruitImages[i]);
            return view1;
        }
    }

}
