package pharamacy.eg.sala;

import java.util.ArrayList;

public class Users {
    public String userId,nameU, cityU, neighborhoodU, country_chooser, Specia_work,address,phoneNumber;
    public ArrayList<String> country_work;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)

    }

    public Users(String userId,String nameU, String cityU, String neighborhoodU, String country_chooser, String specia_work, ArrayList<String> country_work,String phoneNumber) {
       this.userId = userId;
        this.nameU = nameU;
        this.cityU = cityU;
        this.neighborhoodU = neighborhoodU;
        this.country_chooser = country_chooser;
        this.Specia_work = specia_work;
        this.country_work = country_work;
        this.phoneNumber = phoneNumber;
    }
    public Users(String userId,String nameU, String cityU, String neighborhoodU, String country_chooser, String address,String phoneNumber) {
        this.userId = userId;
        this.nameU = nameU;
        this.cityU = cityU;
        this.neighborhoodU = neighborhoodU;
        this.country_chooser = country_chooser;
        this.address= address;
        this.phoneNumber = phoneNumber;
    }

    }