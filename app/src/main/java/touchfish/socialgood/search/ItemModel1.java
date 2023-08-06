package touchfish.socialgood.search;

import java.io.Serializable;

public class ItemModel1 implements Serializable {
    private int id;
    private String itemName;
    private Double itemPrice;
    private int itemSurplus;
    private boolean isDeleted =false;
    private String itemInfo;
    private int itemFirstImg;
    private int[] itemDetailImg;
    private String publisher;
    private int publisherId;

    public ItemModel1() {
    }

    public ItemModel1(String itemName, Double itemPrice){
        this.id=id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemSurplus = itemSurplus;
        this.itemInfo = itemInfo;
        this.publisher = publisher;
        this.publisherId = publisherId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemSurplus() {
        return itemSurplus;
    }

    public void setItemSurplus(int itemSurplus) {
        this.itemSurplus = itemSurplus;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public int getItemFirstImg() {
        return itemFirstImg;
    }

    public void setItemFirstImg(int itemImg) {
        this.itemFirstImg = itemImg;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int[] getItemDetailImg() {
        return itemDetailImg;
    }

    public void setItemDetailImg(int[] itemDetailImg) {
        this.itemDetailImg = itemDetailImg;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
