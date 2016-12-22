package com.globo.globosat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by raphael on 21/12/16.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Network.")
public class NetworkNotFoundException extends RuntimeException{
}
