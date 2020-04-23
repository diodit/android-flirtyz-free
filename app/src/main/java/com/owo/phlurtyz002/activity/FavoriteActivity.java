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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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

/**
 * Created by kibrom on 3/20/17.
 */

public class FavoriteActivity extends BaseActivity {

    private Context context;

    private Activity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        activity = this;

        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DatabaseHandler db = new DatabaseHandler(this);

        final List<GridItem> items = db.getFavoriteList();

        List<String> itemNames = new ArrayList<>();

        List<Bitmap> images = new ArrayList<>();

        for (GridItem item : items) {

            try {

                InputStream is = this.openFileInput(item.getAssetPath());

                images.add(BitmapFactory.decodeStream(is));

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

            itemNames.add(item.getAssetName());
        }

        final GridView gridView = (GridView) findViewById(R.id.favorite_gridview);

        GridAdapter adapter = new GridAdapter(this, itemNames, images);

        gridView.setAdapter(adapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                mode.setTitle("Select Emoji");


                mode.setSubtitle("One Emoji selected");

                MenuInflater inflater = getMenuInflater();

                inflater.inflate(R.menu.remove_from_fav_rec, menu);


                return true;

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub

                switch (item.getItemId()) {

                    case R.id.add_to_favorite:

                        mode.finish();
                }

                int selectCount = gridView.getCheckedItemCount();
                switch (selectCount) {
                    case 1:
                        mode.setSubtitle("One Emoji selected");


                        break;
                    default:
                        mode.setSubtitle("" + selectCount + " Emoji selected");

                        break;
                }

                return true;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // TODO Auto-generated method stub

                //TODO add emojis in array. remove emoji from array when unchecked
//                String[] fileName = imagePaths.get(position).split("/");
//
//                final File photoFile = new File(context.getFilesDir(), fileName[1]);

//                Log.d("fileName", photoFile.toString());

                int selectCount = gridView.getCheckedItemCount();
                switch (selectCount) {
                    case 1:
                        mode.setSubtitle("One Emoji selected");
                        break;
                    default:
                        mode.setSubtitle("" + selectCount + " Emoji selected");
                        break;
                }

            }
        });
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
