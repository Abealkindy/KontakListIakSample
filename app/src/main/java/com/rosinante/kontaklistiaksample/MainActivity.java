package com.rosinante.kontaklistiaksample;

import android.content.CursorLoader;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerroom)
    RecyclerView recyclerroom;

    private RecyclerAdapter recyclerAdapter;

    private static final String AUTHORITY = "com.abraham.androidroomiak.provider";
    private static final String BASE_PATH = "kontak";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int KONTAK = 1;
    private static final int KONTAK_ID = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(AUTHORITY, BASE_PATH, KONTAK);
        MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", KONTAK_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerAdapter = new RecyclerAdapter();
        recyclerroom.setLayoutManager(new LinearLayoutManager(this));
        recyclerroom.setAdapter(recyclerAdapter);
        getLoaderManager().initLoader(KONTAK, null, loaderCallBack);
    }

    private LoaderManager.LoaderCallbacks<Cursor>loaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(), CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recyclerAdapter.setKontak(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            recyclerAdapter.setKontak(null);
        }
    };
    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private Cursor cursor;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)){
            holder.textRecyler.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama")));
        }
        }

        @Override
        public int getItemCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        public void setKontak(Cursor o) {
            cursor = o;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textRecyler;
            public ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
                textRecyler = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
