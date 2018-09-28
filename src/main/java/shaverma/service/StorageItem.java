package shaverma.service;

public class StorageItem {

    private int id;
    private String name;
    private int amount;
    private int price;

    public StorageItem() {
        id = 0;
        name = "";
        amount = 0;
        price = 0;
    }

    public StorageItem(int id, String name, int amount, int price) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
