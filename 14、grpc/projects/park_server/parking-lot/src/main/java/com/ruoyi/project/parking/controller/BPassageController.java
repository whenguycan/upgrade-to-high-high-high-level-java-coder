package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.vo.BPassageVo;
import com.ruoyi.project.parking.service.BFieldService;
import com.ruoyi.project.parking.service.BPassageService;
import com.ruoyi.project.parking.service.IBPassageDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通道信息表;(b_passage)表控制层
 *
 * @author : mzl
 * @date : 2023-2-21
 */
@Api(tags = "通道信息表对象功能接口")
@RestController
@RequestMapping("/bPassage")
public class BPassageController extends BaseController {
    @Autowired
    private BPassageService bPassageService;

    @Resource
    private IBPassageDeviceService ibPassageDeviceService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("/{id}")
    public ResponseEntity<BPassage> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bPassageService.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param bPassage 筛选条件
     * @param
     * @return 查询结果
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public TableDataInfo paginQuery(BPassageVo bPassage) {
        startPage();
        return getDataTable(bPassageService.qryList(bPassage));
    }

    /**
     * 新增数据
     *
     * @param entity 实例对象
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BPassage entity) {
        return toAjax(bPassageService.insert(entity));
    }

    /**
     * 更新数据
     *
     * @param entity 实例对象
     * @return 实例对象
     */
    @ApiOperation("更新数据")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody BPassage entity) {
        return toAjax(bPassageService.update(entity));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation("通过主键删除数据")
    @DeleteMapping("/{id}")
    public AjaxResult deleteById(@PathVariable("id") Integer id) {
        if (ibPassageDeviceService.existDeviceBindPassage(id)) {
           return error("您未解绑设备，请解绑");
        }
        return toAjax(bPassageService.deleteById(id));
    }
}

