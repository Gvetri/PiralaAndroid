package pirala.herokuapp.com;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "admin@gmail.com:12345", "giuseppe@pirala.com:12345"
    };
    private static final String MY_PREFS_NAME = "USERINFO";
    private static final String TAG = "LoginActivity-Error";


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Toolbar toolbar;

    private String email,token;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        email = prefs.getString("email", null);
        token = prefs.getString("token",null);
        id = prefs.getInt("id_user",0);

        Log.i(TAG,"Email: "+email+" Token: "+token+"Id: "+id);
        if ((email != null && token != null) && id!=0){
            verificarCredenciales(email,token,id);
        }
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signup_button = (Button) findViewById(R.id.button_signup_login);
        assert signup_button != null;
        signup_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrarse();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void verificarCredenciales(String email, String token, int id) {
        IniciarActivity(); //TODO ver como hacer para verificar el authentication token sin que se borre el anterior
    }

    private void IniciarActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            IniciarSesion();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

    private void IniciarSesion() {

            String url = "http://192.168.1.102:3001/users/sign_in";
            JSONObject jsonObject = new JSONObject();
            String email_string = mEmailView.getText().toString();
            String password_string = mPasswordView.getText().toString();

            JSONObject user = null;

            try {
                jsonObject.put("password", password_string);
                jsonObject.put("email", email_string);
                user = new JSONObject().put("user", jsonObject);
                Log.i(TAG,"JSON RESPONSE: \n"+jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                    new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG,"Response: "+ response.toString());
                            try {
//                                Toast.makeText(LoginActivity.this, "Id del usuario: "+response.getString("status"), Toast.LENGTH_SHORT).show();
                                //Como el usuario esta dentro del json tenemos que crear otro json object para agarrarlo
                                if (response.getString("status").equals("ok")) {
                                    Toast.makeText(LoginActivity.this, "Iniciando sesion...", Toast.LENGTH_SHORT).show();
                                    JSONObject jo = response.getJSONObject("user");
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putInt("id_user",jo.getInt("id"));
                                    editor.putString("email",jo.getString("email"));
                                    editor.putString("token",jo.getString("authentication_token"));
                                    editor.apply();
                                    IniciarActivity();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(LoginActivity.this, "Ha ocurrido un error iniciando sesion intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Accept","application/json");
                    return headers;
                }
            };



            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(jsonObjectRequest);




    }

    private void Registrarse(){
        Intent i = new Intent(this,SignupActivity.class);
        startActivity(i);
    }
}

