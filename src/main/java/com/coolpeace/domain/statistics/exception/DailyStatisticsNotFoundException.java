package com.coolpeace.domain.statistics.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class DailyStatisticsNotFoundException extends ApplicationException {
    public DailyStatisticsNotFoundException(){super(ErrorCode.DAILY_STATISTICS_NOT_FOUND);}
}
