package edu.ukma.restaurant.dto;

import java.math.BigDecimal;

public class MenuItemDto {
    private Long id;
    private Long menuId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available = true;

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Long getMenuId(){return menuId;}
    public void setMenuId(Long menuId){this.menuId=menuId;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal price){this.price=price;}
    public boolean isAvailable(){return available;}
    public void setAvailable(boolean available){this.available=available;}
}
