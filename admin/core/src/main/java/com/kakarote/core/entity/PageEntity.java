package com.kakarote.core.entity;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangzhiwei
 * 分页需继承的类
 */
@Data
public class PageEntity {
    @ApiModelProperty("当前页数")
    private Integer page = 1;

    @ApiModelProperty("每页展示条数")
    private Integer limit = 15;

    @ApiModelProperty(value = "是否分页，0:不分页 1 分页", allowableValues = "0,1")
    private Integer pageType = 1;

    @ApiModelProperty(value = "传输列名")
    private String fieldName;

    @ApiModelProperty(value = "排序方式")
    private String sortingType;
    /**
     * 排序字段信息
     */
    @ApiModelProperty("排序字段")
    private List<OrderItem> orders = new ArrayList<>();

    public <T> BasePage<T> parse() {
        BasePage<T> page = new BasePage<>(getPage(), getLimit());
        if (Objects.equals(0, pageType)) {
            page.setSize(10000);
        }
        List<OrderItem> orders2 = new ArrayList<>();
        if(fieldName!=null&&!"".equals(fieldName)){
            OrderItem  orderItem = new OrderItem();
            orderItem.setColumn(fieldName);
            if(sortingType!=null&&!"".equals(sortingType)){
                if("ASC".equals(sortingType)){
                    orderItem.setAsc(true);
                }else{
                    orderItem.setAsc(false);
                }
            }
            orders2.add(orderItem);
            for(int i=0;i<orders.size();i++){
                orders2.add(orders.get(i));
            }
        }
        if(orders2.size()>0){
            page.addOrderList(orders2);
        }else{
            page.addOrderList(orders);
        }
        return page;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
        if (pageType == 0) {
            limit = 10000;
        }
    }

    public Integer getLimit() {
        if (limit > 100 && 1 == pageType) {
            limit = 100;
        }
        return limit;
    }

    public void setOrdersBd(String column , boolean asc) {
        OrderItem  orderItem = new OrderItem();
        orderItem.setColumn(column);
        orderItem.setAsc(asc);
        orders.add(orderItem);
    }
}
