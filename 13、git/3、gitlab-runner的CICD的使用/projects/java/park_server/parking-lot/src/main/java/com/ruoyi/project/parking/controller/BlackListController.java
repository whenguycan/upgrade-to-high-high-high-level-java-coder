package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BlackList;
import com.ruoyi.project.parking.domain.bo.BlackListBO;
import com.ruoyi.project.parking.domain.param.BlackListParam;
import com.ruoyi.project.parking.domain.vo.BlackListVO;
import com.ruoyi.project.parking.service.IBlackListService;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/parking/blackList")
public class BlackListController extends BaseController {


    @Autowired
    private IBlackListService blackListService;

    /**
     * @apiNote 获取黑名单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlackList blackList) {
        startPage();
        List<BlackList> list = blackListService.listByParkNoAndCarNumber(blackList);
        return getDataTable(list);
    }

    /**
     * @apiNote 新增单个黑名单
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BlackListVO blackListVO) {
        return toAjax(blackListService.add(blackListVO));
    }

    /**
     * @apiNote 编辑单个黑名单
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BlackListParam blackListParam) {
        return toAjax(blackListService.editById(blackListParam));
    }

    /**
     * @apiNote 物理删除单个黑名单
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(blackListService.removeById(id));
    }

    /**
     * @apiNote 黑名单导入
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<BlackListBO> util = new ExcelUtil<>(BlackListBO.class);
        List<BlackListBO> regularCarList = util.importExcel(file.getInputStream());
        String message = blackListService.importBlackList(regularCarList, updateSupport);
        return success(message);
    }

    /**
     * @apiNote 黑名单导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("黑名单列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        ExcelUtil<BlackListBO> util = new ExcelUtil<>(BlackListBO.class);
        util.importTemplateExcel(response, "固定车记录列表");
    }

    /**
     * @apiNote 黑名单导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlackList blackList) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("黑名单列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        List<BlackList> list = blackListService.listByParkNoAndCarNumber(blackList);
        List<BlackListBO> exportList = new ArrayList<>();
        list.forEach(item -> {
            BlackListBO blackListBO = new BlackListBO();
            BeanUtils.copyBeanProp(blackListBO, item);
            blackListBO.setCreateTime(DateLocalDateUtils.localDateTimeToDate(item.getCreateTime()));
            exportList.add(blackListBO);
        });
        ExcelUtil<BlackListBO> util = new ExcelUtil<>(BlackListBO.class);
        util.exportExcel(response, exportList, "黑名单列表");
    }
}
