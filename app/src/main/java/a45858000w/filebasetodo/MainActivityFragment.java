package a45858000w.filebasetodo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View view;
    private ListView list;
    private EditText txtInsert;
    private Button btAdd;
    private ArrayList<String> listaDatos;

    //ArrayAdapter<String> adapter;
    FirebaseListAdapter<String> adapter;

    private int RC_Sign_in =123;
    private FirebaseAuth auth;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_main, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference todosRef = database.getReference("todos");


        list =(ListView) view.findViewById(R.id.listView);
        txtInsert= (EditText) view.findViewById(R.id.txtInsert);
        btAdd = (Button) view.findViewById(R.id.btAdd);

        setupAuth();

        listaDatos= new ArrayList<String>();
        //adapter = new ArrayAdapter<>(this, R.layout.listaitem, listaDatos);

        adapter = new FirebaseListAdapter<String>(this.getActivity(), String.class, R.layout.listaitem, todosRef) {
            @Override
            protected void populateView(View v, String model, int position) {
                Log.d(null, model);
                ((TextView) v.findViewById(R.id.txtItem)).setText(model);
            }
        };

        list.setAdapter(adapter);







        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtInsert.getText().length()>0)
                {
                    String datos=txtInsert.getText().toString();

                    //adapter.add(datos);
                    //myAdapter.add(datos);

                    //txtInsert.setText("");


                    if(!datos.equals("")){
                        DatabaseReference newReference = todosRef.push();
                        newReference.setValue(datos);
                        txtInsert.setText("");
                    }
                }
            }
        });



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









}
