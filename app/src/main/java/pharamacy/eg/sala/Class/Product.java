package pharamacy.eg.sala.Class;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Product implements Comparable {
    String name_company;
    String nameProduct;
    double price;
    double discount;

    public Product() {
    }


    public Product(double price) {
        this.price = price;
    }

    public Product(String name, double price) {
        this.nameProduct = name;
        this.price = price;
    }

    public Product(String name, double price, double discount) {
        //hnaa low fi discount
        this.nameProduct = name;
        this.price = price;
        this.discount = discount;
    }

    public Product(String name_company, String name, double price, double discount) {
        this.name_company = name_company;
        this.nameProduct = name;
        this.price = price;
        this.discount = discount;
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

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (discount !=0.0) {
            result.put("price", price);
            result.put("discount", discount);
        } else {
            result.put("price", price);
        }
        return result;
    }


    @Override
    public int compareTo(Object o) {
        if (discount !=0.0) {
        int comparedis= (int) ((Product)o).getDiscount();
        return (int) (comparedis-this.discount);

    }else {
            int comparePri= (int) ((Product)o).getPrice();
            return (int) (this.price-comparePri);
        }
    }
}
