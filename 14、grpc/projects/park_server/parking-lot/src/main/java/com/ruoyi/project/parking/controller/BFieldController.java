package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.domain.comboboxdata.ComboboxData;
import com.ruoyi.project.parking.service.BFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域管理表;(b_field)表控制层
 *
 * @author : mzl
 * @date : 2023-2-21
 */
@Api(tags = "区域管理表对象功能接口")
@RestController
@RequestMapping("/bField")
public class BFieldController extends BaseController {
    @Autowired
    private BFieldService bFieldService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("/{id}")
    public ResponseEntity<BField> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bFieldService.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param bfield 筛选条件
     * @param
     * @return 查询结果
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public TableDataInfo paginQuery(BField bfield) {
        startPage();
        List<BField> list = bFieldService.findList(bfield);
        return getDataTable(list);
    }


    /**
     * 分页查询
     *
     * @param
     * @param
     * @return 查询结果
     */
    @ApiOperation("combobox数据源")
    @GetMapping("/fieldComboboxList")
    public List<ComboboxData> fieldComboboxList() {
        List<ComboboxData> comDataList = new ArrayList();
        BField bField = new BField();
        bField.setFieldStatus("1");
        List<BField> list = bFieldService.findList(bField);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(fbField -> {
                ComboboxData comboboxData = new ComboboxData();
                comboboxData.setText(fbField.getFieldName());
                comboboxData.setId(String.valueOf(fbField.getId()));
                comDataList.add(comboboxData);
            });
        }
        return comDataList;
    }

    /**
     * 新增数据
     *
     * @param bfield 实例对象
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BField bfield) {
        return toAjax(bFieldService.insert(bfield));
    }

    /**
     * 更新数据
     *
     * @param bfield 实例对象
     * @return 实例对象
     */
    @ApiOperation("更新数据")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody BField bfield) {
        return toAjax(bFieldService.update(bfield));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation("通过主键删除数据")
    @PostMapping("/remove")
    public AjaxResult deleteById(Integer id) {
        return toAjax(bFieldService.deleteById(id));
    }

}
