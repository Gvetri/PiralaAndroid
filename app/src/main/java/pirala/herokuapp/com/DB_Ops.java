package pirala.herokuapp.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pirala.herokuapp.com.model.Hotel;

/**
 * Created by giuseppe on 29/08/16.
 */
public class DB_Ops {

    private static final String MY_PREFS_NAME = "USERINFO";
    private static final String TAG = "DBOPS";
    private ArrayList<Hotel> hotelList;

    public static void CerrarSesion(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();;
        editor.putInt("id_user", 0);
        editor.putString("email","null");
        editor.putString("token","null");
        editor.apply();
        Toast.makeText(context, "Ha cerrado sesion", Toast.LENGTH_SHORT).show();
    }

    public static List<Hotel> BuscarHoteles(View v, final Context context, final String query, final List<Hotel> hotelList) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://pirala.herokuapp.com/api/v1/hotels.json";
        hotelList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonArray = response.getJSONArray("hotels");
                            Log.i(TAG, String.valueOf(response));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hotel = jsonArray.getJSONObject(i);
                                int id = hotel.getInt("id");
                                String name = hotel.getString("name");
                                String email = hotel.getString("email");
                                String phone = hotel.getString("phone");
                                String about = hotel.getString("about");
                                int stars = hotel.getInt("stars");
                                String address = hotel.getString("address");
                                Hotel new_hotel = new Hotel(name,email,phone,about,stars,address,id);
//                                Log.i(TAG,new_hotel.getName()+" "+new_hotel.getEmail()+ " "+new_hotel.getPhone() + " "+new_hotel.getEmail()+ " "+new_hotel.getId()+ "\n" );
                                    hotelList.add(new_hotel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage() + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"El error es :"+ error );
                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept:", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("x-name",query);
                return headers;
            }
        }

                ;
        requestQueue.add(jsonObjectRequest);
        return hotelList;
    }
}
