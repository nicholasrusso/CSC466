package RuleMining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by cgels on 10/21/17.
 */
public class ItemSet{
    private ArrayList<Integer> items;

    public ItemSet() {
        this.items = new ArrayList<>();
    }

    public ItemSet(List<Integer> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    public ItemSet(ItemSet toCopy) {
        this.items = new ArrayList<>(toCopy.items);
    }

    public ArrayList<Integer> getItems() {
        return new ArrayList<>(items);
    }

    public Integer getItem(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public boolean contains(Integer item) {
        return items.contains(item);
    }

    public boolean containsSubset(ItemSet itemSet) {
        // if subset larger than this item set it cannot be a subset.
        if (size() < itemSet.size()) {
            return false;
        }

        return this.items.containsAll(itemSet.items);
    }

    public void add(Integer item) {
        items.add(item);
    }




    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemSet itemSet = (ItemSet) o;

        return items.equals(itemSet.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
