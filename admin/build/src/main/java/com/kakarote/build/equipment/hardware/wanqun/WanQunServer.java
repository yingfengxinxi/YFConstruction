package com.kakarote.build.equipment.hardware.wanqun;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.core.config.MybatisPlusConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Service
public class WanQunServer {
    final String Authorization = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Nzk2NDE0MDksInVzZXJuYW1lIjoiYWRtaW5pc3RlciJ9.Jn5xPcZ8ZKzdfnkvLuWK6OKVCsjp9oL-EBr_-ri-0ucVaGg8g9VJgrlozHoNWD-19TiWNQHwFz7rhHTIPyPyUKkiTz7dC8HKWLA3B5ptd8NiLLFRlyvNnJbGoVwnZVJzRseFXvolPszHnuhqPbQyOwvjVd-ssPuL5o2fK7J9w59yLtQAxUygVjUsmalGWSr9T2TXt06LDOeflqsgvFPcENnCU-vUKUePnLuv5p9yZcsiOPP3OIWknaV5NkvBel7pq3xp68tx0KG6FU0YH_UhjkWC8Zw02TTVJJnPXmUIwz41Kzr5THu7KxdXdYmo8K1wR_5BsH15jVe__7BTEsMDAw";

    final String OnOffUrl = "http://139.9.114.237:8002/service-relay-forward/v1/relay/qingjian/sendOnOff";

    final String RelayStateUrl = "http://139.9.114.237:8002/service-relay/v1/RelayCommand/getRelayState";

    final Integer relayNum = 4;

    @Autowired
    private IEquipmentAssetsService assetsService;

    /**
     * 设备状态
     * select a.*
     * from b_equipment_type a
     * left join b_equipment_assets b on a.type_id = b.type_id
     * where a.monitor_type = '98' and a.project_id = 73 and a.tenant_id is not null
     */
    //@PostConstruct
    public void refreshRelayState() {
        List<Map<String, Object>> list = assetsService.selectSprayList();
        for (Map<String, Object> asset : list) {
            Map<String, String> params = new HashMap<>();
            params.put("relayCode", asset.get("assetCode").toString());
            try {
                HttpClientResult result = HttpClientUtils.doPost(RelayStateUrl, getHeader(), params);
                JSONObject resultData = result.getContent();
                Integer code = resultData.getInteger("code");
                EquipmentAssets assets = new EquipmentAssets();
                assets.setAssetId(Integer.parseInt(asset.get("assetId").toString()));
                if (code == 200) {
                    assets.setOfflineStatus("1");
                } else {
                    assets.setOfflineStatus("0");
                }
                assets.setOfflineDate(new Date());
                assetsService.updateById(assets);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启全部开关
     *
     * @param assets
     */
    public void startAll(EquipmentAssets assets) {
        for (int i = 0; i < relayNum; i++) {
            start(assets, i + 1);
        }
    }

    /**
     * 打开开关
     * {
     * "serialNumber":"160617090700",
     * "openStatus":"1",
     * "relayLocation":1
     * }
     *
     * @param assets        设备信息
     * @param relayLocation 开关标记
     */
    public void start(EquipmentAssets assets, Integer relayLocation) {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("serialNumber", assets.getPlatformEqCode());
            params.put("openStatus", "1");
            params.put("relayLocation", relayLocation.toString());
            HttpClientUtils.doPost(OnOffUrl, getHeader(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭全部开关
     *
     * @param assets
     */
    public void stopAll(EquipmentAssets assets) {
        for (int i = 0; i < relayNum; i++) {
            stop(assets, i + 1);
        }
    }

    /**
     * 关闭开关
     *
     * @param assets        设备信息
     * @param relayLocation 开关标记
     */
    public void stop(EquipmentAssets assets, Integer relayLocation) {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("serialNumber", assets.getPlatformEqCode());
            params.put("openStatus", "2");
            params.put("relayLocation", relayLocation.toString());
            HttpClientUtils.doPost(OnOffUrl, getHeader(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", Authorization);
        return header;
    }

    public static void main(String[] args) {
        File filePath = new File("C:\\Users\\Lenovo\\Desktop\\s_my_");
        File[] fn = filePath.listFiles();
        for (File file : fn) {
            String fullFileName = file.getName();
            String index = fullFileName.substring(0,2);
            String fileName = fullFileName.substring(0, fullFileName.lastIndexOf("."));
            String batchId = UUID.randomUUID().toString().replace("-", "");
            System.out.println("#记录 =>" + index );
            String infoSql = "INSERT INTO `yijiaren`.`e_actv_activity` ( `class_id`, `topic_ids`, `title`, `text`, `approval_status`, `cover_id`, `attachment_ids`, `activity_start_time`, `activity_end_time`, `activity_location`, `community_id`, `release_time`, `display_position`, `is_head_of_the`, `is_show_home`, `show_home_sort`, `create_by`, `create_time`, `update_by`, `update_time`, `remarks`, `del_flag`, `tenant_id`)" +
                    " VALUES ( '27', ',1,3,4,', '" + fileName + "', '<p>" + fileName + "</p>', '2', NULL, '" + batchId + "', NULL, NULL, NULL, ',0,', '2023-05-15 11:22:09', '5', '0', '0', NULL, '14773', '2023-05-16 11:22:09', '14773', '2023-05-16 14:26:12', NULL, '0', '1');";
            System.out.println(infoSql);
            System.out.println("#视频附件 =>" + index );
            String vedioSql = "INSERT INTO `yijiaren`.`wk_admin_file` (`file_id`, `name`, `size`, `create_user_id`, `create_time`, `path`, `file_type`, `type`, `source`, `is_public`, `batch_id`, `tenant_id`) VALUES " +
                    "('" + MybatisPlusConfig.getNextId() + "', '" + fileName + ".mp4', '" + file.length() + "', '14773', '2023-05-16 11:20:09.837000', 'https://223.99.217.138:53018/static/yijiaren/zuqiu/live/" + index + ".mp4', 'mp4', '2', NULL, '0', '" + batchId + "', '1');";
            System.out.println(vedioSql);
            System.out.println("#封面附件 =>" + index );
            String fmSql = "INSERT INTO `yijiaren`.`wk_admin_file` (`file_id`, `name`, `size`, `create_user_id`, `create_time`, `path`, `file_type`, `type`, `source`, `is_public`, `batch_id`, `tenant_id`) VALUES " +
                    "('" + MybatisPlusConfig.getNextId() + "', 'fm.jpg', '52', '14773', '2023-05-16 10:30:36.193000', 'https://yfamily1.oss-cn-qingdao.aliyuncs.com/img/fm.jpg', 'img', '2', NULL, '0', '" + batchId + "', '1');";
            System.out.println(fmSql);
        }
    }
}
