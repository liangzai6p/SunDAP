package com.sunyard.dap.dataserve.entity;

public class ZoneCount {
    private String name;
    private String value;
    private String amount;
    private String max;
    private String city_no;
    public ZoneCount(){

    }
    public ZoneCount(String name, String value, String amount, String max, String city_no) {
        this.name = name;
        this.value = value;
        this.amount = amount;
        this.max = max;
        this.city_no = city_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCity_no() {
        return city_no;
    }

    public void setCity_no(String city_no) {
        this.city_no = city_no;
    }

    @Override
    public String toString() {
        return "ZoneCount{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", amount='" + amount + '\'' +
                ", max='" + max + '\'' +
                ", city_no='" + city_no + '\'' +
                '}';
    }
}
