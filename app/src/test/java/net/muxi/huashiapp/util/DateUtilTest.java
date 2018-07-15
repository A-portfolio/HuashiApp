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
    public void toDate() throws Exception {

    }

    @Test
    public void toToday() throws Exception {

    }

    @Test
    public void toDateInYear() throws Exception {

    }

    @Test
    public void toWeek() throws Exception {

    }

    @Test
    public void toWeek1() throws Exception {

    }

    @Test
    public void parseDateInYear() throws Exception {

    }

    @Test
    public void getDayBetweenDates() throws Exception {

    }

    @Test
    public void getDistanceWeek() throws Exception {

    }

    @Test
    public void getTheDateInYear() throws Exception {

    }

    @Test
    public void getTheDate() throws Exception {

    }

    @Test
    public void getToday() throws Exception {

    }

    @Test
    public void getWeek() throws Exception {

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
    public void getTheWeekDate() throws Exception {

    }

    @Test
    public void isAfter() throws Exception {
    }

    @Test
    public void getSecondSpace() throws Exception {

    }

}