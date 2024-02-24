package com.bezina.myNotes.validators;

import org.hibernate.loader.ast.internal.CollectionLoaderSubSelectFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation {
    Logger LOG = LoggerFactory.getLogger(ResponseErrorValidation.class);

    public ResponseEntity<Object> mapValidationService(BindingResult result)
    {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(result.getAllErrors())) {
                LOG.info("ResponseErrorValidation class has errors");

                for (ObjectError error : result.getAllErrors()){
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                    LOG.error( error.toString()+"///"+error.getCode()+" "+error.getDefaultMessage());
                }
                for (FieldError error : result.getFieldErrors()) {
                    errorMap.put(error.getField(), error.getDefaultMessage());
            }
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
