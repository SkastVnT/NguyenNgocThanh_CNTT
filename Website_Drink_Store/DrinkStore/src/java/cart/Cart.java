/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cart;

import db.Product;
import db.ProductFacade;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PHT
 */
public class Cart {

    private Map<Integer, Item> map = null;

    public Cart() {
        this.map = new HashMap<>();
    }

    public Collection<Item> getList() {
        return this.map.values();
    }

    public void add(int id, int quantity) throws SQLException {
        if (this.map.containsKey(id)) {
            //neu trong cart da co id thi chi cap nhat quantity
            Item item = this.map.get(id);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            //neu trong cart chua co item thi them moi
            ProductFacade pf = new ProductFacade();
            Product product = pf.select(id);
            Item item = new Item(product, quantity);
            //them item vao map
            this.map.put(id, item);
        }
    }

    public void remove(int id) {
        this.map.remove(id);
    }

    public void update(int id, int quantity) {
        Item item = this.map.get(id);
        item.setQuantity(item.getQuantity() + quantity);
    }
    
    public void empty(){
        this.map.clear();
    }
    
    public double getTotalCost(){
        double total = 0;
        for(Item item: this.map.values()){
            total += item.getCost();
        }
        return total;
    }
}
