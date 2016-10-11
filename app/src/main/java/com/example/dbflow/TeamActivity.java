package com.example.dbflow;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dbflow.database.models.Player;
import com.example.dbflow.database.models.Player_Table;
import com.example.dbflow.database.models.Team;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class TeamActivity extends AppCompatActivity {

    private FloatingActionButton mFab;
    private EditText mEditText;
    private ListView mListView;

    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        team = getIntent().getParcelableExtra("EXTRA_TEAM");
        setTitle(team.getName());

        mEditText = (EditText) findViewById(R.id.editText);
        mListView = (ListView) findViewById(R.id.listView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = mEditText.getText().toString();

                if (!playerName.isEmpty()) {
                    insertPlayer(playerName);
                    mEditText.setText("");
                    refresh();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = (Player) mListView.getItemAtPosition(position);
                player.delete();
                refresh();
                return false;
            }
        });

        refresh();
    }

    private void insertPlayer(String name) {
        Player player = new Player();
        player.setName(name);
        player.setTeam(team);
        player.save();
    }

    private void refresh() {
        List<Player> players = SQLite.select()
                .from(Player.class)
                .where(Player_Table.team_id.eq(team.getId()))
                .queryList();

        mListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                players));
    }
}
