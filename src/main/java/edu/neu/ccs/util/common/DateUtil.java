package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class DateUtil {
    public static String pattern = "yyyy-MM-dd";
    public static SimpleDateFormat formatter;
    public static SimpleDateFormat formatter2;
    public static DateTimeFormatter dateFormatter;

    public DateUtil() {
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return dateFormatter;
    }

    public static Date getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date());
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static Date getNowDateShort() {
        String dateString = formatter.format(new Date());
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String getStringAllDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String getStringDateShort() {
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStr(Date dateDate) {
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStr(LocalDate dateDate) {
        String dateString = dateFormatter.format(dateDate);
        return dateString;
    }

    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static Timestamp strToDateSql(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter2.parse(strDate, pos);
        return new Timestamp(strtodate.getTime());
    }

    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 122400000L * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getTodayShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getHHMMSS(String value) {
        String hour = "00";
        String minute = "00";
        String second = "00";
        if(value != null && !value.trim().equals("")) {
            int v_int = Integer.valueOf(value).intValue();
            hour = v_int / 3600 + "";
            minute = v_int % 3600 / 60 + "";
            second = v_int % 3600 % 60 + "";
        }

        return (hour.length() > 1?hour:"0" + hour) + ":" + (minute.length() > 1?minute:"0" + minute) + ":" + (second.length() > 1?second:"0" + second);
    }

    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour = dateString.substring(11, 13);
        return hour;
    }

    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min = dateString.substring(14, 16);
        return min;
    }

    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getTwoHour(String st1, String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if(Integer.parseInt(kk[0]) < Integer.parseInt(jj[0])) {
            return "0";
        } else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60.0D;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60.0D;
            return y - u > 0.0D?y - u + "":"0";
        }
    }

    public static String getTwoDay(String sj1, String sj2) {
        long day = 0L;

        try {
            Date e = formatter.parse(sj1);
            Date mydate = formatter.parse(sj2);
            day = (e.getTime() - mydate.getTime()) / 86400000L;
        } catch (Exception var6) {
            return "";
        }

        return day + "";
    }

    public static String getPreTime(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";

        try {
            Date date1 = format.parse(sj1);
            long Time = date1.getTime() / 1000L + (long)(Integer.parseInt(jj) * 60);
            date1.setTime(Time * 1000L);
            mydate1 = format.format(date1);
        } catch (Exception var7) {
            ;
        }

        return mydate1;
    }

    public static String getNextDay(String nowdate, String delay) {
        try {
            String e = "";
            Date d = strToDate(nowdate);
            long myTime = d.getTime() / 1000L + (long)(Integer.parseInt(delay) * 24 * 60 * 60);
            d.setTime(myTime * 1000L);
            e = formatter.format(d);
            return e;
        } catch (Exception var6) {
            return "";
        }
    }

    public static String getFromNow(int day) {
        Date date = new Date();
        long dateTime = date.getTime() / 1000L + (long)(day * 24 * 60 * 60);
        date.setTime(dateTime * 1000L);
        return formatter2.format(date);
    }

    public static boolean isLeapYear(String ddate) {
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar)Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(1);
        return year % 400 == 0?true:(year % 4 == 0?year % 100 != 0:false);
    }

    public static String getEDate(String str) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        String j = strtodate.toString();
        String[] k = j.split(" ");
        return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
    }

    public static String getEndDateOfMonth(String dat) {
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if(mon != 1 && mon != 3 && mon != 5 && mon != 7 && mon != 8 && mon != 10 && mon != 12) {
            if(mon != 4 && mon != 6 && mon != 9 && mon != 11) {
                if(isLeapYear(dat)) {
                    str = str + "29";
                } else {
                    str = str + "28";
                }
            } else {
                str = str + "30";
            }
        } else {
            str = str + "31";
        }

        return str;
    }

    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(1) - cal2.get(1);
        if(0 == subYear) {
            if(cal1.get(3) == cal2.get(3)) {
                return true;
            }
        } else if(1 == subYear && 11 == cal2.get(2)) {
            if(cal1.get(3) == cal2.get(3)) {
                return true;
            }
        } else if(-1 == subYear && 11 == cal1.get(2) && cal1.get(3) == cal2.get(3)) {
            return true;
        }

        return false;
    }

    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(3));
        if(week.length() == 1) {
            week = "0" + week;
        }

        String year = Integer.toString(c.get(1));
        return year + week;
    }

    public static String getWeek(String sdate, String num) {
        Date dd = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if(num.equals("1")) {
            c.set(7, 2);
        } else if(num.equals("2")) {
            c.set(7, 3);
        } else if(num.equals("3")) {
            c.set(7, 4);
        } else if(num.equals("4")) {
            c.set(7, 5);
        } else if(num.equals("5")) {
            c.set(7, 6);
        } else if(num.equals("6")) {
            c.set(7, 7);
        } else if(num.equals("0")) {
            c.set(7, 1);
        }

        return (new SimpleDateFormat("yyyy-MM-dd")).format(c.getTime());
    }

    public static String getWeek(String sdate) {
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (new SimpleDateFormat("EEEE")).format(c.getTime());
    }

    public static String getWeekStr(String sdate) {
        String str = "";
        str = getWeek(sdate);
        if("1".equals(str)) {
            str = "星期日";
        } else if("2".equals(str)) {
            str = "星期一";
        } else if("3".equals(str)) {
            str = "星期二";
        } else if("4".equals(str)) {
            str = "星期三";
        } else if("5".equals(str)) {
            str = "星期四";
        } else if("6".equals(str)) {
            str = "星期五";
        } else if("7".equals(str)) {
            str = "星期六";
        }

        return str;
    }

    public static long getDays(String date1, String date2) {
        if(date1 != null && !date1.equals("")) {
            if(date2 != null && !date2.equals("")) {
                Date date = null;
                Date mydate = null;

                try {
                    date = formatter.parse(date1);
                    mydate = formatter.parse(date2);
                } catch (Exception var6) {
                    ;
                }

                long day = (date.getTime() - mydate.getTime()) / 86400000L;
                return day;
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static String getNowMonth(String sdate) {
        sdate = sdate.substring(0, 8) + "01";
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int u = c.get(7);
        String newday = getNextDay(sdate, 1 - u + "");
        return newday;
    }

    public static String getNo(int k) {
        return getUserDate("yyyyMMddhhmmss") + getRandom(k);
    }

    public static String getRandom(int i) {
        Random jjj = new Random();
        if(i == 0) {
            return "";
        } else {
            String jj = "";

            for(int k = 0; k < i; ++k) {
                jj = jj + jjj.nextInt(9);
            }

            return jj;
        }
    }

    public static boolean RightDate(String date) {
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(date == null) {
            return false;
        } else {
            SimpleDateFormat sdf;
            if(date.length() > 10) {
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }

            try {
                sdf.parse(date);
                return true;
            } catch (ParseException var3) {
                return false;
            }
        }
    }

    public static String getStringDateMonth(String sdate, String nd, String yf, String rq, String format) {
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        String s_nd = dateString.substring(0, 4);
        String s_yf = dateString.substring(5, 7);
        String s_rq = dateString.substring(8, 10);
        String sreturn = "";
        if(sdate != null && !sdate.equals("")) {
            sdate = getOKDate(sdate);
            s_nd = sdate.substring(0, 4);
            s_yf = sdate.substring(5, 7);
            s_rq = sdate.substring(8, 10);
            if(nd.equals("1")) {
                sreturn = s_nd;
                if(format.equals("1")) {
                    sreturn = s_nd + "年";
                } else if(format.equals("2")) {
                    sreturn = s_nd + "-";
                } else if(format.equals("3")) {
                    sreturn = s_nd + "/";
                } else if(format.equals("5")) {
                    sreturn = s_nd + ".";
                }
            }

            if(yf.equals("1")) {
                sreturn = sreturn + s_yf;
                if(format.equals("1")) {
                    sreturn = sreturn + "月";
                } else if(format.equals("2")) {
                    sreturn = sreturn + "-";
                } else if(format.equals("3")) {
                    sreturn = sreturn + "/";
                } else if(format.equals("5")) {
                    sreturn = sreturn + ".";
                }
            }

            if(rq.equals("1")) {
                sreturn = sreturn + s_rq;
                if(format.equals("1")) {
                    sreturn = sreturn + "日";
                }
            }
        } else {
            if(nd.equals("1")) {
                sreturn = s_nd;
                if(format.equals("1")) {
                    sreturn = s_nd + "年";
                } else if(format.equals("2")) {
                    sreturn = s_nd + "-";
                } else if(format.equals("3")) {
                    sreturn = s_nd + "/";
                } else if(format.equals("5")) {
                    sreturn = s_nd + ".";
                }
            }

            if(yf.equals("1")) {
                sreturn = sreturn + s_yf;
                if(format.equals("1")) {
                    sreturn = sreturn + "月";
                } else if(format.equals("2")) {
                    sreturn = sreturn + "-";
                } else if(format.equals("3")) {
                    sreturn = sreturn + "/";
                } else if(format.equals("5")) {
                    sreturn = sreturn + ".";
                }
            }

            if(rq.equals("1")) {
                sreturn = sreturn + s_rq;
                if(format.equals("1")) {
                    sreturn = sreturn + "日";
                }
            }
        }

        return sreturn;
    }

    public static String getNextMonthDay(String sdate, int m) {
        sdate = getOKDate(sdate);
        int year = Integer.parseInt(sdate.substring(0, 4));
        int month = Integer.parseInt(sdate.substring(5, 7));
        month += m;
        if(month < 0) {
            month += 12;
            --year;
        } else if(month > 12) {
            month -= 12;
            ++year;
        }

        String smonth = "";
        if(month < 10) {
            smonth = "0" + month;
        } else {
            smonth = "" + month;
        }

        return year + "-" + smonth + "-10";
    }

    public static String getOKDate(String sdate) {
        if(sdate != null && !sdate.equals("")) {
            if(sdate.length() == 8) {
                sdate = sdate.substring(0, 4) + "-" + sdate.substring(4, 6) + "-" + sdate.substring(6, 8);
            }

            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(sdate, pos);
            String dateString = formatter.format(strtodate);
            return dateString;
        } else {
            return getStringDateShort();
        }
    }

    private static String getBeforeDay(Calendar cl) {
        cl.add(5, -1);
        return formatter.format(cl.getTime());
    }

    private static String getAfterDay(Calendar cl) {
        cl.add(5, 1);
        return formatter.format(cl.getTime());
    }

    private static String getDateAMPM() {
        GregorianCalendar ca = new GregorianCalendar();
        int i = ca.get(9);
        return i == 0?"AM":"PM";
    }

    private static int compareToDate(String date1, String date2) {
        return date1.compareTo(date2);
    }

    private static int compareToDateString(String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte i = 0;

        try {
            long e = formatter.parse(date1).getTime();
            long ldate2 = formatter.parse(date2).getTime();
            if(e > ldate2) {
                i = 1;
            } else if(e == ldate2) {
                i = 0;
            } else {
                i = -1;
            }
        } catch (ParseException var8) {
            var8.printStackTrace();
        }

        return i;
    }

    public static String[] getFiveDate() {
        String[] dates = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String five = " 05:00:00";
        if(getDateAMPM().equals("AM") && compareToDateString(getStringDate(), getStringDateShort() + five) == -1) {
            dates[0] = getBeforeDay(calendar) + five;
            dates[1] = getStringDateShort() + five;
        } else {
            dates[0] = getStringDateShort() + five;
            dates[1] = getAfterDay(calendar) + five;
        }

        return dates;
    }

    public static String getFiveDate2() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String five = " 05:00:00";
        String reStr = "";
        if(getDateAMPM().equals("AM") && compareToDateString(getStringDate(), getStringDateShort() + five) == -1) {
            reStr = getBeforeDay(calendar);
        } else {
            reStr = getStringDateShort();
        }

        return reStr;
    }

    static {
        formatter = new SimpleDateFormat(pattern);
        formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
    }
}
