package pirala.herokuapp.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private static final String MY_PREFS_NAME = "USERINFO";
    private EditText email,name,password,password_confirmation;
    private String password_string;
    private String password_confirmation_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password_confirmation = (EditText) findViewById(R.id.password_confirmation);


        Button button_delete = (Button) findViewById(R.id.button_delete);
        Button button_signup = (Button) findViewById(R.id.button_signup);


        assert button_delete != null;
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "Los campos han sido reestablecidos.", Toast.LENGTH_SHORT).show();
                name.setText("");
                email.setText("");
                password.setText("");
                password_confirmation.setText("");
            }
        });

        assert button_signup != null;
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_string = password.getText().toString();
                password_confirmation_string = password_confirmation.getText().toString();

                if (password_string.equals(password_confirmation_string)){

                    Toast.makeText(SignupActivity.this, "Usuario ha sido registrado con exito", Toast.LENGTH_SHORT).show();
                    IniciarRegistro();
                } else{
                    password.setError("Las contraseñas no coinciden");
                    password.requestFocus();
                }
            }

            private void IniciarRegistro() {
//                String url = "http://192.168.1.102:3001/users.json"; url local
                String url = "https://pirala.herokuapp.com/users.json";
                JSONObject jsonObject = new JSONObject();
                String email_string = email.getText().toString();
                String name_string = name.getText().toString();

                JSONObject user = null;

                try {
                    jsonObject.put("email", email_string);
                    jsonObject.put("password", password_string);
                    jsonObject.put("name",  name_string);
                    jsonObject.put("last_name", "last_name");
                    jsonObject.put("status", "true");
                    user = new JSONObject().put("user", jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG,"Response: "+ response.toString());
                                try {
                                    Toast.makeText(SignupActivity.this, "Id del usuario: "+response.getString("id"), Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putInt("id_user",response.getInt("id"));
                                    editor.putString("email",response.getString("email"));
                                    editor.putString("token",response.getString("authentication_token"));
                                    editor.apply();
                                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish(); // Call once you redirect to another activity

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Toast.makeText(SignupActivity.this, "Ha ocurrido un error registrando el usuario. Quizas el correo este ya siendo utilizado", Toast.LENGTH_SHORT).show();
                        }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                    };



                RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
                requestQueue.add(jsonObjectRequest);

            }


        });


    }
}
