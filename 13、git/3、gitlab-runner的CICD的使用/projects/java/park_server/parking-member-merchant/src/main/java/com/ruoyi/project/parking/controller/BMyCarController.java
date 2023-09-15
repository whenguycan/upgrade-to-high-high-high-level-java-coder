package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.service.IBMyCarService;
import com.ruoyi.project.parking.service.LotParkingGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * H5我的车辆管理Controller
 *
 * @author fangch
 * @date 2023-03-02
 */
@RestController
@RequestMapping("/parking/BMyCar")
public class BMyCarController extends BaseController {
    @Autowired
    private IBMyCarService bMyCarService;

    @Autowired
    private LotParkingGrpcServiceImpl lotParkingGrpcService;

    /**
     * 查询H5我的车辆管理列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:list')")
    @GetMapping("/list")
    public TableDataInfo list(BMyCar bMyCar) {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bMyCar.setCreateBy(String.valueOf(loginUser.getUserId()));
        List<BMyCar> list = bMyCarService.selectBMyCarList(bMyCar);
        return getDataTable(list);
    }

    /**
     * 导出H5我的车辆管理列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:export')")
//    @Log(title = "H5我的车辆管理", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BMyCar bMyCar)
//    {
//        List<BMyCar> list = bMyCarService.selectBMyCarList(bMyCar);
//        ExcelUtil<BMyCar> util = new ExcelUtil<BMyCar>(BMyCar.class);
//        util.exportExcel(response, list, "H5我的车辆管理数据");
//    }

    /**
     * 获取H5我的车辆管理详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(bMyCarService.selectBMyCarById(id));
    }

    /**
     * 新增H5我的车辆管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:add')")
    @Log(title = "H5我的车辆管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BMyCar bMyCar) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bMyCar.setCreateBy(String.valueOf(loginUser.getUserId()));
        return bMyCarService.insertBMyCar(bMyCar);
    }

    /**
     * 修改H5我的车辆管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:edit')")
    @Log(title = "H5我的车辆管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BMyCar bMyCar) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bMyCar.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(bMyCarService.updateBMyCar(bMyCar));
    }

    /**
     * 设为默认
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:edit')")
    @Log(title = "设为默认", businessType = BusinessType.UPDATE)
    @PostMapping("/setDefault/{id}")
    public AjaxResult setDefault(@PathVariable Integer id) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String userId = String.valueOf(loginUser.getUserId());
        return toAjax(bMyCarService.setDefault(id, userId, userId));
    }

    /**
     * 删除H5我的车辆管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BMyCar:remove')")
    @Log(title = "H5我的车辆管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(bMyCarService.deleteBMyCarByIds(ids));
    }

    /**
     * 查询我使用过的车牌历史记录
     */
    @GetMapping("/history")
    public AjaxResult historyUsed() {
        return success(lotParkingGrpcService.queryCarNumberHistoryUsedByMemberId(getUserId()));
    }
}
