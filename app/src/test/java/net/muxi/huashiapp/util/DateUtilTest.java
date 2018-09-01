package net.muxi.huashiapp.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/9
 */
public class DateUtilTest {
    @Test
    public void toDate() {

    }

    @Test
    public void toToday() {

    }

    @Test
    public void toDateInYear() {

    }

    @Test
    public void toWeek() {

    }

    @Test
    public void toWeek1() {

    }

    @Test
    public void parseDateInYear() {

    }

    @Test
    public void getDayBetweenDates() {

    }

    @Test
    public void getDistanceWeek() {

    }

    @Test
    public void getTheDateInYear() {

    }

    @Test
    public void getTheDate() {

    }

    @Test
    public void getToday() {

    }

    @Test
    public void getWeek() {

    }

    @Test
    public void getDayInWeek() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int mondayDate = 6;

        Date[] dates = new Date[7];
        for (int i = 0;i < 7;i ++){
            dates[i] = format.parse(String.format("2017-3-%d",mondayDate));
            assertEquals(i + 1,DateUtil.getDayInWeek(dates[i]));
            mondayDate ++;
        }
    }

    @Test
    public void getTheWeekDate() {

    }

    @Test
    public void isAfter() {
    }

    @Test
    public void getSecondSpace() {

    }

}