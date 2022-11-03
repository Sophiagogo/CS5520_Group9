package edu.northeastern.cs5520_group9.webService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.northeastern.cs5520_group9.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class getImageActivity extends AppCompatActivity {
    TableLayout table;
    ProgressBar progressBar;
    List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        table = findViewById(R.id.all);
        progressBar = findViewById(R.id.progressBar1);
        fetch();
    }

    public void fetch() {                                                                               // Method to fetch all images
        API api = RetrofitClient.getInstance().getAPI();
        Call<List<Image>> getImages = api.getImages();
        getImages.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if (response.isSuccessful()) {
                    images = response.body();                                                           // Get the response and create an image object model
                    table.removeAllViews();                                                             // Remove all views from the table parent view

                    for (int j = 0; j < images.size(); j += 2) {
                        TableRow tr = new TableRow(getImageActivity.this);                                // Create TableView Object
                        // Set orientation parameters for the TableRow
                        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        int top = 40;
                        int bottom = 40;
                        int left = 40;
                        int right = 40;
                        tableRowParams.setMargins(left, top, right, bottom);
                        tr.setLayoutParams(tableRowParams);
                        ImageView img1 = new ImageView(getImageActivity.this);                            // Create ImageView object
                        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(500, 500);   // Set layout parameters for ImageView
                        if (images.get(j).getUrl() == null) {
                            img1.setBackgroundResource(R.drawable.no_image);                            // If no image is available, set a default image
                        } else {
                            img1.setBackgroundResource(R.drawable.image_border);
                            img1.setImageBitmap(getBitmapFromURL(images.get(j).getUrl()));              // Set the image for the ImageView that we get from the response
                        }
                        tr.addView(img1, imgParams);
                        try {
                            ImageView img2 = new ImageView(getImageActivity.this);
                            if (images.get(j+1).getUrl() == null) {
                                img2.setBackgroundResource(R.drawable.no_image);
                            } else {
                                img2.setBackgroundResource(R.drawable.image_border);
                                img2.setImageBitmap(getBitmapFromURL(images.get(j+1).getUrl()));
                            }
                            tr.addView(img2, imgParams);
                        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
                        }
                        progressBar.setVisibility(View.GONE);
                        table.addView(tr, tableRowParams);                                              // Add the views to the TableView parent
                    }
                    Toast.makeText(getImageActivity.this, "Images Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Toast.makeText(getImageActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method will be generating the Bitmap Object from the URL of image.
    public static Bitmap getBitmapFromURL(String src) {
        final Bitmap[] bmp = new Bitmap[1];
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bmp[0] = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                bmp[0] = null;
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bmp[0];
    }
}
