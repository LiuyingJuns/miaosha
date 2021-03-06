package com.miaosha.validation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    /**
     * 实现校验方法并返回校验结果
     * @param bean
     */
    public ValidationResult validate(Object bean){
         ValidationResult result = new ValidationResult();

        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);

        if(constraintViolationSet.size()>0){
            result.setError(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrorMsg().put(propertyName,errMsg);

            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //实例化validator
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
