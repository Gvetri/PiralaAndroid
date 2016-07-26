package pirala.herokuapp.com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by giuseppe on 30/06/16.
 */
public class Hotel implements Parcelable {
    private String name,email,phone,about,stars,address;
    private boolean status;
    private int id,stars_int;

    public Hotel(String name, String email, String phone, String about, String stars, String address, Boolean status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.stars = stars;
        this.address = address;
        this.status = status;
    }

    public Hotel(String name, String email, String phone, String about,int stars_int, String address, int id) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.stars_int = stars_int;
        this.address = address;
        this.id = id;
    }

    protected Hotel(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        about = in.readString();
        stars = in.readString();
        address = in.readString();
        status = in.readByte() != 0;
        id = in.readInt();
        stars_int = in.readInt();
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStars_int() {
        return stars_int;
    }

    public void setStars_int(int stars_int) {
        this.stars_int = stars_int;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(about);
        dest.writeString(stars);
        dest.writeString(address);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(id);
        dest.writeInt(stars_int);
    }
}
