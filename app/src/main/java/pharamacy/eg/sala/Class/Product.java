package pharamacy.eg.sala.Class;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Product {
    String name_company;
    String name;
    String price;
    String discount;


    public Product() {
    }

    public Product(String price) {
        this.price = price;
    }

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, String price, String discount) {
        //hnaa low fi discount
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public Product(String name_company, String name, String price, String discount) {
        this.name_company = name_company;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public String getName_company() {
        return name_company;
    }

    public void setName_company(String name_company) {
        this.name_company = name_company;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (discount != null) {
            result.put("price", price);
            result.put("discount", discount);
        } else {
            result.put("price", price);
        }
        return result;
    }
}
