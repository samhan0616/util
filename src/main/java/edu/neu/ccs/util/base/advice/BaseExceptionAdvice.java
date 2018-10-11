package edu.neu.ccs.util.base.advice;

import edu.neu.ccs.exception.BaseException;
import edu.neu.ccs.util.http.ResponseMessage;
import edu.neu.ccs.util.http.ResponseMessageCodeEnum;
import edu.neu.ccs.util.http.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
//@Order(value=4)
public class BaseExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(BaseExceptionAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseMessage handlerAdcDaBaseException(BaseException exception) {
        logger.warn(exception.getMessage(), exception);
        return Result.error(exception.getErrorCode(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMessage handlerAdcDaBaseException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return Result.error(ResponseMessageCodeEnum.ERROR.getCode(), "程序异常，请重试。如果重复出现请联系管理员处理！");
    }

}
