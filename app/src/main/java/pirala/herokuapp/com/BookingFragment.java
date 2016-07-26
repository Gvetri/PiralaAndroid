package pirala.herokuapp.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pirala.herokuapp.com.adapters.BookingAdapter;
import pirala.herokuapp.com.dialogs.BookingDialog;
import pirala.herokuapp.com.dialogs.HotelDialog;
import pirala.herokuapp.com.model.Booking;
import pirala.herokuapp.com.model.Hotel;


public class BookingFragment extends Fragment {
    private static final String TAG = "BookingsFragment";
    private static final String MY_PREFS_NAME = "USERINFO";
    private RecyclerView recyclerView;
    private BookingAdapter bAdapter;
    private List<Booking> bookingList;
    private RequestQueue requestQueue;
    private String email_string;
    private String token_string;
    private int id_user;
    public BookingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserCredentials();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        bookingList = new ArrayList<>();
        bAdapter = new BookingAdapter(bookingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(bAdapter);
        requestQueue = Volley.newRequestQueue(getContext());
        prepareBookingData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Booking booking = bookingList.get(position);
                Toast.makeText(getContext(), booking.getCreated_at() + " is selected!", Toast.LENGTH_SHORT).show();
                showBookingDialog(booking);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "LOOOOOONG CLICK", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void getUserCredentials() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        email_string = prefs.getString("email", null);
        token_string = prefs.getString("token",null);
        id_user = prefs.getInt("id_user",0);

    }

    private void showBookingDialog(Booking booking) {
        FragmentManager fm = getFragmentManager();
        BookingDialog bookingDialog = BookingDialog.newInstance(booking);
        bookingDialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
        bookingDialog.show(fm,"dialog_reserva");
    }

    private void prepareBookingData() {

            String url = "http://192.168.1.102:3001/api/v1/bookings.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response: "+ response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("bookings");
                            Log.i(TAG,"JsonObject bookings: \n" + String.valueOf(jsonArray));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject booking_json = jsonArray.getJSONObject(i);
                                Log.i(TAG,"Valor del booking_json \n"+booking_json.toString());
                                if (booking_json.getInt("user_id")==id_user){ //Add only the bookings with the same user id
                                    int booking_id = booking_json.getInt("id");
                                    String date_out = stringIsNull(booking_json.getString("date_out"));
                                    //stringIsNull Verify if string is null dah.
                                    String hotel_id = intIsNull(booking_json.getString("hotel_id"));
                                    //TODO this should be done in the api btw ain't nobody got time for that -meme-
                                    int int_hotel_id = Integer.parseInt(hotel_id);
                                    String status = booking_json.getString("status");
                                    boolean status_b = statusToBoolean(status);
                                    //This method check if the status is null change the status to false
                                    String created_at = stringIsNull(booking_json.getString("created_at"));
                                    String date_in = stringIsNull(booking_json.getString("date_in"));
                                    Booking b = new Booking(date_in,date_out,created_at,"hotel numero "+hotel_id,booking_id,int_hotel_id,status_b);
                                    bookingLog(b); // This method log every booking to show their information
                                    bookingList.add(b);
                                }
                            }
                            bAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Ha ocurrido un error cargando las reservas", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept","application/json");
                headers.put("X-User-Email",email_string);
                headers.put("X-User-Token",token_string);
                Log.i(TAG,email_string + token_string);

                return headers;
            }
        };


            requestQueue.add(jsonObjectRequest);



    }

    private String intIsNull(String string) {
        //Como el fucking id puede ser null no podemos volver int de una vez
        String s;
        if (string.equals("null")) {
            s = "0";
        } else{
            s = string;
        }
        return s;
    }

    private String stringIsNull(String string) {
        String s;
        if (string.equals("null")){
            s = "No disponible";
        } else {
            s = string;
        }
        return s;
    }

    private void bookingLog(Booking b) {
        String booking_string = "Booking Log: \n Booking id: "+b.getId()+" date out: "+b.getDate_out()+" hotel_id: "+b.getHotel_id()+
                " status: "+b.getStatus()+" Created at "+b.getCreated_at()+" date in: "+b.getDate_in();
        Log.i(TAG,booking_string);
    }

    private boolean statusToBoolean(String status) {
        boolean s = false;

        if (status.equals("nil") || status.equals("false")){
            s = false;
        } else if (status.equals("true")){
            s = true;
        }
        return s;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BookingFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BookingFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
