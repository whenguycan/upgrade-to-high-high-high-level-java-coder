import request from '@/utils/request'

export function parkDetail() {
    return request({
        url: '/parking/BParkChargeScheme/getInfo',
        method: 'get'
    })
}

export function parkAdd(data) {
    return request({
        url: '/parking/BParkChargeScheme/add',
        method: 'post',
        data
    })
}

export function parkEdit(data) {
    return request({
        url: '/parking/BParkChargeScheme/edit',
        method: 'post',
        data
    })
}

export function ruleList(query) {
    return request({
        url: '/parking/BParkChargeRule/list',
        method: 'get',
        params: query
    })
}

export function ruleDetail(id) {
    return request({
        url: '/parking/BParkChargeRule/' + id,
        method: 'get',
    })
}

export function ruleEdit(data) {
    return request({
        url: '/parking/BParkChargeRule/edit',
        method: 'post',
        data
    })
}

export function ruleDelete(id) {
    return request({
        url: '/parking/BParkChargeRule/delete?ids=' + id,
        method: 'post',
    })
}

export function ruleTest(data) {
    return request({
        url: '/parking/BParkChargeRule/testParkRate',
        method: 'post',
        data
    })
}

export function ruleAdd(data) {
    return request({
        url: '/parking/BParkChargeRule/add',
        method: 'post',
        data
    })
}

export function ruleReset(data) {
    return request({
        url: '/parking/BParkChargeRule/reset',
        method: 'post',
        data
    })
}

export function listField(query) {
    return request({
        url: "/parking/BParkChargeRule/bFieldList",
        method: "get",
        params: query,
    });
}

export function categoryList(query) {
    return request({
        url: "/parking/BParkChargeRule/regularCarCategoryList",
        method: "get",
        params: query,
    });
}

export function getSettingcartypeList(query) {
    return request({
        url: '/parking/BParkChargeRule/carTypeList',
        method: 'get',
        params: query
    })
}

export function setRelation(data) {
    return request({
        url: '/parking/BParkChargeRelationVehicle/setRelation',
        method: 'post',
        data
    })
}

export function relationVehicleList(query) {
    return request({
        url: '/parking/BParkChargeRelationVehicle/list',
        method: 'get',
        params: query
    })
}

export function noRelationVehicleList(query) {
    return request({
        url: '/parking/BParkChargeRelationVehicle/notRelatedList',
        method: 'get',
        params: query
    })
}

export function holidayList(query) {
    return request({
        url: "/parking/BHoliday/list",
        method: "get",
        params: query,
    });
}

export function holidayDetail(id) {
    return request({
        url: "/parking/BHoliday/"+id,
        method: "get",
    });
}

export function holidayDel(data) {
    return request({
        url: "/parking/BHoliday/delete?ids="+data.id,
        method: "post"
    });
}

export function holidayAdd(data) {
    return request({
        url: "/parking/BHoliday/add",
        method: "post",
        data
    });
}

export function holidayEdit(data) {
    return request({
        url: "/parking/BHoliday/edit",
        method: "post",
        data
    });
}

export function getThisYearJjr(query) {
    return request({
        url: "/parking/BHoliday/getThisYearJjr",
        method: "get",
        params: query,
    });
}

export function setHolRelation(data) {
    return request({
        url: '/parking/BParkChargeRelationHoliday/setRelation',
        method: 'post',
        data
    })
}

export function relationHolidayList(query) {
    return request({
        url: "/parking/BParkChargeRelationHoliday/list",
        method: "get",
        params: query
    });
}

export function relationHolidayDel(data) {
    return request({
        url: "/parking/BParkChargeRelationHoliday/delete?ids="+data.id,
        method: "post"
    });
}

export function relationVehicleDel(data) {
    return request({
        url: "/parking/BParkChargeRelationVehicle/delete?ids="+data.id,
        method: "post"
    });
}

