package edu.ukma.restaurant.entity;

import edu.ukma.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> items = new ArrayList<>();

    public Menu() {}
    public Menu(String name) {
        this.name = name;
    }

    public Long getId() {return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public Restaurant getRestaurant(){return restaurant;}
    public void setRestaurant(Restaurant r){this.restaurant=r;}
    public List<MenuItem> getItems(){return items;}
    public void setItems(List<MenuItem> items){this.items=items;}
    public void addItem(MenuItem it){ items.add(it); it.setMenu(this); }
}

