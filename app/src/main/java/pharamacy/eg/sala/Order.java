package pharamacy.eg.sala;

import java.util.ArrayList;

public class Order {
    String nameOfCompany;
    String numberOffices;

    String date ;
    ArrayList<ProductOrder> order ;

    public Order(String nameOfCompany, String numberOffices,String date , ArrayList<ProductOrder> order) {
        this.nameOfCompany = nameOfCompany;
        this.numberOffices = numberOffices;
        this.date  = date ;
        this.order = order;
    }

    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public void setNameOfCompany(String nameOfCompany) {
        this.nameOfCompany = nameOfCompany;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOffices() {
        return numberOffices;
    }

    public void setNumberOffices(String numberOffices) {
        this.numberOffices = numberOffices;
    }
    public String getTitle() {
        return nameOfCompany;
    }

    public void setTitle(String title) {
        this.nameOfCompany = title;
    }

    public ArrayList<ProductOrder> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<ProductOrder> order) {
        this.order = order;
    }




}
