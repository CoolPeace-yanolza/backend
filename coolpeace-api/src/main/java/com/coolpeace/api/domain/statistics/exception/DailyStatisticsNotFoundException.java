package com.coolpeace.api.domain.statistics.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class DailyStatisticsNotFoundException extends ApplicationException {
    public DailyStatisticsNotFoundException(){super(ErrorCode.DAILY_STATISTICS_NOT_FOUND);}
}
