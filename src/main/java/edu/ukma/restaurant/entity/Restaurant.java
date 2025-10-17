package edu.ukma.restaurant.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @Enumerated(EnumType.STRING)
    private Status status = Status.CLOSED;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    public enum Status { OPEN, CLOSED }

    public Restaurant() {}
    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
        this.status = Status.CLOSED;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    public Status getStatus(){return status;}
    public void setStatus(Status status){this.status=status;}
    public List<Menu> getMenus(){return menus;}
    public void setMenus(List<Menu> menus){this.menus=menus;}

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.setRestaurant(this);
    }
    public void removeMenu(Menu menu) {
        menus.remove(menu);
        menu.setRestaurant(null);
    }
}

