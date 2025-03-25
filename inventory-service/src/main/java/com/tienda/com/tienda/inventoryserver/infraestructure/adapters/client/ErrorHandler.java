package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

public class ErrorHandler {

    /**
     * Method to extract the error message from a FeignException
     *
     * @param e FeignException to extract the error message from
     * @return Extracted error message, or a default message if extraction fails
     */
    public static String extractErrorMessage(FeignException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(e.contentUTF8());
            return jsonNode.has("userMessage") ? jsonNode.get("userMessage").asText() : "Error desconocido";
        } catch (Exception ex) {
            return "Error desconocido";
        }
    }
}
