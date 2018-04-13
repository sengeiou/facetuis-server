package com.facetuis.server.service.pinduoduo.response;

public class OrderDetail {
    private String order_sn;
    private int goods_id;
    private String goods_name;
    private String goods_thumbnail_url;
    private int goods_quantity;
    private int goods_price;
    private int order_amount;
    private long order_create_time;
    private long order_verify_time;
    private long order_pay_time;
    private long promotion_rate;
    private long promotion_amount;
    private int order_status;
    private String order_status_desc;
    private String order_group_success_time;
    private String order_modify_at;
    private String p_id;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_thumbnail_url() {
        return goods_thumbnail_url;
    }

    public void setGoods_thumbnail_url(String goods_thumbnail_url) {
        this.goods_thumbnail_url = goods_thumbnail_url;
    }

    public int getGoods_quantity() {
        return goods_quantity;
    }

    public void setGoods_quantity(int goods_quantity) {
        this.goods_quantity = goods_quantity;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(int order_amount) {
        this.order_amount = order_amount;
    }

    public long getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(long order_create_time) {
        this.order_create_time = order_create_time;
    }

    public long getOrder_verify_time() {
        return order_verify_time;
    }

    public void setOrder_verify_time(long order_verify_time) {
        this.order_verify_time = order_verify_time;
    }

    public long getOrder_pay_time() {
        return order_pay_time;
    }

    public void setOrder_pay_time(long order_pay_time) {
        this.order_pay_time = order_pay_time;
    }

    public long getPromotion_rate() {
        return promotion_rate;
    }

    public void setPromotion_rate(long promotion_rate) {
        this.promotion_rate = promotion_rate;
    }

    public long getPromotion_amount() {
        return promotion_amount;
    }

    public void setPromotion_amount(long promotion_amount) {
        this.promotion_amount = promotion_amount;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getOrder_status_desc() {
        return order_status_desc;
    }

    public void setOrder_status_desc(String order_status_desc) {
        this.order_status_desc = order_status_desc;
    }

    public String getOrder_group_success_time() {
        return order_group_success_time;
    }

    public void setOrder_group_success_time(String order_group_success_time) {
        this.order_group_success_time = order_group_success_time;
    }

    public String getOrder_modify_at() {
        return order_modify_at;
    }

    public void setOrder_modify_at(String order_modify_at) {
        this.order_modify_at = order_modify_at;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
}
