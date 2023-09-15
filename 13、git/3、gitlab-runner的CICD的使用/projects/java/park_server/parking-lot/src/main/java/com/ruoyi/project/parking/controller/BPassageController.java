package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.constant.WarnMessageModel;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.MessageFormat;
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

    public static final String PASSAGE_NAME_LABEL = "通道名称";
    public static final String PASSAGE_NO_LABEL = "通道编号";
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
    public AjaxResult add(@RequestBody @Validated BPassage entity) {
        if (bPassageService.countPassageByPassageName(entity.getPassageName(), entity.getParkNo()) > 0) {
            return error(MessageFormat.format(WarnMessageModel.DATABASE_EXIST_TIP, PASSAGE_NAME_LABEL, entity.getPassageName()));
        }
        if (bPassageService.countPassageByPassageNo(entity.getPassageNo(), entity.getParkNo()) > 0) {
            return error(MessageFormat.format(WarnMessageModel.DATABASE_EXIST_TIP, PASSAGE_NO_LABEL, entity.getPassageNo()));
        }
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
        BPassage bPassage = bPassageService.queryById(entity.getId());
        if (!bPassage.getPassageName().equals(entity.getPassageName()) && bPassageService.countPassageByPassageName(entity.getPassageName(), entity.getParkNo()) > 0) {
            return error(MessageFormat.format(WarnMessageModel.DATABASE_EXIST_TIP, PASSAGE_NAME_LABEL, entity.getPassageName()));
        }
        if (!bPassage.getPassageNo().equals(entity.getPassageNo()) && bPassageService.countPassageByPassageNo(entity.getPassageNo(), entity.getParkNo()) > 0) {
            return error(MessageFormat.format(WarnMessageModel.DATABASE_EXIST_TIP, PASSAGE_NO_LABEL, entity.getPassageNo()));
        }
        return toAjax(bPassageService.update(entity));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation("通过主键删除数据")
    @DeleteMapping("/delete")
    public AjaxResult deleteById(Integer id) {
        if (ibPassageDeviceService.existDeviceBindPassage(id)) {
            return error("您未解绑设备，请解绑");
        }
        return toAjax(bPassageService.deleteById(id));
    }
}

