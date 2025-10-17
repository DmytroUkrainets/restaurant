package edu.ukma.restaurant.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuItem(){}
    public MenuItem(String name, String desc, BigDecimal price){
        this.name=name; this.description=desc; this.price=price;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getDescription(){return description;}
    public void setDescription(String d){this.description=d;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal p){this.price=p;}
    public boolean isAvailable(){return available;}
    public void setAvailable(boolean a){this.available=a;}
    public Menu getMenu(){return menu;}
    public void setMenu(Menu menu){this.menu=menu;}
}

