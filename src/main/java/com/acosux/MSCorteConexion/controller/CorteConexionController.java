/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSCorteConexion.controller;

import com.acosux.MSCorteConexion.util.RespuestaWebTO;
import com.acosux.MSCorteConexion.util.UtilsJSON;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.acosux.MSCorteConexion.service.CorteConexionService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author mario
 */
@RestController
@RequestMapping("/corteConexionController/")
public class CorteConexionController {

    @Autowired
    CorteConexionService corteConexionService;

    @RequestMapping("/listarCuentasCobrarCorteConexion")
    public RespuestaWebTO listarCuentasCobrarCorteConexion(HttpServletRequest request, @RequestBody String json) throws Exception {
        System.out.println("==> LLEGAMOS AL INICIO listarCuentasCobrarCorteConexion");
        System.out.println("JSON: " + json);
        RespuestaWebTO resp = new RespuestaWebTO();
        if (json != null) {
            json = json.substring(8, json.length());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("empresa", "FSN");
        String cliente = json;
        try {
            System.out.println("==> RECIBIMOS LOS PARAMETROS, CLIENTE: " + cliente);
            if (cliente != null && !cliente.equals("")) {
                Date anio = new Date();

                ZoneId timeZone = ZoneId.systemDefault();
                LocalDate getLocalDate = anio.toInstant().atZone(timeZone).toLocalDate();

                String anioActual = getLocalDate.getYear() + "";
                map.put("desde", anioActual + "-01-01");
                map.put("hasta", anioActual + "-12-31");
                map.put("ichfa", false);

                System.out.println("==> A PUNTO DE IRSE A SERVICIO: " + cliente);
                resp = corteConexionService.getCarListaCuentasPorCobrarDetalladoTOCortesConexion(map);

            } else {
                resp.setMessage("Debe ingresar un código de cliente.");
            }
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
        }
        return resp;
    }
}
