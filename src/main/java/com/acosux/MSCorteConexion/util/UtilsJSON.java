/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSCorteConexion.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mario
 */
public class UtilsJSON {

    private static final ObjectMapper om = new ObjectMapper();

    public static String objetoToJson(Object obj) throws JsonProcessingException {
        return om.writeValueAsString(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(Class<T> clase, Object json) {
        try {
            return (List<T>) om.readValue(objetoToJson(json),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, clase));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> jsonToMap(String json) {
        try {
            return om.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObjeto(Class<T> clase, String json) {
        try {
            return (T) om.readValue(json, clase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObjeto(Class<T> clase, Object json) {
        try {
            return (T) om.readValue(objetoToJson(json), clase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
