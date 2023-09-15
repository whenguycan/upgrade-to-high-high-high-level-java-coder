package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.bo.RegularCarBO;
import com.ruoyi.project.parking.domain.param.RegularCarParam;
import com.ruoyi.project.parking.domain.vo.RegularCarVO;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 固定车记录表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/parking/regularCar")
public class RegularCarController extends BaseController {


    @Autowired
    private IRegularCarService regularCarService;

    /**
     * @apiNote 获取固定车记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RegularCar regularCar) {
        startPage();
        List<RegularCar> list = regularCarService.listByParkNoAndCarNumberAndPlaceNoAndOwnerAndCategoryIdAndCarType(regularCar);
        return getDataTable(list);
    }

    /**
     * @apiNote 新增单个固定车记录（线下）
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody RegularCarVO regularCarVO) {
        return toAjax(regularCarService.add(regularCarVO));
    }

//    /**
//     * @apiNote 新增单个固定车记录（线上）
//     */
//    @PostMapping("/add/online")
//    public AjaxResult addOnline(@RequestBody RegularCarVO regularCarVO) {
//        return toAjax(regularCarService.addOnline(regularCarVO));
//    }

    /**
     * @apiNote 编辑单个固定车记录
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody RegularCarParam regularCarParam) {
        return toAjax(regularCarService.editById(regularCarParam));
    }

    /**
     * @apiNote 逻辑删除单个固定车记录
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(regularCarService.removeById(id));
    }

    /**
     * @apiNote 启用单个固定车记录
     */
    @GetMapping("/enable/{id}")
    public AjaxResult enable(@PathVariable @NotNull Integer id) {
        return toAjax(regularCarService.switchStatusById(id, "1"));
    }

    /**
     * @apiNote 禁用单个固定车记录
     */
    @GetMapping("/disable/{id}")
    public AjaxResult disable(@PathVariable @NotNull Integer id) {
        return toAjax(regularCarService.switchStatusById(id, "0"));
    }

    /**
     * @apiNote 固定车列表导入
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport, Integer carCategoryId) throws Exception {
        ExcelUtil<RegularCarBO> util = new ExcelUtil<>(RegularCarBO.class);
        List<RegularCarBO> regularCarList = util.importExcel(file.getInputStream());
        String message = regularCarService.importRegularCar(regularCarList, updateSupport, carCategoryId);
        return success(message);
    }

    /**
     * @apiNote 固定车列表导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("固定车记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        ExcelUtil<RegularCarBO> util = new ExcelUtil<>(RegularCarBO.class);
        util.importTemplateExcel(response, "固定车记录列表");
    }

    /**
     * @apiNote 固定车列表导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, RegularCar regularCar) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("固定车记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        List<RegularCar> list = regularCarService.listByParkNoAndCarNumberAndPlaceNoAndOwnerAndCategoryIdAndCarType(regularCar);
        List<RegularCarBO> exportList = new ArrayList<>();
        list.forEach(car -> {
            RegularCarBO regularCarBO = new RegularCarBO();
            BeanUtils.copyBeanProp(regularCarBO, car);
            // excel不识别LocalDate格式，需要转成Date格式返回
            if (null != car.getStartTime()) {
                regularCarBO.setStartTime(DateLocalDateUtils.localDateToDate(car.getStartTime()));
            }
            if (null != car.getEndTime()) {
                regularCarBO.setEndTime(DateLocalDateUtils.localDateToDate(car.getEndTime()));
            }
            exportList.add(regularCarBO);
        });
        ExcelUtil<RegularCarBO> util = new ExcelUtil<>(RegularCarBO.class);
        util.exportExcel(response, exportList, "固定车记录列表");
    }
}
