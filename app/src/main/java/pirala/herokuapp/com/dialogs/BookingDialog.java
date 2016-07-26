package pirala.herokuapp.com.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pirala.herokuapp.com.R;
import pirala.herokuapp.com.model.Booking;

/**
 * Created by giuseppe on 22/07/16.
 */
public class BookingDialog extends DialogFragment {
    private static final String URL = "http://192.168.1.102:3001/api/v1/bookings/";
    private static final String TAG = "BookingDialog";
    private static final String MY_PREFS_NAME = "USERINFO";
    private RequestQueue requestQueue;

    public BookingDialog(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_booking,container,false);
        TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        TextView booking_in = (TextView) view.findViewById(R.id.booking_in);
        TextView booking_out = (TextView) view.findViewById(R.id.booking_out);
        TextView created_at = (TextView) view.findViewById(R.id.created_at);
        TextView status = (TextView) view.findViewById(R.id.status);
        Button delete_booking = (Button) view.findViewById(R.id.button);
        ImageView back_nav = (ImageView) view.findViewById(R.id.back_nav);

        final Booking booking = getArguments().getParcelable("booking");
        requestQueue = Volley.newRequestQueue(getContext());
        if (booking == null) {
            Toast.makeText(getActivity(), "Ha ocurrido un error cargando la informacion de la reserva", Toast.LENGTH_SHORT).show();
        } else{
            try {
                hotel_name.setText(booking.getHotel_name());
                booking_in.setText(booking.getDate_in());
                booking_out.setText(booking.getDate_out());
                String created_at_string = truncateDate(booking.getCreated_at());
                //Truncate shows only the first 10 char of the date
                created_at.setText(created_at_string);
                String status_string = booleanToString(booking.getStatus());
                status.setText(status_string);

                back_nav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });

                delete_booking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EliminarBooking(booking);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private String truncateDate(String date) {
        String s;
        if (date.equals("No disponible")) {
            s = date;
        } else {
            s = date.substring(0,10);
        }
        return s;
    }

    private void EliminarBooking(Booking booking) {
        String url = URL+ booking.getId()+ ".json";
        Log.i(TAG,url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response: "+ response.toString());
                        try {
                            if(response.getString("code").equals("0")){
                                Toast.makeText(getActivity(), "La reserva ha sido eliminada", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Ha ocurrido un error eliminando la reserva", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String email_string = getEmailCredential();
                String token_string = getTokenCredential();
                Log.i(TAG,"EMAIL Y TOKEN : "+email_string + token_string);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept","application/json");
                headers.put("X-User-Email",email_string);
                headers.put("X-User-Token",token_string);

                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private String getTokenCredential() {
        String s;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        s = sharedPreferences.getString("token", null);
        return s;
    }

    private String getEmailCredential() {
        String s;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        s = sharedPreferences.getString("email", null);
        return s;
    }

    private String booleanToString(Boolean status) {
        String s;
        if (status){
            s = "Activa";
        } else {
            s = "Suspendida";
        }
        return s;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public static BookingDialog newInstance(Booking booking){
        BookingDialog f = new BookingDialog();
        Bundle args = new Bundle();
        args.putParcelable("booking",booking);
        f.setArguments(args);
        return f;
    }



    @Override

    public void onResume() {

        // Get existing layout params for the window

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent

        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // Call super onResume after sizing

        super.onResume();

    }
}
