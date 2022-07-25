/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSCorteConexion.service;

import com.acosux.MSCorteConexion.util.RespuestaWebTO;
import com.acosux.MSCorteConexion.util.UtilsJSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author mario
 */
@Service
public class CorteConexionServiceImpl implements CorteConexionService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${acosux.endpoint}")
    private String ENDPOINT;
    private static final long VALIDITY_TIME_MS = 10 * 24 * 60 * 60 * 1000;// 10 days Validity
    private final String secret = "mrin";

    public HttpEntity<String> createTokenForUser(Map<String, Object> map) throws Exception {
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject("admin")
                .claim("userId", "admin")
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<String> entity = new HttpEntity<>(UtilsJSON.objetoToJson(map), headers);
        return entity;
    }

    @Override
    public RespuestaWebTO getCarListaCuentasPorCobrarDetalladoTOCortesConexion(Map<String, Object> map) throws Exception {
        System.out.println("==> LLEGAMOS AL SERVICIO getCarListaCuentasPorCobrarDetalladoTOCortesConexion");
        RespuestaWebTO res = new RespuestaWebTO();
        try {
            res = restTemplate.postForObject(ENDPOINT + "/todocompuWS/carteraWebController/listarCuentasCobrarCorteConexionMS", createTokenForUser(map), RespuestaWebTO.class);
            System.out.println("==> REGRESAMOS CON LA RESPUESTA DESDE EL SERVIDOR: " + (res != null ? res.getMessage().toString() : "SIN RESPUESTA"));
        } catch (RestClientException e) {
            res.setMessage(e.getMessage() != null ? e.getMessage() : "Error desconocido");
        }
        return res;
    }

}
