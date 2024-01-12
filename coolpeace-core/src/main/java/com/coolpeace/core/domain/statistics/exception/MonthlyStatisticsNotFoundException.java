package com.coolpeace.core.domain.statistics.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class MonthlyStatisticsNotFoundException extends ApplicationException {
    public MonthlyStatisticsNotFoundException(){super(ErrorCode.MONTHLY_STATISTICS_NOT_FOUND);}
}
