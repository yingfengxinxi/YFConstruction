/*
 * @Author: wnj 
 * @Date: 2021-11-19 15:34:30 
 * @Last Modified by: wnj
 * @Last Modified time: 2021-11-19 16:59:57
 */
/**
 * 本js方法说明
 * getDate                  获取当天日期
 * getMonday                获取本周开始/结束日期
 * getMonth                 获取本月开始/结束时间
 * getYear                  获取本年开始/结束时间
 * 
 * formatDate               日期格式化（YYYY-MM-dd）
 * 
 * getWeekStartDate         获取本周开始日期
 * getWeekEndDate           获取本周结束日期
 * getMonthStartDate        获取本月开始日期
 * getMonthEndDate          获取本月结束日期
 * getLastMonthStartDate    获取上月开始日期
 * getLastMonthEndDate      获取上月结束日期
 * getQuarterStartDate      获取本季度开始日期
 * getQuarterEndDate        获取本季度开始日期
 * 
 * getMonthDays             获取本年某月的天数
 * getQuarterStartMonth     获取本季度开始月份
 */

/**
 * dates为数字类型，0代表今日,-1代表昨日，1代表明日，返回yyyy-mm-dd格式字符串，dates不传默认代表今日。
 * @param {*} dates 
 * @returns 
 */
export function getDate (dates) {
    var dd = new Date();
    var n = dates || 0;
    dd.setDate(dd.getDate() + n);
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;
    var d = dd.getDate();
    m = m < 10 ? "0" + m : m;
    d = d < 10 ? "0" + d : d;
    var day = y + "-" + m + "-" + d;
    return day;
};
/**
 * type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，dates为数字类型，不传或0代表本周，-1代表上周，1代表下周
 * getMonday("s", 1)  //得到下周一的yyyy-mm-dd格式日期
 * getMonday("e", 1)  //得到下周日的yyyy-mm-dd格式日期
 * @param {*} type 
 * @param {*} dates 
 * @returns 
 */
export function getMonday (type, dates) {
    var now = new Date();
    var nowTime = now.getTime();
    var day = now.getDay();
    var longTime = 24 * 60 * 60 * 1000;
    var n = longTime * 7 * (dates || 0);
    if (type == "s") {
        var dd = nowTime - (day - 1) * longTime + n;
    };
    if (type == "e") {
        var dd = nowTime + (7 - day) * longTime + n;
    };
    dd = new Date(dd);
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;
    var d = dd.getDate();
    m = m < 10 ? "0" + m : m;
    d = d < 10 ? "0" + d : d;
    var day = y + "-" + m + "-" + d;
    return day;
};
/**
 * type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，months为数字类型，不传或0代表本月，-1代表上月，1代表下月
 * getMonth("s", 1)  //得到下月第一天的yyyy-mm-dd格式日期
 * getMonth("e", 1)  //得到下月最后一天的yyyy-mm-dd格式日期
 * @param {*} type 
 * @param {*} months 
 * @returns 
 */
export function getMonth (type, months) {
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    if (Math.abs(months) > 12) {
        months = months % 12;
    };
    if (months != 0) {
        if (month + months > 12) {
            year++;
            month = (month + months) % 12;
        } else if (month + months < 1) {
            year--;
            month = 12 + month + months;
        } else {
            month = month + months;
        };
    };
    month = month < 10 ? "0" + month : month;
    var date = d.getDate();
    var firstday = year + "-" + month + "-" + "01";
    var lastday = "";
    if (month == "01" || month == "03" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12") {
        lastday = year + "-" + month + "-" + 31;
    } else if (month == "02") {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
            lastday = year + "-" + month + "-" + 29;
        } else {
            lastday = year + "-" + month + "-" + 28;
        };
    } else {
        lastday = year + "-" + month + "-" + 30;
    };
    var day = "";
    if (type == "s") {
        day = firstday;
    } else {
        day = lastday;
    };
    return day;
};
/**
 * type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，dates为数字类型，不传或0代表今年，-1代表去年，1代表明年
 * getYear("s", 1)  //得到明年第一天的yyyy-mm-dd格式日期
 * getYear("e", 1)  //得到明年最后一天的yyyy-mm-dd格式日期
 * @param {*} type 
 * @param {*} dates 
 * @returns 
 */
export function getYear (type, dates) {
    var dd = new Date();
    var n = dates || 0;
    var year = dd.getFullYear() + Number(n);
    if (type == "s") {
        var day = year + "-01-01";
    };
    if (type == "e") {
        var day = year + "-12-31";
    };
    if (!type) {
        var day = year + "-01-01/" + year + "-12-31";
    };
    return day;
};

/** 
* 获取本周、本季度、本月、上月的开端日期、停止日期 
*/
var now = new Date(); //当前日期 
var nowDayOfWeek = now.getDay(); //今天本周的第几天 
var nowDay = now.getDate(); //当前日 
var nowMonth = now.getMonth(); //当前月 
var nowYear = now.getYear(); //当前年 
nowYear += (nowYear < 2000) ? 1900 : 0; // 

var lastMonthDate = new Date(); //上月日期 
lastMonthDate.setDate(1);
lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
var lastYear = lastMonthDate.getYear();
var lastMonth = lastMonthDate.getMonth();

//格局化日期：yyyy-MM-dd 
export function formatDate (date) {
    var myyear = date.getFullYear();
    var mymonth = date.getMonth() + 1;
    var myweekday = date.getDate();

    if (mymonth < 10) {
        mymonth = "0" + mymonth;
    }
    if (myweekday < 10) {
        myweekday = "0" + myweekday;
    }
    return (myyear + "-" + mymonth + "-" + myweekday);
}
//获得本周的开端日期 
export function getWeekStartDate () {
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
    return formatDate(weekStartDate);
}

//获得本周的停止日期 
export function getWeekEndDate () {
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    return formatDate(weekEndDate);
}

//获得本月的开端日期 
export function getMonthStartDate () {
    var monthStartDate = new Date(nowYear, nowMonth, 1);
    return formatDate(monthStartDate);
}

//获得本月的停止日期 
export function getMonthEndDate () {
    var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
    return formatDate(monthEndDate);
}


//获得某月的天数 
export function getMonthDays (myMonth) {
    var monthStartDate = new Date(nowYear, myMonth, 1);
    var monthEndDate = new Date(nowYear, myMonth + 1, 1);
    var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
    return days;
}

//获得本季度的开端月份 
export function getQuarterStartMonth () {
    var quarterStartMonth = 0;
    if (nowMonth < 3) {
        quarterStartMonth = 0;
    }
    if (2 < nowMonth && nowMonth < 6) {
        quarterStartMonth = 3;
    }
    if (5 < nowMonth && nowMonth < 9) {
        quarterStartMonth = 6;
    }
    if (nowMonth > 8) {
        quarterStartMonth = 9;
    }
    return quarterStartMonth;
}



//获得上月开端时候 
export function getLastMonthStartDate () {
    var lastMonthStartDate = new Date(nowYear, lastMonth, 1);
    return formatDate(lastMonthStartDate);
}

//获得上月停止时候 
export function getLastMonthEndDate () {
    var lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));
    return formatDate(lastMonthEndDate);
}

//获得本季度的开端日期 
export function getQuarterStartDate () {

    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
    return formatDate(quarterStartDate);
}

//或的本季度的停止日期 
export function getQuarterEndDate () {
    var quarterEndMonth = getQuarterStartMonth() + 2;
    var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));
    return formatDate(quarterStartDate);
}
