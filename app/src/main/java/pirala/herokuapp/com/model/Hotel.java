package pirala.herokuapp.com.model;

/**
 * Created by giuseppe on 30/06/16.
 */
public class Hotel {
    private String name,email,phone,about,stars,address,status;

    public Hotel(String name, String email, String phone, String about, String stars, String address, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.stars = stars;
        this.address = address;
        this.status = status;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
