package com.dum.dodam.Community;

import java.util.Date;

public class TIME_MAXIMUM
{
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public String calculateTime(Date date)
    {

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC)
        {
            // sec
            msg = diffTime + "초전";
        }
        else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN)
        {
            // min
            System.out.println(diffTime);

            msg = diffTime + "분전";
        }
        else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR)
        {
            // hour
            msg = (diffTime ) + "시간전";
        }
        else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY)
        {
            // day
            msg = (diffTime ) + "일전";
        }
        else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH)
        {
            // day
            msg = (diffTime ) + "달전";
        }
        else
        {
            msg = (diffTime) + "년전";
        }

        return msg;
    }

    public int calculateTime(Date start, Date end)
    {

        long curTime = start.getTime();
        long regTime = end.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        diffTime /= TIME_MAXIMUM.SEC;
        diffTime /= TIME_MAXIMUM.MIN;
        if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY)
        {
            // day
            return (int)diffTime;
        }

        return (int)diffTime;
    }
}


