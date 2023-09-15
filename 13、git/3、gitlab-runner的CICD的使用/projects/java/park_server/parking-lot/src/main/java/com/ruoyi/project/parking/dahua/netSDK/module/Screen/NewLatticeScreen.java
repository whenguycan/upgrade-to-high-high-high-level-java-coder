package com.ruoyi.project.parking.dahua.netSDK.module.Screen;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.project.parking.dahua.netSDK.common.Res;
import com.ruoyi.project.parking.dahua.netSDK.lib.NetSDKLib;
import com.ruoyi.project.parking.dahua.netSDK.module.LoginModule;
import com.sun.jna.Pointer;

import javax.swing.*;
import java.io.UnsupportedEncodingException;

/**
 * @author 119178 点阵屏下发demo
 */
public class NewLatticeScreen {


//    public static void main(String[] args) {
//        NewLatticeScreen newLatticeScreen=new NewLatticeScreen();
//        LoginModule.login("58.216.154.140", 37777,
//                "admin", "admin123");
//        newLatticeScreen.actionPerformed("马增来","50","马增来","月卡用户",
//                "31",1,"2023-03-30","2023-03-31","100元",
//                88,"PASSING_CAR","","测试");
//        LoginModule.logout();
//        LoginModule.cleanup();
//    }

    // 设备断线通知回调
    private DisConnect disConnect = new DisConnect();

    // 网络连接恢复
    private HaveReConnect haveReConnect = new HaveReConnect();

    /////////////////面板///////////////////
// 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private class DisConnect implements NetSDKLib.fDisConnect {
        @Override
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }
    }

    // 网络连接恢复，设备重连成功回调
// 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
    private class HaveReConnect implements NetSDKLib.fHaveReConnect {
        @Override
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);

        }
    }

    /**
     * Create the application.
     */
    public NewLatticeScreen() {
        LoginModule.init(disConnect, haveReConnect); // 打开工程，初始化
    }

    public void actionPerformed(String carNumber, String parkingTime, String masterofCar,
                                String userType, String RemainDay, int passEnable,
                                String stuInTime, String stuOutTime, String parkCharges,
                                int remainSpace, String carStatus,String subUserTypes,String remark) {
        int emType = NetSDKLib.CtrlType.CTRLTYPE_CTRL_SET_PARK_INFO;

        final JPanel panel1 = new JPanel();
        NetSDKLib.NET_CTRL_SET_PARK_INFO msg = new NetSDKLib.NET_CTRL_SET_PARK_INFO();

        try {
            // 车牌号码
            byte[] plateNumber = carNumber.getBytes("GBK");
            System.arraycopy(plateNumber, 0, msg.szPlateNumber, 0, plateNumber.length);
        } catch (UnsupportedEncodingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        // 停车时长,单位:分钟
        if (!parkingTime.equals("")) {
            msg.nParkTime = Integer.parseInt(parkingTime);
        } else {
            msg.nParkTime = 0;
        }

        // 车主姓名
        try {
            byte[] masterOfCar = masterofCar.getBytes("GBK");
            System.arraycopy(masterOfCar, 0, msg.szMasterofCar, 0, masterOfCar.length);
        } catch (UnsupportedEncodingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        // 用户类型
        // monthlyCardUser表示月卡用户,yearlyCardUser表示年卡用户,longTimeUser表示长期用户/VIP,casualUser表示临时用户/Visitor

        if (userType != null) {
            if (userType.equals(Constants.MONTHLY_CARD_USER)) {
                System.arraycopy(Constants.MONTHLY_CARD_USER.getBytes(), 0, msg.szUserType, 0, Constants.MONTHLY_CARD_USER.length());
            } else if (userType.equals(Constants.YEARLY_CARD_USER)) {
                System.arraycopy(Constants.YEARLY_CARD_USER.getBytes(), 0, msg.szUserType, 0, Constants.YEARLY_CARD_USER.length());
            } else if (userType.equals(Constants.LONG_TIME_USER)) {
                System.arraycopy(Constants.LONG_TIME_USER.getBytes(), 0, msg.szUserType, 0, Constants.LONG_TIME_USER.length());
            } else if (userType.equals(Constants.CASUAL_USER)) {
                System.arraycopy(Constants.CASUAL_USER.getBytes(), 0, msg.szUserType, 0, Constants.CASUAL_USER.length());
            }
        }

        // 到期天数
        if (!RemainDay.equals("")) {
            msg.nRemainDay = Integer.parseInt(RemainDay);
        } else {
            msg.nRemainDay = 0;
        }

        // 0:不允许车辆通过 1:允许车辆通过
        msg.nPassEnable = passEnable;

        // 车辆入场时间
        String[] InTimes = stuInTime.split("-");

        msg.stuInTime.dwYear = (short) Integer.parseInt(InTimes[0]);
        msg.stuInTime.dwMonth = (byte) Integer.parseInt(InTimes[1]);
        msg.stuInTime.dwDay = (byte) Integer.parseInt(InTimes[2]);

        // 车辆出场时间
        String[] OutTimes = stuOutTime.split("-");

        msg.stuOutTime.dwYear = (short) Integer.parseInt(OutTimes[0]);
        msg.stuOutTime.dwMonth = (byte) Integer.parseInt(OutTimes[1]);
        msg.stuOutTime.dwDay = (byte) Integer.parseInt(OutTimes[2]);

        try {
            byte[] parkCharge = parkCharges.getBytes("GBK");
            System.arraycopy(parkCharge, 0, msg.szParkCharge, 0, parkCharge.length);
        } catch (UnsupportedEncodingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        // 停车库余位数
        msg.nRemainSpace = remainSpace;

        // 过车状态 详见EM_CARPASS_STATUS
        if (carStatus != null) {
            if (carStatus.equals(Res.string().getPassingCar())) {
                msg.emCarStatus = 1;
            } else if (carStatus.equals(Res.string().getNoCar())) {
                msg.emCarStatus = 2;
            }
        }

        // 用户类型（szUserType字段）的子类型
        try {
            byte[] subUserType = subUserTypes.getBytes("GBK");
            System.arraycopy(subUserType, 0, msg.szSubUserType, 0, subUserType.length);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //// 备注信息
        try {
            byte[] remarks = remark.getBytes("GBK");
            System.arraycopy(remarks, 0, msg.szRemarks, 0, remarks.length);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//        try {
//            byte[] custom = textField_CustomerInfo.getText().getBytes("GBK");
//            System.arraycopy(custom, 0, msg.szCustom, 0, custom.length);
//        } catch (UnsupportedEncodingException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

        msg.write();
        boolean ret = DotmatrixScreenModule.setDotmatrixScreen(emType, msg);
//        if (ret) {
//            JOptionPane.showMessageDialog(panel1, Res.string().getSetUpSuccess());
//        } else {
//            JOptionPane.showMessageDialog(panel1, Res.string().getSetUpFailed());
//        }
    }
}
