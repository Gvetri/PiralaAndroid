package pirala.herokuapp.com;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
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

import pirala.herokuapp.com.adapters.HotelsAdapter;
import pirala.herokuapp.com.dialogs.HotelDialog;
import pirala.herokuapp.com.model.Hotel;

public class hotel_activity extends Fragment {

    private static final String TAG = "HotelActivity";
    private RecyclerView recyclerView;
    private HotelsAdapter adapter;
    private List<Hotel> hotelList;
    RequestQueue requestQueue;
    private TextView buscador;
    private ImageView iv_buscador;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_hotel,container,false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        buscador = (TextView) getView().findViewById(R.id.et_buscador);
        iv_buscador = (ImageView) getView().findViewById(R.id.iv_buscador);

        iv_buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                String query = buscador.getText().toString();
                hotelList.clear();
                prepareHotels(query);

            }
        });

        hotelList = new ArrayList<>();
        adapter = new HotelsAdapter(getContext(),hotelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1 , dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getContext());
        prepareHotels("");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Hotel hotel = hotelList.get(position);
                ShowHotelDialog(hotel);
            }

            private void ShowHotelDialog(Hotel hotel) {
                FragmentManager fm = getFragmentManager();
                HotelDialog hoteldialog = HotelDialog.newInstance(hotel);
                hoteldialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
                hoteldialog.show(fm,"dialog_hotel");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    private void prepareHotels(final String s) {
//        String url = "http://192.168.1.102:3001/hoteles2.json";
        //String url = "https://pirala.herokuapp.com/hoteles2.json";
        String url = "https://pirala.herokuapp.com/api/v1/hotels.json";
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
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG,error.getMessage());
//                        Toast.makeText(getContext(), "Ha ocurrido un error cargando los hoteles", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept:", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("x-name",s);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private hotel_activity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final hotel_activity.ClickListener clickListener) {
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
