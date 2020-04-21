package pharamacy.eg.sala;

import java.util.ArrayList;

import pharamacy.eg.sala.Class.Product;

public class MyItemList implements Comparable {
    private double discount;
    private double price;
    String nameInList,ownerProduct;
    private String nameCompany;
    private String PhoneNumber;

    public MyItemList(double discount, double price) {
        this.discount = discount;
        this.price = price;
    }

    public MyItemList(double discount, double price,String nameCompany, String nameInList,String phoneNumber, String ownerProduct) {
        this.discount = discount;
        this.price = price;
        this.nameCompany = nameCompany;
        this.nameInList = nameInList;
        PhoneNumber = phoneNumber;
        this.ownerProduct = ownerProduct;
    }

    public String getNameInList() {
        return nameInList;
    }

    public void setNameInList(String nameInList) {
        this.nameInList = nameInList;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getOwnerProduct() {
        return ownerProduct;
    }

    public void setOwnerProduct(String ownerProduct) {
        this.ownerProduct = ownerProduct;
    }


    public MyItemList() {
    }

    public double getDiscount() {

        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int compareTo(Object o) {
        if (discount != 0.0) {
            int comparedis = (int) ((MyItemList) o).getDiscount();
            return (int) (comparedis - this.discount);

        } else {
            int comparePri = (int) ((MyItemList) o).getPrice();
            return (int) (this.price - comparePri);
        }
    }
}