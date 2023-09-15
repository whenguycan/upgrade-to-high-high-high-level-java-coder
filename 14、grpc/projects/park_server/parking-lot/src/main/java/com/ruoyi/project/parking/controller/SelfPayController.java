package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.SelfPay;
import com.ruoyi.project.parking.domain.bo.SelfPayBO;
import com.ruoyi.project.parking.domain.vo.SelfPayVO;
import com.ruoyi.project.parking.service.ISelfPayService;
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
 * 自主缴费审核 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/1 10:31
 */
@RestController
@RequestMapping("/parking/selfPay")
public class SelfPayController extends BaseController {


    @Autowired
    private ISelfPayService selfPayService;

    /**
     * @apiNote 获取自主缴费审核列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SelfPay selfPay) {
        startPage();
        List<SelfPay> list = selfPayService.listByParkNoAndCarNumberAndStatusAndCreateTime(selfPay);
        return getDataTable(list);
    }

    /**
     * @apiNote 申请自主缴费
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SelfPayVO selfPayVO) {
        return toAjax(selfPayService.add(selfPayVO));
    }


    /**
     * @apiNote 审核通过自主缴费
     */
    @GetMapping("/agree/{id}")
    public AjaxResult agree(@PathVariable @NotNull Integer id) {
        return toAjax(selfPayService.switchStatusById(id, "1"));
    }

    /**
     * @apiNote 审核不通过自主缴费
     */
    @GetMapping("/disagree/{id}")
    public AjaxResult disagree(@PathVariable @NotNull Integer id) {
        return toAjax(selfPayService.switchStatusById(id, "2"));
    }

    /**
     * @apiNote 自主缴费列表导入
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport, String settingRegularCarCategoryId) throws Exception {
        ExcelUtil<SelfPayBO> util = new ExcelUtil<>(SelfPayBO.class);
        List<SelfPayBO> selfPayBOList = util.importExcel(file.getInputStream());
        String message = selfPayService.importSelfPay(selfPayBOList, updateSupport, settingRegularCarCategoryId);
        return success(message);
    }

    /**
     * @apiNote 自主缴费列表导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("自主缴费审核记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        ExcelUtil<SelfPayBO> util = new ExcelUtil<>(SelfPayBO.class);
        util.importTemplateExcel(response, "自主缴费审核记录列表");
    }

    /**
     * @apiNote 自主缴费列表导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, SelfPay selfPay) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("自主缴费审核记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        List<SelfPay> list = selfPayService.listByParkNoAndCarNumberAndStatusAndCreateTime(selfPay);
        List<SelfPayBO> exportList = new ArrayList<>();
        list.forEach(item -> {
            SelfPayBO selfPayBO = new SelfPayBO();
            BeanUtils.copyBeanProp(selfPayBO, item);
            selfPayBO.setApplyTime(DateLocalDateUtils.localDateTimeToDate(item.getApplyTime()));
            exportList.add(selfPayBO);
        });
        ExcelUtil<SelfPayBO> util = new ExcelUtil<>(SelfPayBO.class);
        util.exportExcel(response, exportList, "自主缴费审核记录列表");
    }
}
