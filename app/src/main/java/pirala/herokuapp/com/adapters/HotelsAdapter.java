package pirala.herokuapp.com.adapters;

import android.content.Context;
import android.gesture.Gesture;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pirala.herokuapp.com.R;
import pirala.herokuapp.com.model.Hotel;

/**
 * Created by giuseppe on 30/06/16.
 */
public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.MyViewHolder>{
    private Context mContext;
    private List<Hotel> hotelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,email;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public HotelsAdapter(Context mContext, List<Hotel> hotelList) {
        this.mContext = mContext;
        this.hotelList = hotelList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HotelsAdapter.MyViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.name.setText(hotel.getName());
        holder.email.setText(hotel.getEmail());
    }

    private void showPopupMenu(View view){
        PopupMenu popup = new PopupMenu(mContext,view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_hotel,popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
    }
    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Todo Aqui hay que colocar la funcion para mostrar un solo hotel
                    Toast.makeText(mContext, "Marico el que lo lea", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

}
