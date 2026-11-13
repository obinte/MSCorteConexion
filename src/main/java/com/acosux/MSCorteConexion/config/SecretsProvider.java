package com.acosux.MSCorteConexion.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * US-269: lee un secreto de AWS Secrets Manager (JSON plano de claves) con las
 * credenciales del servidor. Mismo modelo que MSVitapro, con parser JSON real.
 */
public class SecretsProvider {

    private static final Logger logger = LoggerFactory.getLogger(SecretsProvider.class);
    private static final String REGION = "us-east-1";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonNode secreto;

    public SecretsProvider(String nombreSecreto) {
        try {
            AWSSecretsManager cliente = AWSSecretsManagerClientBuilder.standard().withRegion(REGION).build();
            try {
                String json = cliente.getSecretValue(new GetSecretValueRequest().withSecretId(nombreSecreto)).getSecretString();
                JsonNode nodo = MAPPER.readTree(json);
                this.secreto = nodo != null && nodo.isObject() ? nodo : null;
            } finally {
                cliente.shutdown();
            }
            if (secreto == null) {
                logger.error("[SecretsProvider] El secreto '{}' no es un objeto JSON", nombreSecreto);
            }
        } catch (Exception e) {
            logger.error("[SecretsProvider] No se pudo leer el secreto '{}': {}", nombreSecreto, e.getMessage());
            this.secreto = null;
        }
    }

    /** Valor de texto de la clave, o null si el secreto no cargó o la clave no existe o está vacía. */
    public String get(String clave) {
        if (secreto == null) {
            return null;
        }
        JsonNode valor = secreto.get(clave);
        if (valor == null || !valor.isTextual() || valor.asText().trim().isEmpty()) {
            return null;
        }
        return valor.asText();
    }
}
