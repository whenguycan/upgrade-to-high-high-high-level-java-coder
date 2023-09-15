package com.ruoyi.project.parking.schedule;

import com.ruoyi.project.parking.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@Slf4j
public class HomePageSchedule {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IPayMethodDayFactService payMethodDayFactService;

    @Autowired
    private IPayTypeDayFactService payTypeDayFactService;

    @Autowired
    private IDurationStatisticDayFactService durationStatisticDayFactService;

    @Autowired
    private IOrderSituationDayFactService orderSituationDayFactService;

    @Autowired
    private IEntryExitAnalysisDayFactService entryExitAnalysisDayFactService;

    @Autowired
    private ITimeShareUsageDayFactService timeShareUsageDayFactService;

    @Autowired
    private ITopRankDayFactService topRankDayFactService;

    @Autowired
    private IParkUsageDayFactService parkUsageDayFactService;

    @Autowired
    private IRevenueStatisticsDayFactService revenueStatisticsDayFactService;

    @Autowired
    private ICouponDetailDayFactService couponDetailDayFactService;

    private static final String ANALYSE_PAY_METHOD_DAY_FACT = "ANALYSE_PAY_METHOD_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_PAY_TYPE_DAY_FACT = "ANALYSE_PAY_TYPE_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_DURATION_STATISTIC_DAY_FACT = "ANALYSE_DURATION_STATISTIC_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_ORDER_SITUATION_DAY_FACT = "ANALYSE_ORDER_SITUATION_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_ENTRY_EXIT_ANALYSIS_DAY_FACT = "ANALYSE_ENTRY_EXIT_ANALYSIS_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_TIME_SHARE_USAGE_DAY_FACT = "ANALYSE_TIME_SHARE_USAGE_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_TOP_RANK_DAY_FACT = "ANALYSE_TOP_RANK_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_PARK_USAGE_DAY_FACT = "ANALYSE_PARK_USAGE_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_REVENUE_STATISTICS_DAY_FACT = "ANALYSE_REVENUE_STATISTICS_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_COUPON_DETAIL_DAY_FACT = "ANALYSE_COUPON_DETAIL_DAY_FACT_TASK_RUNNING";

    private static final String ANALYSE_AMOUNT_ANALYSIS_DAY_FACT = "ANALYSE_AMOUNT_ANALYSIS_DAY_FACT_TASK_RUNNING";

    /**
     * 分析首页付费方式事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analysePayMethodDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_PAY_METHOD_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                payMethodDayFactService.analysePayMethodDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页付费方式事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页付费方式事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页付费类型事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analysePayTypeDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_PAY_TYPE_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                payTypeDayFactService.analysePayTypeDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页付费类型事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页付费类型事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页时长统计事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analyseDurationStatisticDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_DURATION_STATISTIC_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                durationStatisticDayFactService.analyseDurationStatisticDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页时长统计事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页时长统计事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页订单情况事实
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void analyseOrderSituationDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_ORDER_SITUATION_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后1800秒自动解锁
        boolean res = lock.tryLock(100, 1800000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                orderSituationDayFactService.analyseOrderSituationDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页订单情况事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页订单情况事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页出入场分析事实
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void analyseEntryExitAnalysisDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_ENTRY_EXIT_ANALYSIS_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后1800秒自动解锁
        boolean res = lock.tryLock(100, 1800000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                entryExitAnalysisDayFactService.analyseEntryExitAnalysisDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页出入场分析事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页出入场分析事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页分时利用率事实
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void analyseTimeShareUsageDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_TIME_SHARE_USAGE_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后1800秒自动解锁
        boolean res = lock.tryLock(100, 1800000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                timeShareUsageDayFactService.analyseTimeShareUsageDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页分时利用率事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页分时利用率事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页车场热门排行事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analyseTopRankDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_TOP_RANK_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                topRankDayFactService.analyseTopRankDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页车场热门排行事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页车场热门排行事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页今日泊车使用情况事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analyseParkUsageDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_PARK_USAGE_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                parkUsageDayFactService.analyseParkUsageDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页今日泊车使用情况事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页今日泊车使用情况事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页收入统计事实
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void analyseRevenueStatisticsDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_REVENUE_STATISTICS_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后600秒自动解锁
        boolean res = lock.tryLock(100, 600000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
//                LoginUser loginUser = SecurityUtils.getLoginUser();
//                String userId = String.valueOf(loginUser.getUserId());
                revenueStatisticsDayFactService.analyseRevenueStatisticsDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页收入统计事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页收入统计事实已在其他实例执行 !");
        }
    }

    /**
     * 分析首页月发放优惠券事实
     * cron 每日凌晨1点更新数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void analyseCouponDetailDayFact() throws InterruptedException {
        // redisson 分布式锁
        RLock lock = redissonClient.getLock(ANALYSE_COUPON_DETAIL_DAY_FACT);
        // 尝试加锁，最多等待1秒，上锁以后1800秒自动解锁
        boolean res = lock.tryLock(100, 1800000, TimeUnit.MILLISECONDS);
        if (res) {
            try {
                couponDetailDayFactService.analyseCouponDetailDayFact("1");
            } catch (Exception e) {
                log.error("执行定时分析首页月发放优惠券统计事实任务异常 - {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            log.info("分析首页月发放优惠券统计事实已在其他实例执行 !");
        }
    }
}
