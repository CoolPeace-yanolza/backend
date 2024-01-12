package com.coolpeace.core.domain.statistics.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class DailyStatisticsNotFoundException extends ApplicationException {
    public DailyStatisticsNotFoundException(){super(ErrorCode.DAILY_STATISTICS_NOT_FOUND);}
}
