package com.qdoner.datarecept.resolver.equipment.elevator.resolver;

import com.alibaba.fastjson.JSONObject;
import com.qdoner.datarecept.resolver.equipment.elevator.service.ElevatorService;
import com.qdoner.datarecept.resolver.equipment.tower.service.AnalysisService;
import com.qdoner.datarecept.utils.CRCUtils;
import com.qdoner.datarecept.utils.FeelTheBase;
import com.qdoner.datarecept.utils.HexEcodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 济南拓兴升降机  16进制交互
 */
@Component
@Slf4j
public class ElevatorResolver_JNTX {
    /**
     * 预留：校验数据  长度、校验码
     *
     * @param msg
     * @return
     */
    public boolean checkData(String msg) {

        return true;
    }

    /**
     * 获取设备编码
     *
     * @param msg
     * @return
     */
    public String codeResolver(String msg) {
        //设备编号
        String assetNum = msg.substring(8, 14);
        return assetNum;
    }

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private AnalysisService analysisService;
    /**
     * 数据处理（业务层）
     *
     * @param msg
     * @param asset 设备信息
     * @return
     */
    public void dataResolver(String msg, JSONObject asset) throws Exception {
        //判断命令字
        String mlz = msg.substring(4, 6);
        //截取信息体
        if (!"10".equals(mlz) &&
                !"11".equals(mlz) &&
                !"12".equals(mlz) &&
                !"13".equals(mlz)&&
                !"0C".equals(mlz) &&
                !"31".equals(mlz) &&
                !"20".equals(mlz) &&
                !"3D".equals(mlz)) {
            return;
        }
        String body = msg.substring(14, msg.length() - 1);//去头 帧头2、帧类型1、塔吊编号1、设备编号3
        body = body.substring(0, body.length() - 11);//去尾(不包含) 校验1、长度1、帧尾4
        //解析信息体
        if ("10".equals(mlz)) {//实时
            elevatorService.realTimeData(body, asset, false);
        }
        if ("11".equals(mlz)) {//报警
            elevatorService.realTimeData(body, asset, true);
        }
        if ("12".equals(mlz)) {//人员
            elevatorService.staffInfo(body, asset);
        }
        if ("13".equals(mlz)) {//工作循环
            elevatorService.workCycle(body, asset);
        }
        if ("0C".equals(mlz)) {//塔机实时数据
            analysisService.realTimeData(HexEcodeUtil.hex2byte(msg));
        }
        if ("31".equals(mlz)) {//塔机信息
            analysisService.basicsInfo(HexEcodeUtil.hex2byte(msg));
        }
        if ("3D".equals(mlz)) {//塔机工作循环
            analysisService.workCycle(HexEcodeUtil.hex2byte(msg));
        }
        if ("20".equals(mlz)) {//图片
            elevatorService.staffImg(body, asset);
        }
    }

    /**
     * 组装响应信息
     *
     * @param str
     * @return
     */
    public String responseMsg(String str) throws Exception {
        //帧头
        String res = null;
        if("A55A".equals(str.substring(0, 4))){
            if("01".equals(str.substring(4, 6))){
                SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
                Date date = new Date();
                String time = ft.format(date);
                String huifu = str.substring(0, 4) + "02" + str.substring(6, 14) + "01" + time + "0F";
                res = huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
            }else if("60".equals(str.substring(4, 6))){
                SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
                Date date = new Date();
                String time = ft.format(date);
                String huifu = str.substring(0, 4) + "61" + str.substring(6, 14) + "01" + time + "0F";
                res =  huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
            }
        }
        return res;
    }
}
