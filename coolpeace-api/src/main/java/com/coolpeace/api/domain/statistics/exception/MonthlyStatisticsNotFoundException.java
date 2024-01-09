package com.coolpeace.api.domain.statistics.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class MonthlyStatisticsNotFoundException extends ApplicationException {
    public MonthlyStatisticsNotFoundException(){super(ErrorCode.MONTHLY_STATISTICS_NOT_FOUND);}
}
