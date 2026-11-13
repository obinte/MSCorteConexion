package com.acosux.MSCorteConexion.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * US-269: token de servicio hacia el backend, firmado con la llave nueva (HS256, 32 bytes)
 * que se lee de Secrets Manager, a nombre del usuario de sistema (por defecto {@code quartz}).
 * Sin llave no se firma nada: no hay vuelta atrás a la llave vieja.
 *
 * <p>Propiedades por contexto (nunca el valor de la llave):</p>
 * <ul>
 * <li>{@code acosux.jwt.secret.name}: secreto de AWS (por defecto {@code ms.acosux.com}).</li>
 * <li>{@code acosux.jwt.secret.key}: clave dentro del secreto según el backend de destino
 * ({@code jwt.secret.data}, {@code jwt.secret.mondragon} o {@code jwt.secret.palmar}).</li>
 * <li>{@code acosux.jwt.usuario}: nick del usuario de sistema en la base de destino
 * (por defecto {@code quartz}; {@code QUARTZ} en shrimp y cameron).</li>
 * </ul>
 */
@Component
public class TokenServicio {

    private static final Logger logger = LoggerFactory.getLogger(TokenServicio.class);
    static final String SERVICIO = "corte";
    static final long VALIDEZ_MS = 10 * 60 * 1000L;
    static final int LONGITUD_MINIMA_LLAVE = 32;

    @Value("${acosux.jwt.secret.name:ms.acosux.com}")
    private String nombreSecreto;
    @Value("${acosux.jwt.secret.key:}")
    private String claveSecreto;
    @Value("${acosux.jwt.usuario:quartz}")
    private String usuario;

    private volatile byte[] llave;

    @PostConstruct
    public void cargar() {
        if (claveSecreto == null || claveSecreto.trim().isEmpty()) {
            logger.error("[TOKEN-SERVICIO] Falta acosux.jwt.secret.key: este contexto no podrá llamar al backend");
            return;
        }
        llave = decodificar(new SecretsProvider(nombreSecreto).get(claveSecreto.trim()));
        if (llave == null) {
            logger.error("[TOKEN-SERVICIO] Sin llave válida en '{}' / '{}': este contexto no podrá llamar al backend",
                    nombreSecreto, claveSecreto);
        } else {
            logger.info("[TOKEN-SERVICIO] Llave cargada de '{}' / '{}'; usuario={}, servicio={}",
                    nombreSecreto, claveSecreto, usuario, SERVICIO);
        }
    }

    static byte[] decodificar(String valorBase64) {
        if (valorBase64 == null) {
            return null;
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(valorBase64.trim());
            return bytes.length >= LONGITUD_MINIMA_LLAVE ? bytes : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Cabecera {@code Authorization} completa ({@code "Bearer " + token}). */
    public String autorizacion() {
        return "Bearer " + crearToken();
    }

    String crearToken() {
        byte[] actual = llave;
        if (actual == null) {
            throw new IllegalStateException("[TOKEN-SERVICIO] Sin llave para firmar el token de servicio "
                    + "(revisar acosux.jwt.secret.name / acosux.jwt.secret.key y el secreto)");
        }
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDEZ_MS))
                .setSubject(usuario)
                .claim("userId", usuario)
                .claim("servicio", SERVICIO)
                .signWith(SignatureAlgorithm.HS256, actual)
                .compact();
    }

    // Para pruebas manuales/unitarias.
    void configurar(byte[] llave, String usuario) {
        this.llave = llave;
        this.usuario = usuario;
    }
}
