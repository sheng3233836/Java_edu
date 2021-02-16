package com.lagou.bean;

public class Book {
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +  '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Book() {
    }

    public Book(Integer id, String name, Float price, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // 图书ID
    private Integer id;
    // 图书名称
    private String name;
    // 图书价格
    private Float price;
    // 图书描述
    private String desc;


}
