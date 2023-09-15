package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.WhiteList;
import com.ruoyi.project.parking.domain.bo.WhiteListBO;
import com.ruoyi.project.parking.domain.param.WhiteListParam;
import com.ruoyi.project.parking.domain.vo.WhiteListVO;
import com.ruoyi.project.parking.service.IWhiteListService;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 白名单表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/27 17:22
 */
@RestController
@RequestMapping("/parking/whiteList")
public class WhiteListController extends BaseController {


    @Autowired
    private IWhiteListService whiteListService;

    /**
     * @apiNote 获取白名单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WhiteList whiteList) {
        startPage();
        List<WhiteList> list = whiteListService.listByParkNoAndCarNumber(whiteList);
        return getDataTable(list);
    }

    /**
     * @apiNote 新增单个白名单
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WhiteListVO whiteListVO) {
        return toAjax(whiteListService.add(whiteListVO));
    }


    /**
     * @apiNote 编辑单个白名单
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody WhiteListParam whiteListParam) {
        return toAjax(whiteListService.editById(whiteListParam));
    }

    /**
     * @apiNote 物理删除单个白名单
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(whiteListService.removeById(id));
    }

    /**
     * @apiNote 白名单列表导入
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<WhiteListBO> util = new ExcelUtil<WhiteListBO>(WhiteListBO.class);
        List<WhiteListBO> whiteListBOList = util.importExcel(file.getInputStream());
        String message = whiteListService.importWhiteList(whiteListBOList, updateSupport);
        return success(message);
    }

    /**
     * @apiNote 白名单列表导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("白名单列表" + LocalDate.now() + " " + LocalTime.now().toString(), StandardCharsets.UTF_8) + ".xlsx");
        ExcelUtil<WhiteListBO> util = new ExcelUtil<>(WhiteListBO.class);
        util.importTemplateExcel(response, "白名单列表");
    }

    /**
     * @apiNote 白名单列表导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, WhiteList whiteList) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("固定车记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        List<WhiteList> list = whiteListService.listByParkNoAndCarNumber(whiteList);
        List<WhiteListBO> exportList = new ArrayList<>();
        list.forEach(item -> {
            WhiteListBO whiteListBO = new WhiteListBO();
            BeanUtils.copyBeanProp(whiteListBO, item);
            whiteListBO.setCreateTime(DateLocalDateUtils.localDateTimeToDate(item.getCreateTime()));
            exportList.add(whiteListBO);
        });
        ExcelUtil<WhiteListBO> util = new ExcelUtil<>(WhiteListBO.class);
        util.exportExcel(response, exportList, "白名单列表");
    }
}
