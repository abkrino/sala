package pharamacy.eg.sala;

import pharamacy.eg.sala.Class.Product;

public class Order extends Product {
String PhoneNumberOffices;
String PhoneNumberpharmacy;
int numberOrder;

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

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public Order(String phoneNumberOffices, String phoneNumberpharmacy, int numberOrder) {
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.numberOrder = numberOrder;
    }

    public Order(String price, String phoneNumberOffices, String phoneNumberpharmacy, int numberOrder) {
        super(price);
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.numberOrder = numberOrder;
    }

    public Order(String name, String price, String phoneNumberOffices, String phoneNumberpharmacy, int numberOrder) {
        super(name, price);
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.numberOrder = numberOrder;
    }

    public Order(String name, String price, String discount, String phoneNumberOffices, String phoneNumberpharmacy, int numberOrder) {
        super(name, price, discount);
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.numberOrder = numberOrder;
    }

    public Order(String name_company, String name, String price, String discount, String phoneNumberOffices, String phoneNumberpharmacy, int numberOrder) {
        super(name_company, name, price, discount);
        PhoneNumberOffices = phoneNumberOffices;
        PhoneNumberpharmacy = phoneNumberpharmacy;
        this.numberOrder = numberOrder;
    }
}