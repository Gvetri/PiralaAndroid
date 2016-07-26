package pirala.herokuapp.com.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import pirala.herokuapp.com.R;
import pirala.herokuapp.com.model.Booking;


/**
 * Created by giuseppe on 19/07/16.
 */
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private List<Booking> bookingList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date_in,date_out,created_at,status,hotel_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            date_in = (TextView) itemView.findViewById(R.id.booking_in_tv);
            created_at = (TextView) itemView.findViewById(R.id.created_at_tv);
            hotel_name = (TextView) itemView.findViewById(R.id.hotel_name);
        }



    }
    public BookingAdapter(List<Booking> bookingsList){
        this.bookingList = bookingsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_booking, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.date_in.setText(booking.getDate_in());
        holder.created_at.setText(booking.getCreated_at());
        holder.hotel_name.setText(booking.getHotel_name());
    }

//    This method returns if the status is active or not
    private String BookingStatus(Boolean status) {
        String status_string;
        if (status){
            status_string = "Activa";
        } else {
            status_string = "Cancelada";
        }
        return status_string;
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

}
