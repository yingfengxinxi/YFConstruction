package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.build.labour.service.IBLabourAttendanceService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.build.safety.service.IBSafetySafetyTrainingService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 考勤管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/bLabourAttendance")
public class BLabourAttendanceController {


    @Autowired
    private IBLabourAttendanceService iBLabourAttendanceService;


    @Autowired
    private IBLabourAttendanceDetailService iBLabourAttendanceDetailService;



    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourAttendanceBO>> list(@RequestBody BLabourAttendanceBO bLabourAttendanceBO)
    {
        BasePage<BLabourAttendanceBO> list = iBLabourAttendanceService.selectList(bLabourAttendanceBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourAttendance bLabourAttendance)
    {
        String falg=iBLabourAttendanceService.add(bLabourAttendance);

        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBLabourAttendanceService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourAttendance bLabourAttendance)
    {

        String falg= iBLabourAttendanceService.edit(bLabourAttendance);
        return Result.ok();
    }

    /**
     * 数据添加前验证
     */
    @PostMapping("/validation")
    @ApiOperation("数据验证")
    @OperateLog(detail = "数据验证",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result validation(@Validated @RequestBody BLabourAttendance bLabourAttendance)
    {

        String falg= iBLabourAttendanceService.validation(bLabourAttendance);
        ;
        return Result.ok().setData(falg);
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {

        iBLabourAttendanceDetailService.deletePid(id);
        return Result.ok( iBLabourAttendanceService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBLabourAttendanceService.removeById(id);
        }
        return Result.ok();
    }

    @RequestMapping("/ybxz")
    public void ybxz(HttpServletRequest request, HttpServletResponse response){

        String fileName = "attendance_import.xlsx";
        File file = new File("D:/zhgd/kq/attendance_import.xlsx");
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            String tempFileName= "";
            try {
                tempFileName = URLEncoder.encode(fileName, "UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
            response.setHeader("Content-Disposition", "attachment;filename="+tempFileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}

