package pirala.herokuapp.com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by giuseppe on 19/07/16.
 */
public class Booking implements Parcelable {
    String date_in,date_out,created_at,hotel_name;
    Boolean status;
    int id,hotel_id;

    public Booking(String date_in, String date_out, String created_at, String hotel_name, int id, int hotel_id,boolean status) {
        this.date_in = date_in;
        this.date_out = date_out;
        this.created_at = created_at;
        this.hotel_name = hotel_name;
        this.id = id;
        this.hotel_id = hotel_id;
        this.status = status;
    }

    protected Booking(Parcel in) {
        date_in = in.readString();
        date_out = in.readString();
        created_at = in.readString();
        hotel_name = in.readString();
        id = in.readInt();
        hotel_id = in.readInt();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date_in);
        dest.writeString(date_out);
        dest.writeString(created_at);
        dest.writeString(hotel_name);
        dest.writeInt(id);
        dest.writeInt(hotel_id);
    }
}
