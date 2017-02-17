package a45858000w.filebasetodo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.R.attr.data;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View view;
    private ListView list;
    private EditText txtInsert;
    private Button btAdd;
    private Button btVideo;
    private Button btCamara;
    private ArrayList<String> listaDatos;

    //ArrayAdapter<String> adapter;
    FirebaseListAdapter<String> adapter;

    private int RC_Sign_in =123;
    private FirebaseAuth auth;
    private String pathFotoTemporal;
    private String pathVideoTemporal;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private GridView gridview;
    private GridViewImageAdapter adaptador;
    FirebaseDatabase database;
    private DatabaseReference todosRef;



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_main, container, false);

        database = FirebaseDatabase.getInstance();
        todosRef = database.getReference("todos");




        btCamara = (Button) view.findViewById(R.id.btCamara);

        btVideo = (Button) view.findViewById(R.id.btVideo);

        setupAuth();

        listaDatos= new ArrayList<String>();
        //adapter = new ArrayAdapter<>(this, R.layout.listaitem, listaDatos);
/*
        adapter = new FirebaseListAdapter<String>(this.getActivity(), String.class, R.layout.listaitem, todosRef) {
            @Override
            protected void populateView(View v, String model, int position) {
                Log.d(null, model);
                ((TextView) v.findViewById(R.id.txtItem)).setText(model);
            }
        };

        list.setAdapter(adapter);*/





        btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });

        gridview = (GridView) view.findViewById(R.id.grdGaleria);
        // crear el gridview a partir del elemento del xml gridview


        //gridview.setAdapter( new GridViewImageAdapter(getContext()) );


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int
                    position, long id) {

                Toast.makeText(getContext(), "" + position,Toast.LENGTH_SHORT).show();        }});









        return view;
    }






    private void setupAuth() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d("Current user", String.valueOf(auth.getCurrentUser()));
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
                            )
                            .build(),
                    RC_Sign_in);}
    }

    @Override
    public void onStart() {
        super.onStart();

    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathFotoTemporal = "file:" + image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void takeVideo ()
    {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(videoFile));// set the image file name
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
    }


    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String videoFileName = "Video_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathVideoTemporal = "file:" + image.getAbsolutePath();
        return image;
    }







    public void onActivityResult(int requestCode, int resultCode, Intent intent)  {
        super.onActivityResult(requestCode, resultCode, intent);

            try {

                if (requestCode == REQUEST_TAKE_PHOTO) {
                    if (resultCode == RESULT_OK) {

                        //Uri seleccio = intent.getData();

                        Uri.Builder b = Uri.parse(pathFotoTemporal).buildUpon();

                        String[] columna = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContext().getContentResolver().query(b.build(), columna, null, null, null);
                        cursor.moveToFirst();

                        int indexColumna = cursor.getColumnIndex(columna[0]);
                        String rutaFitxer = cursor.getString(indexColumna);
                        if(!rutaFitxer.equals("")){
                             DatabaseReference newReference = todosRef.push();
                             newReference.setValue(rutaFitxer);
                        }
                        cursor.close();
                    }
                }


                if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        // Video captured and saved to fileUri specified in the Intent
                        //Toast.makeText(this, "Video saved to:\n" +data.getData(), Toast.LENGTH_LONG).show();

                        Uri.Builder b = Uri.parse(pathVideoTemporal).buildUpon();

                        String[] columna = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContext().getContentResolver().query(b.build(), columna, null, null, null);
                        cursor.moveToFirst();

                        int indexColumna = cursor.getColumnIndex(columna[0]);
                        String rutaFitxer = cursor.getString(indexColumna);
                        if(!rutaFitxer.equals("")){
                            DatabaseReference newReference = todosRef.push();
                            newReference.setValue(rutaFitxer);
                        }
                        cursor.close();

                    } else if (resultCode == RESULT_CANCELED) {
                        // User cancelled the video capture
                    } else {
                        // Video capture failed, advise user
                    }
                }



            }catch (Exception e) {
                e.printStackTrace();

            }

    }








}
