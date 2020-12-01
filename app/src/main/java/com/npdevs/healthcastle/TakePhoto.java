package com.npdevs.healthcastle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.core.content.FileProvider;



public class TakePhoto extends AppCompatActivity {

    private Button takePicture;
    private ImageView imageView;
    private Uri imageFile;
    private ListView foodListView;
    String mCurrentPhotoPath;
    List<Map<String,String>> mFoodData;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SimpleAdapter simpleAdapter;

    private static String MY_TOKEN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        MY_TOKEN = "6fadf8a7a2f4395428af414ce3662efe";


        takePicture = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        foodListView = (ListView) findViewById(R.id.foodListView);


        takePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture(v);
            }
        });

        mFoodData = JSONUtil.getInitalListData();

        // create the grid item mapping
        String[] from = new String[] {"col_1", "col_2" };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2};

        ListView listView = (ListView) findViewById(R.id.foodListView);
        simpleAdapter = new SimpleAdapter(this,mFoodData,android.R.layout.simple_list_item_1,from,to);

        listView.setAdapter(simpleAdapter);
    }

    // using intent to take a picture with existing camera app on the phone
    public void takePicture(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("FoodRecognitionExample", ex.getMessage(), ex);
                // TODO: return and toast
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.npdevs.healthcastle.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap original = setPic(); // set image to the ImageView

            Bitmap cropped = ImageUtil.cropCenterImage(original, 544, 544); // crop center image and resize to 544x544
            FoodTask foodTask = new FoodTask(MY_TOKEN, cropped);

            final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait...", "Recognizing food");
            progressDialog.setCancelable(true);

            new FoodRecognitionTask(new FoodServiceCallback<JSONObject>() {

                @Override
                public void finishRecognition(JSONObject response, FoodRecognitionException exception) {

                    progressDialog.dismiss();

                    if (exception != null) {
                        // handle exception gracefully
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        JSONUtil.foodJsonToList(response, mFoodData);
                        simpleAdapter.notifyDataSetChanged();
                    }

                }
            }).execute(foodTask);

        }

    }

    private Bitmap setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        try {
            bitmap = ImageUtil.rotateImageIfRequired(bitmap, Uri.parse(mCurrentPhotoPath));
        } catch (IOException e) {
            Log.e("FoodRecognitionExample", e.getMessage(),e);
        }

        imageView.setImageBitmap(bitmap);

        return bitmap;
    }
}


class FoodRecognitionException extends Exception {
    public FoodRecognitionException(String msg) {
        super(msg);
    }

    public FoodRecognitionException(Throwable cause) {
        super(cause);
    }

    public FoodRecognitionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

class FoodRecognitionTask extends AsyncTask<FoodTask,Void,String> {

    private FoodServiceCallback<JSONObject> delegate = null;
    private FoodRecognitionException exception = null;

    public FoodRecognitionTask(FoodServiceCallback<JSONObject> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected String doInBackground(FoodTask... params) {
        try {
            FoodRecognitionClient foodClient = new FoodRecognitionClient(params[0]);
            String response = foodClient.send();
            return response;
        } catch (IOException e) {
            Log.e("FoodRecognitionExample", e.getMessage(), e);
            this.exception = new FoodRecognitionException(e.getMessage());
        } catch (FoodRecognitionException e) {
            Log.e("FoodRecognitionExample", e.getMessage(), e);
            this.exception = e;
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result){

        JSONObject json = null;
        try {
            if (result != null && result.startsWith("{")) {
                json = new JSONObject(result);
                if (json.has("error")) {
                    JSONObject error = json.optJSONObject("error");
                    String errorDetail = error.optString("errorDetail");
                    int code = error.optInt("code");
                    this.exception = new FoodRecognitionException("Code: " + code + ", error: " + errorDetail);
                }
            } else {
                this.exception = new FoodRecognitionException("Returned document not a valid JSON: " + result);
            }
        } catch (JSONException e) {
            Log.e("FoodRecognitionExample", e.getMessage(), e);
            this.exception = new FoodRecognitionException(e.getMessage());
        }
        delegate.finishRecognition(json, this.exception);
    }
}

interface FoodServiceCallback<T> {

    /**
     * Indicates that the upload operation has finished. This method is called even if the
     * upload hasn't completed successfully.
     */
    void finishRecognition(JSONObject response, FoodRecognitionException exception);
}

class FoodTask {

    private Bitmap image;
    private String token;

    public FoodTask(String token, Bitmap image) throws IllegalArgumentException {
        if (token == null || image == null) {
            throw new IllegalArgumentException("Both parameters required");
        }
        this.token = token;
        this.image = image;
    }

    public String getToken() {
        return this.token;
    }

    public Bitmap getImage() {
        return this.image;
    }
}

class FoodRecognitionClient {

    private static final String ENDPOINT = "https://api-2445582032290.production.gw.apicast.io/v1/foodrecognition";

    private String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * Food Recognition for Calorie Mama API Main method
     *
     * @param foodTask
     * @throws IOException
     */
    public FoodRecognitionClient(FoodTask foodTask) throws IOException {
        // creates a unique boundary based on time stamp
        if (foodTask == null) {
            throw new IllegalArgumentException("Food task can't be null");
        }

        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(ENDPOINT + "?user_key=" + foodTask.getToken());
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);    // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"),
                true);

        addImage("file", foodTask.getImage());
    }

    private void addImage(String fieldName, Bitmap image) throws IOException {
        String fileName = "foodimage.jpg";
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.writeTo(outputStream);
        writer.append(LINE_FEED);
        writer.flush();
        outputStream.flush();
        out.flush();
        out.close();
    }

    public String send() throws FoodRecognitionException {
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        StringBuilder output = new StringBuilder();

        try {

            int status = httpConn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
                reader.close();
                httpConn.disconnect();
            } else if (status == HttpURLConnection.HTTP_FORBIDDEN) {
                throw new FoodRecognitionException("Error code: " + status + ", Description: Access denied. Check your Token please");
            } else {
                throw new FoodRecognitionException("Error code: " + status + ", Description: " + output.toString());
            }
        } catch (IOException e) {
            throw new FoodRecognitionException(e.getMessage(), e);
        }
        return output.toString();
    }

}

class ImageUtil {

    /**
     * Crops the center of the image to desired wiidth, height
     * @param sourceBitmap source image from camera
     * @param targetWidth must be 544 for food recognition
     * @param targetHeight must be 544 for food recognition
     * @return cropped bitmap
     */
    public static Bitmap cropCenterImage(Bitmap sourceBitmap, int targetWidth, int targetHeight) {
        Bitmap outputBitmap = ThumbnailUtils.extractThumbnail(sourceBitmap, targetWidth, targetHeight);
        return outputBitmap;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);


        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}

class JSONUtil {


    public static List<Map<String,String>> getInitalListData() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(createItem("Food Name", "Calories"));
        return list;
    }

    public static void foodJsonToList(JSONObject response, List<Map<String, String>> list) {

        list.clear();

        if (response != null) {
            JSONArray results = response.optJSONArray("results");
            for (int i=0; i<results.length(); i++) {
                JSONObject result = results.optJSONObject(i);
                JSONArray items = result.optJSONArray("items");
                for (int j=0; j<items.length(); j++) {
                    JSONObject item = items.optJSONObject(j);
                    String itemName = item.optString("name","");
                    JSONObject nutrition = item.optJSONObject("nutrition");
                    int calories = nutrition.optInt("calories");
                    list.add(createItem(itemName + " (" + calories + " Cal)", ""));
                }
            }
        }
    }

    private static Map<String,String> createItem(String foodName, String calories) {
        Map<String,String>  item =  new HashMap<>();
        item.put("col_1", foodName);
        item.put("col_2", calories);
        return item;
    }

}
