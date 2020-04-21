package pharamacy.eg.sala;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pharamacy.eg.sala.Class.Product;

public class ProductOrder {
    private String PhoneNumberOffices;
    private String PhoneNumberpharmacy;
    private String name_company;

    private String nameProduct;
    private double price;
    private double discount;

    public ProductOrder(String nameProduct, double price, double discount) {
        this.nameProduct = nameProduct;
        this.price = price;
        this.discount = discount;
    }

    public ProductOrder(String phoneNumberOffices, String phoneNumberpharmacy, String name_company, String nameProduct, double price, double discount) {
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.name_company = name_company;
        this.nameProduct = nameProduct;
        this.price = price;
        this.discount = discount;
    }

    public String getPhoneNumberOffices() {
        return PhoneNumberOffices;
    }

    public void setPhoneNumberOffices(String phoneNumberOffices) {
        PhoneNumberOffices = phoneNumberOffices;
    }

    public String getPhoneNumberpharmacy() {
        return PhoneNumberpharmacy;
    }

    public void setPhoneNumberpharmacy(String phoneNumberpharmacy) {
        PhoneNumberpharmacy = phoneNumberpharmacy;
    }



    public String getName_company() {
        return name_company;
    }

    public void setName_company(String name_company) {
        this.name_company = name_company;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (discount != 0.0d) {
            result.put("product name",nameProduct);
            result.put("price", price);
            result.put("discount", discount);
        } else {
            result.put("price", price);
        }
        return result;
    }
}