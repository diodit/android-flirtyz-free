package com.owo.phlurtyz002.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.owo.phlurtyz002.R;
import com.owo.phlurtyz002.adapter.GridAdapter;
import com.owo.phlurtyz002.core.BaseActivity;
import com.owo.phlurtyz002.database.DatabaseHandler;
import com.owo.phlurtyz002.model.GridItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by kibrom on 3/20/17.
 */

public class RecentActivity extends BaseActivity {

    private Context context;

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        activity = this;

        setContentView(R.layout.activity_recent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        DatabaseHandler db = new DatabaseHandler(this);

        final Stack<GridItem> items = db.getRecentList();

        List<String> itemNames = new ArrayList<>();

        List<Bitmap> images = new ArrayList<>();

        while(!items.empty()) {

            GridItem item = items.pop();

            try {

                InputStream is = this.openFileInput(item.getAssetPath());

                images.add(BitmapFactory.decodeStream(is));

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

            itemNames.add(item.getAssetName());
        }

        GridView gridView = (GridView) findViewById(R.id.recent_gridview);

        GridAdapter adapter = new GridAdapter(this, itemNames, images);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String fileName = items.get(position).getAssetPath();

                final File photoFile = new File(context.getFilesDir(), fileName);

                Uri uri = FileProvider.getUriForFile(context, "com.owo.phlurtyz", photoFile);

                Intent sendIntent = ShareCompat.IntentBuilder.from(activity)

                        .setType("image/png")

                        .setSubject("Subject")

                        .setStream(uri)

                        .setChooserTitle("Share Emoji")

                        .createChooserIntent()

                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                context.startActivity(sendIntent);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
