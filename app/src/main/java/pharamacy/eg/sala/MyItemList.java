package pharamacy.eg.sala;
public class MyItemList {
    private String discount;
    private String price;

    public MyItemList() {
    }

    public MyItemList(String discount, String price) {
        this.discount = discount;
        this.price = price;
    }

    public String getDiscount() {

        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}