package pirala.herokuapp.com.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import pirala.herokuapp.com.R;
import pirala.herokuapp.com.model.Hotel;

/**
 * Created by giuseppe on 19/07/16.
 */
public class HotelDialog extends DialogFragment {

    private static final String TAG = "Hotel Dialog";
    private TextView hotel_name,hotel_about,hotel_phone,hotel_email;
    private RatingBar stars;
    private ImageView back_nav;
    private FloatingActionButton fab;

    public HotelDialog(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_hotel,container,false);
        hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        hotel_about = (TextView) view.findViewById(R.id.description);
        hotel_phone = (TextView) view.findViewById(R.id.telephone);
        hotel_email = (TextView) view.findViewById(R.id.email);
        this.stars = (RatingBar) view.findViewById(R.id.ratingbar);
        back_nav = (ImageView) view.findViewById(R.id.back);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        Hotel hotel = getArguments().getParcelable("hotel");
        if (hotel == null) {
            Toast.makeText(getActivity(), "Nulo de nuevo", Toast.LENGTH_SHORT).show();
        } else{
            try {
                hotel_name.setText(hotel.getName());
                hotel_about.setText(hotel.getAbout());
                hotel_phone.setText(hotel.getPhone());
                hotel_email.setText(hotel.getEmail());
                this.stars.setRating(hotel.getStars_int());

                back_nav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Reservar!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static HotelDialog newInstance(Hotel hotel){
        HotelDialog f = new HotelDialog();
        Bundle args = new Bundle();
        args.putParcelable("hotel",hotel);
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
