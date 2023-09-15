package com.czdx.parkingcharge.common.enums;

public class ParkEnums {

    private ParkEnums() {}

    /**
     * 车场编号
     */
    public enum ParkNo {
        WEST_TAI_LAKE_GROUP("P20230000000001", "西太湖物业");

        private String value;

        private String desc;

        ParkNo(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 节假日类型标识
     */
    public enum HolidayType {
        ALL("ALL", "全部");

        private String value;

        private String desc;

        HolidayType(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 区域类型标识
     */
    public enum ParkLotSign {
        ALL("ALL", "全部");

        private String value;

        private String desc;

        ParkLotSign(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 车类型标识
     */
    public enum VehicleTypeSign {
        ALL("ALL", "全部");

        private String value;

        private String desc;

        VehicleTypeSign(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 车辆车类型标识
     */
    public enum CarVehicleCategory {
        LS("LS", "临时车"), GD("GD", "固定车");

        private String value;

        private String desc;

        CarVehicleCategory(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

}
