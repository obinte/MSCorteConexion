/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSCorteConexion.util;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author developer3
 */
public class RespuestaWebTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
    
    
}
