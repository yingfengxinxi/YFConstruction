package com.qdoner.datarecept.controller;


import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.BO.EquipmentReceptPlatformBO;
import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import com.qdoner.datarecept.server.TcpServer;
import com.qdoner.datarecept.server.UdpServer;
import com.qdoner.datarecept.service.IEquipmentReceptPlatformService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备数据对接平台 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/receptPlatform")
public class ReceptConfigController {

    @Autowired
    private IEquipmentReceptPlatformService receptPlatformService;

    @PostMapping("/list")
    @ApiOperation("查询设备数据对接平台配置")
    @OperateLog(detail = "查询设备数据对接平台配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getList(@RequestBody EquipmentReceptPlatformBO receptPlatformBO) {
        return Result.ok(receptPlatformService.selectByPage(receptPlatformBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新设备数据对接平台配置")
    @OperateLog(detail = "保存或更新设备数据对接平台配置", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EquipmentReceptPlatform receptPlatform) {
        if (receptPlatform.getId() == null) {//校验port
            String type = receptPlatform.getType();
            if ("1".equals(type)) {
                Integer port = receptPlatform.getPort();
                List<EquipmentReceptPlatform> list = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getPort, port).list();
                if (list.size() > 0) {
                    return Result.error(-1, port + "端口被占用");
                }
            }
        }
        receptPlatformService.saveOrUpdate(receptPlatform);
        return Result.ok();
    }

    /**
     * 查询设备数据对接平台配
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询设备数据对接平台配置")
    @OperateLog(detail = "查询设备数据对接平台配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id) {
        return Result.ok(receptPlatformService.getById(id));
    }

    /**
     * 删除设备数据对接平台配置
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除设备数据对接平台配置")
    @OperateLog(detail = "删除设备数据对接平台配置", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            receptPlatformService.removeById(id);
        }
        return Result.ok();
    }

    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private UdpServer udpServer;

    @PostMapping("/changeStatus")
    @ApiOperation("数据对接配置启用/停用")
    @OperateLog(detail = "数据对接配置启用/停用", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result changeStatus(@RequestBody EquipmentReceptPlatform receptPlatform) {
        EquipmentReceptPlatform old = receptPlatformService.getById(receptPlatform.getId());
        Integer port = old.getPort();
        //
        if (port != null) {
            //解除监听port
            tcpServer.closeChannels(port);
            udpServer.closeChannels(port);
        }
        if ("1".equals(receptPlatform.getStatus())) {//启用
            if ("1".equals(old.getType())) {
                //绑定监听port
                tcpServer.createChannel(port, old.getMsgScale());
            }
            if ("2".equals(old.getType())) {
                //绑定监听port
                udpServer.createChannel(port);
            }
        }
        return Result.ok(receptPlatformService.updateById(receptPlatform));
    }
}

