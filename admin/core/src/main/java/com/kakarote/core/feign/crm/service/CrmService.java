package com.kakarote.core.feign.crm.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "crm")
public interface CrmService {

    /**
     * 查询客户信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmCustomer/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryCustomerInfo(@RequestBody Collection ids);


    /**
     * 查询客户信息(like)
     *
     * @param name 名字
     * @return entity
     */
    @PostMapping("/crmCustomer/queryByNameCustomerInfo")
    public Result<List<SimpleCrmEntity>> queryByNameCustomerInfo(@RequestParam("name") String name);


    /**
     * 查询客户信息(eq)
     *
     * @param name 名字
     * @return entity
     */
    @PostMapping("/crmCustomer/queryNameCustomerInfo")
    public Result<List<SimpleCrmEntity>> queryNameCustomerInfo(@RequestParam("name")String name);

    /**
     * 查询联系人信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmContacts/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryContactsInfo(@RequestBody Collection ids);

    /**
     * 查询商机信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmBusiness/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryBusinessInfo(@RequestBody Collection ids);

    /**
     * 查询合同信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmContract/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryContractInfo(@RequestBody Collection ids);

    @PostMapping("/crmProduct/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryProductInfo();

    /**
     * 添加活动记录
     * @param type
     * @param activityType 活动类型
     * @param activityTypeId  类型ID
     * @return
     */
    @PostMapping("/crmActivity/addActivity")
    Result addActivity(@RequestParam("type") Integer type,@RequestParam("activityType") Integer activityType,@RequestParam("activityTypeId") Integer activityTypeId);

    @PostMapping(value = "/crmField/batchUpdateEsData")
    Result batchUpdateEsData(@RequestParam("id")String id,@RequestParam("name")String name);

    @PostMapping("/crmCustomerJob/putInInternational")
    Result putInInternational();

    @GetMapping("/crmdata/initData/{tenantId}")
    @ApiOperation("初始化CRM数据--新建租户")
    Result initData(@PathVariable("tenantId") Integer tenantId);

    @GetMapping("/crmdata/initDataForUser/{tenantId}/{userId}/{roleId}")
    @ApiOperation("初始化CRM数据--新建租户管理员")
    Result initDataForUser(@PathVariable("tenantId") Integer tenantId, @PathVariable("userId") Integer userId,
                           @PathVariable("roleId") Integer roleId);

    @GetMapping("/crmField/initDataToEs")
    @ApiOperation("登录后保存CRM自定义字段数据至es")
    Result initFieldDataToEs();
}
