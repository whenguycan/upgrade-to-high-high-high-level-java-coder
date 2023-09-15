package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderRequestVO;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 交易记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/parking/parkorderrecords")
public class ParkOrderRecordsController extends BaseController {


    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;


    /**
     * 查询 交易记录 通过 条件
     *
     * @param carNumber 车牌号
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam String carNumber) {
        String parkNo = getParkNO();
        SearchOrderRequestVO param = new SearchOrderRequestVO();
        param.setPaymetnod(1);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        param.setPageNum(pageDomain.getPageNum());
        param.setPageSize(pageDomain.getPageSize());
        param.setCarNumber(carNumber);
        param.setOrderNo(parkNo);
        return AjaxResult.success(parkingOrderGrpcService.queryOrderListWithPage(param));
    }

}
