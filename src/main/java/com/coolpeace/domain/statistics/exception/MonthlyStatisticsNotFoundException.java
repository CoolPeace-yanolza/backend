package com.coolpeace.domain.statistics.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class MonthlyStatisticsNotFoundException extends ApplicationException {
    public MonthlyStatisticsNotFoundException(){super(ErrorCode.MONTHLY_STATISTICS_NOT_FOUND);}
}
