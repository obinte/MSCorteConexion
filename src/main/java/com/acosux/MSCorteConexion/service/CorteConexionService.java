/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSCorteConexion.service;

import com.acosux.MSCorteConexion.util.RespuestaWebTO;
import java.util.Map;

/**
 *
 * @author mario
 */
public interface CorteConexionService {

    RespuestaWebTO getCarListaCuentasPorCobrarDetalladoTOCortesConexion(Map<String, Object> map) throws Exception;

}
