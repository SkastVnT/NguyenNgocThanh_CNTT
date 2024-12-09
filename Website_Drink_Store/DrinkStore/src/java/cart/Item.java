/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cart;

import db.Product;

/**
 *
 * @author PHT
 */
public class Item {
    private Product product;
    private int quantity;
    
    public Item() {
    }

    public Item(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getOldPrice(){
        return this.product.getPrice();
    }
    
    public double getDiscount(){
        return this.product.getDiscount();
    }
    
    public double getNewPrice(){
        return this.product.getPrice()*(1-this.product.getDiscount());
    }
    
    public double getCost(){
        return this.getNewPrice()*this.quantity;
    }
}
