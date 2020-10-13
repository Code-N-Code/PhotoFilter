package com.codencode.photofilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //UI elements
    ImageView mPlaceholderImage;
    RecyclerView mRecyclerview;
    Button mSaveButton , mSelectButton;

    //image bitmaps
    Bitmap mThumbImage;
    Bitmap mSmallThumbImage;
    Bitmap mImageToBeSaved;

    //other variables
    Uri imgURI;
    boolean mRenew;
    FilterAdapter mAdapter;



    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attachElements();
        handleButtonEvents();
        initOtherVariables();
    }

    void attachElements()
    {
        mPlaceholderImage = findViewById(R.id.main_placegolder_img);
        mRecyclerview = findViewById(R.id.main_recyclerview);
        mSaveButton = findViewById(R.id.main_img_save);
        mSelectButton = findViewById(R.id.main_img_select);
    }

    void initOtherVariables()
    {
        mRenew = false;
        imgURI = null;
        createBitmaps(null);
    }

    void handleButtonEvents()
    {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(getUniqueName());
            }
        });

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRenew = true;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }
        });
    }

    String getUniqueName()
    {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());
        return "image" + "-" + date + "-" + currentTime;
    }

    void createBitmaps(Uri uri)
    {
        if(uri == null)
        {
            assignBitmap(BitmapFactory.decodeResource(this.getResources() , R.drawable.placeholder));
        }
        else
        {
            try {
                assignBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initRecyclerView();
    }

    void assignBitmap(Bitmap src)
    {
        mThumbImage = Bitmap.createScaledBitmap(src ,
                450 , 600 , false);

        mSmallThumbImage = Bitmap.createScaledBitmap(src ,
                64 , 64 , false);

        mImageToBeSaved = src;
    }

    void initRecyclerView()
    {
        Filter f1 = SampleFilters.getBlueMessFilter();
        mPlaceholderImage.setImageBitmap(f1.processFilter(mThumbImage));

        if(mRenew == false)
        {
            mAdapter = new FilterAdapter(getFilterList() , this , mPlaceholderImage , mThumbImage);

            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.HORIZONTAL);
            mRecyclerview.setLayoutManager(manager);
            mRecyclerview.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.setImage(mThumbImage);
            mAdapter.mList.clear();
            mAdapter.mList = getFilterList();
            mAdapter.notifyDataSetChanged();
        }
    }

    List<CircleImage> getFilterList()
    {
        List<CircleImage> list = new ArrayList<>();

        list.add(new CircleImage(Bitmap.createBitmap(mSmallThumbImage) , SampleFilters.getBlueMessFilter()));
        list.add(new CircleImage(Bitmap.createBitmap(mSmallThumbImage) , SampleFilters.getLimeStutterFilter()));
        list.add(new CircleImage(Bitmap.createBitmap(mSmallThumbImage) , SampleFilters.getNightWhisperFilter()));
        list.add(new CircleImage(Bitmap.createBitmap(mSmallThumbImage) , SampleFilters.getStarLitFilter()));
        list.add(new CircleImage(Bitmap.createBitmap(mSmallThumbImage) , SampleFilters.getAweStruckVibeFilter()));

        return list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgURI = result.getUri();

                createBitmaps(imgURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void saveImage(String image_name) {

        if(imgURI == null){
            Toast.makeText(this, "Select an Image", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap finalBitmap = Bitmap.createBitmap(mThumbImage);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = image_name+ ".jpg";
        File file = new File(myDir, fname);

        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();

            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}