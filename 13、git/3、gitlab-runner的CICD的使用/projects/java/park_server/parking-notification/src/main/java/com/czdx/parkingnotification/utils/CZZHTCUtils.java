package com.czdx.parkingnotification.utils;

import com.alibaba.fastjson2.JSON;
import com.czdx.parkingnotification.common.constant.CacheConstants;
import com.czdx.parkingnotification.domain.BField;
import com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse;
import com.czdx.parkingnotification.domain.czzhtc.request.*;
import com.czdx.parkingnotification.mapper.HomePageMapper;
import com.czdx.parkingnotification.service.IBFieldService;
import com.czdx.parkingnotification.system.domain.SysDept;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class CZZHTCUtils {
    //停车场应用标识,由云平台统一分配
    private static String appId;

    //密钥，由云平台统一分配
    private static String secretKey;

    //上行服务心跳检测状态
    public static boolean flag = false;

    //region 常州智慧停车云平台接口URL
    //常州智慧停车云平台地址
    public static String CZZHTC_ADDRESS = "";

    //车场信息同步接口地址
    public static final String SYNC_PARK_INFO_URL = CZZHTC_ADDRESS + "/park/upsert.htm";

    //空车位数上传接口地址
    public static final String EMPTY_SPACE_RECORD_URL = CZZHTC_ADDRESS + "/record/empty.htm";

    //进出记录上传接口地址
    public static final String TURNOVER_RECORD_URL = CZZHTC_ADDRESS + "/record/upsert.htm";

    //收费记录上传接口地址
    public static final String BILL_RECORD_URL = CZZHTC_ADDRESS + "/bill/upsert.htm";

    //进出记录图片上传接口地址
    public static final String TURNOVER_IMAGE_RECORD_URL = CZZHTC_ADDRESS + "//record/img.htm";

    //心跳检测接口地址
    public static final String HEART_URL = CZZHTC_ADDRESS + "/server/heart.htm";
    //endregion

    /**
     * 车场空位 redis key
     */
    public static final String PARKNO_ACCOUNT_KEY = "parkno_account:";

    @Autowired
    Environment environment;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisCache redisCache;

    @Autowired
    HomePageMapper homePageMapper;

    @Autowired
    IBFieldService fieldService;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @PostConstruct
    void initConfig() {
        log.info("初始化云平台配置");
        appId = environment.getProperty("czzhtc.appId");
        secretKey = environment.getProperty("czzhtc.secretKey");
        CZZHTC_ADDRESS = environment.getProperty("czzhtc.address");
        log.info("初始化云平台配置成功");
//        log.info("启动心跳检测线程");
//        executorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                heart();
//            }
//        }, 0, 1, TimeUnit.MINUTES);
//        log.info("启动心跳检测线程成功");
//        log.info("启动空车位数上传线程");
//        executorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                //获取空车位数和总车位数，构建空车位数上传请求参数
//                EmptySpaceRecordRequest request = new EmptySpaceRecordRequest();
//                //空车位数
//                BigDecimal spaceCount = BigDecimal.ZERO;
//                List<SysDept> parks = homePageMapper.getAllParkIds();
//                for (SysDept park : parks) {
//                    spaceCount = spaceCount.add(BigDecimal.valueOf(redisCache.getCacheObject(PARKNO_ACCOUNT_KEY + park.getParkNo())));
//                }
//                request.setSpaceCount(spaceCount.intValue());
//                //总车位数
//                BigDecimal totalCount = BigDecimal.ZERO;
//                BField field = new BField();
//                field.setFieldStatus("1");
//                List<BField> fieldList = fieldService.findList(field, parks.stream().map(SysDept::getParkNo).toList());
//                for (BField bField : fieldList) {
//                    totalCount = totalCount.add(BigDecimal.valueOf(bField.getSpaceCount()));
//                }
//                request.setTotalCount(totalCount.intValue());
//                emptySpaceRecord(request);
//            }
//        }, 0, 30, TimeUnit.SECONDS);
//        log.info("启动空车位数上传线程成功");
    }

    /**
     * @param timestamp 时间戳，单位秒
     * @param data      需要签名的数据
     * @return 签名
     * @apiNote 常州智慧停车云平台签名算法
     * @author 琴声何来
     * @since 2023/3/31 15:41
     */
    public String sign(String timestamp, String data) {
        //固定参数
        Map<String, String> paramMap = Map.of("appid", appId, "timestamp", timestamp, "version", "1.0");
        //MD5数据源
        Map<String, String> signMap = new TreeMap<>(paramMap);
        signMap.put("data", URLEncoder.encode(data, StandardCharsets.UTF_8));
        //返回MD5加密后的参数
        return DigestUtils.md5DigestAsHex((Joiner.on("&").withKeyValueSeparator("=").join(signMap) + "&secretkey=" + secretKey).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param url  请求的URL地址
     * @param data 请求的数据
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<T>
     * @apiNote 发送请求
     * @author 琴声何来
     * @since 2023/4/3 9:56
     */
    private <T> CZZHTCResponse<T> sendRequest(String url, String data) {
        //获取时间戳，单位为秒
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        //拼接url
        String realUrl = url + "?appid=" + appId + "&timestamp=" + timestamp + "&version=1.0&sign=" + sign(timestamp, data);
        //请求body
        String body = "data=" + URLEncoder.encode(data, StandardCharsets.UTF_8);
        //请求
        RequestEntity<String> request = new RequestEntity<>(body, HttpMethod.POST, URI.create(realUrl));
        ResponseEntity<CZZHTCResponse<T>> response = restTemplate.exchange(realUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("请求云平台成功，返回信息：{}", response.getBody());
            //返回响应信息
        } else {
            log.error("请求云平台失败，错误码：{}，错误信息：{}", response.getStatusCodeValue(), response.getBody());
        }
        return response.getBody();
    }

    private <T> CZZHTCResponse<T> sendImageRequest(String url, String data, File image) {
        //获取时间戳，单位为秒
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        //拼接url
        String realUrl = url + "?appid=" + appId + "&timestamp=" + timestamp + "&version=1.0&sign=" + sign(timestamp, data);
        //请求body
        String body = "data=" + URLEncoder.encode(data, StandardCharsets.UTF_8);
        //请求
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", image);
        builder.part("data", body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = new RequestEntity<>(builder.build(), headers, HttpMethod.POST, URI.create(realUrl));
        ResponseEntity<CZZHTCResponse<T>> response = restTemplate.exchange(realUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("请求云平台成功，返回信息：{}", response.getBody());
            //返回响应信息
        } else {
            log.error("请求云平台失败，错误码：{}，错误信息：{}", response.getStatusCodeValue(), response.getBody());
        }
        return response.getBody();
    }

    //region 基础数据对接

    /**
     * @param request 车场信息同步参数
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<java.lang.String>
     * @apiNote 车场信息同步接口
     * @author 琴声何来
     * @since 2023/4/3 15:06
     */
    public CZZHTCResponse<String> syncParkInfo(SyncParkInfoRequest request) {
        //当前接口无需返回
        CZZHTCResponse<String> response = sendRequest(SYNC_PARK_INFO_URL, JSON.toJSONString(request));
        if (response.isSuccess()) {
            log.info("车场信息同步成功");
        } else {
            log.info("车场信息同步失败");
        }
        return response;
    }

    /**
     * @param request 空车位数上传参数
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<java.lang.String>
     * @apiNote 空车位数上传接口
     * @author 琴声何来
     * @since 2023/4/3 15:14
     */
    public CZZHTCResponse<String> emptySpaceRecord(EmptySpaceRecordRequest request) {
        //当前接口无需返回
        CZZHTCResponse<String> response = sendRequest(EMPTY_SPACE_RECORD_URL, JSON.toJSONString(request));
        if (response.isSuccess()) {
            log.info("空车位数上传成功");
        } else {
            log.info("空车位数上传失败");
        }
        return response;
    }
    //endregion

    //region 通行数据对接

    /**
     * @param request 进出记录上传参数
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<java.lang.String> 默认data为null，只返回success和message
     * @apiNote 进出记录上传接口
     * @author 琴声何来
     * @since 2023/4/3 11:27
     */
    public CZZHTCResponse<String> turnoverRecord(TurnoverRecordRequest request) {
        //当前接口无需返回
        CZZHTCResponse<String> response = sendRequest(TURNOVER_RECORD_URL, JSON.toJSONString(request));
        if (response.isSuccess()) {
            log.info("进出记录上传成功");
        } else {
            log.info("进出记录上传失败");
        }
        return response;
    }

    /**
     * @param request 收费记录上传参数
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<java.lang.String> 默认data为null，只返回success和message
     * @apiNote 收费记录上传接口
     * @author 琴声何来
     * @since 2023/4/3 11:27
     */
    public CZZHTCResponse<String> billRecord(BillRecordRequest request) {
        //当前接口无需返回
        CZZHTCResponse<String> response = sendRequest(BILL_RECORD_URL, JSON.toJSONString(request));
        if (response.isSuccess()) {
            log.info("收费记录上传成功");
        } else {
            log.info("收费记录上传失败");
        }
        return response;
    }

    /**
     * @param request 进出记录图片上传参数
     * @param image   进出记录图片
     * @return com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse<java.lang.String> 默认data为null，只返回success和message
     * @apiNote 进出记录图片上传接口
     * @author 琴声何来
     * @since 2023/4/3 11:27
     */
    public CZZHTCResponse<String> turnoverImageRecord(TurnoverImageRecordRequest request, File image) {
        //当前接口无需返回
        CZZHTCResponse<String> response = sendImageRequest(TURNOVER_IMAGE_RECORD_URL, JSON.toJSONString(request), image);
        if (response.isSuccess()) {
            log.info("进出记录图片上传成功");
        } else {
            log.info("进出记录图片上传失败");
        }
        return response;
    }
    //endregion

    //心跳线程
    public void heart() {
        boolean oldFlag = flag;
        String data = "{}";
        CZZHTCResponse<String> response = sendRequest(HEART_URL, data);
        flag = response.isSuccess();
        if (!oldFlag && flag) {
            //如果之前是false且本次为true，则说明之前心跳失败，本次为心跳成功，需要补传缓存中的所有数据
            //补传车场信息
            List<SyncParkInfoRequest> syncParkInfoRequestList = redisCache.getCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY);
            List<SyncParkInfoRequest> removeSyncParkInfoList = new ArrayList<>();
            if (!syncParkInfoRequestList.isEmpty()) {
                log.info("开始补传车场信息，list：{}", syncParkInfoRequestList);
                for (SyncParkInfoRequest syncParkInfoRequest : syncParkInfoRequestList) {
                    CZZHTCResponse<String> syncParkInfoResponse = syncParkInfo(syncParkInfoRequest);
                    if (syncParkInfoResponse.isSuccess()) {
                        removeSyncParkInfoList.add(syncParkInfoRequest);
                    }
                }
                syncParkInfoRequestList.removeAll(removeSyncParkInfoList);
                redisCache.setCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY, syncParkInfoRequestList);
                log.info("补传车场信息完成，list：{}", syncParkInfoRequestList);
            }
            //补传进出信息
            List<TurnoverRecordRequest> turnoverRecordRequestList = redisCache.getCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY);
            List<TurnoverRecordRequest> removeTurnoverRecordList = new ArrayList<>();
            if (!turnoverRecordRequestList.isEmpty()) {
                log.info("开始补传进出信息，list：{}", turnoverRecordRequestList);
                for (TurnoverRecordRequest turnoverRecordRequest : turnoverRecordRequestList) {
                    CZZHTCResponse<String> turnoverRecordResponse = turnoverRecord(turnoverRecordRequest);
                    if (turnoverRecordResponse.isSuccess()) {
                        removeTurnoverRecordList.add(turnoverRecordRequest);
                    }
                }
                turnoverRecordRequestList.removeAll(removeTurnoverRecordList);
                redisCache.setCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY, turnoverRecordRequestList);
                log.info("补传进出信息完成，list：{}", turnoverRecordRequestList);
            }
            //补传收费信息
            List<BillRecordRequest> billRecordRequestList = redisCache.getCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY);
            List<BillRecordRequest> removeBillRecordList = new ArrayList<>();
            if (!billRecordRequestList.isEmpty()) {
                log.info("开始补传收费信息，list：{}", billRecordRequestList);
                for (BillRecordRequest billRecordRequest : billRecordRequestList) {
                    CZZHTCResponse<String> billRecordResponse = billRecord(billRecordRequest);
                    if (billRecordResponse.isSuccess()) {
                        removeBillRecordList.add(billRecordRequest);
                    }
                }
                billRecordRequestList.removeAll(removeBillRecordList);
                redisCache.setCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY, billRecordRequestList);
                log.info("补传收费信息完成，list：{}", billRecordRequestList);
            }
        }
        log.info("常州智慧停车停车云平台心跳检测发送成功，flag：{}", flag);
    }

    public static void main(String[] args) {
        RestTemplate restTemplate1 = new RestTemplate();
        TurnoverImageRecordRequest turnoverImageRecordRequest = new TurnoverImageRecordRequest();
        turnoverImageRecordRequest.setRecordID(1);
        turnoverImageRecordRequest.setType(1);
        String data = JSON.toJSONString(turnoverImageRecordRequest);
        //获取时间戳，单位为秒
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        //拼接url
        //固定参数
        Map<String, String> paramMap = Map.of("appid", "93cb66160d91b30d8b0a15cce54c5010c1c87c35fa28df2a", "timestamp", timestamp, "version", "1.0");
        //MD5数据源
        Map<String, String> signMap = new TreeMap<>(paramMap);
        signMap.put("data", URLEncoder.encode(data, StandardCharsets.UTF_8));
        //返回MD5加密后的参数
        String sign = DigestUtils.md5DigestAsHex((Joiner.on("&").withKeyValueSeparator("=").join(signMap) + "&secretkey=db91ed652012c1cdc16e090bd8aeb82b").getBytes(StandardCharsets.UTF_8));
        String realUrl = "http://tcapi.czzhtc.cn:18888//record/img.htm?appid=93cb66160d91b30d8b0a15cce54c5010c1c87c35fa28df2a&timestamp=" + timestamp + "&version=1.0&sign=" + sign;
        //请求body
        String body = "data=" + URLEncoder.encode(data, StandardCharsets.UTF_8);
        //请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new File("C:\\Users\\LENOVO\\Documents\\WeChat Files\\All Users\\4151560c2d7560f7874fbb39a9306f97.jpg"));
        builder.part("data", body);
        RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = new RequestEntity<>(builder.build(), headers, HttpMethod.POST, URI.create(realUrl));
        ResponseEntity<CZZHTCResponse<String>> response = restTemplate1.exchange(realUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("请求云平台成功，返回信息：{}", response.getBody());
            //返回响应信息
            System.out.println(response.getBody());
        } else {
            log.error("请求云平台失败，错误码：{}，错误信息：{}", response.getStatusCodeValue(), response.getBody());
            System.out.println(response);
        }
    }
}
