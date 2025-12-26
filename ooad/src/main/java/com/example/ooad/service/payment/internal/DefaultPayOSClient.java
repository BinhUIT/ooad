package com.example.ooad.service.payment.internal;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultPayOSClient implements PayOSClient {
    private final RestTemplate restTemplate;

    private final String baseUrl;
    private final String createEndpoint;
    private final String infoEndpoint;
    private final String cancelEndpoint;
    private final String clientId;
    private final String apiKey;
    private final String checksumKey;

    public DefaultPayOSClient(RestTemplate restTemplate,
            @Value("${payos.base-url:https://api-merchant.payos.vn/v2}") String baseUrl,
            @Value("${payos.endpoint.create:/payment-requests}") String createEndpoint,
            @Value("${payos.endpoint.info:/payment-requests/{orderCode}}") String infoEndpoint,
            @Value("${payos.endpoint.cancel:/payment-requests/{orderCode}/cancel}") String cancelEndpoint,
            @Value("${payos.client-id:}") String clientId,
            @Value("${payos.api-key:}") String apiKey,
            @Value("${payos.checksum-key:}") String checksumKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.createEndpoint = sanitizeEndpoint(createEndpoint);
        this.infoEndpoint = sanitizeEndpoint(infoEndpoint);
        this.cancelEndpoint = sanitizeEndpoint(cancelEndpoint);
        this.clientId = clientId;
        this.apiKey = apiKey;
        this.checksumKey = checksumKey;
    }

    @Override
    public PayOSCheckoutResponse createPaymentLink(PayOSCreateRequest request) {
        String url = baseUrl + createEndpoint;
        
        // Generate signature for PayOS API v2
        String signature = generateSignature(
            request.getAmount(),
            request.getCancelUrl(),
            request.getDescription(),
            request.getOrderCode(),
            request.getReturnUrl()
        );
        
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderCode", request.getOrderCode());
        payload.put("amount", request.getAmount());
        payload.put("description", request.getDescription());
        payload.put("returnUrl", request.getReturnUrl());
        payload.put("cancelUrl", request.getCancelUrl());
        payload.put("signature", signature);
        
        // Items are optional for PayOS v2
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            payload.put("items", request.getItems().stream().map(this::mapItem).collect(Collectors.toList()));
        }

        log.info("PayOS Request URL: {}", url);
        log.info("PayOS Request payload: {}", payload);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, buildHeaders());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        
        log.info("PayOS Response: {}", response.getBody());
        
        return PayOSCheckoutResponse.from(response.getBody());
    }

    @Override
    public PayOSPaymentLinkResponse getPaymentLink(Long orderCode) {
        String resolved = buildUrl(infoEndpoint, orderCode);
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(resolved, HttpMethod.GET, entity, Map.class);
        return PayOSPaymentLinkResponse.from(response.getBody());
    }

    @Override
    public PayOSPaymentLinkResponse cancelPaymentLink(Long orderCode, String cancellationReason) {
        String resolved = buildUrl(cancelEndpoint, orderCode);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("cancellationReason", cancellationReason != null ? cancellationReason : "User cancelled");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, buildHeaders());
        ResponseEntity<Map> response = restTemplate.postForEntity(resolved, entity, Map.class);
        return PayOSPaymentLinkResponse.from(response.getBody());
    }

    private Map<String, Object> mapItem(PayOSCreateRequest.Item item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", item.getName());
        map.put("quantity", item.getQuantity());
        map.put("price", item.getPrice());
        return map;
    }

    private String buildUrl(String endpoint, Object orderCode) {
        if (endpoint.contains("{orderCode}")) {
            return baseUrl + endpoint.replace("{orderCode}", orderCode.toString());
        }
        return baseUrl + endpoint;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (clientId != null && !clientId.isBlank()) {
            headers.set("x-client-id", clientId);
        }
        if (apiKey != null && !apiKey.isBlank()) {
            headers.set("x-api-key", apiKey);
        }
        return headers;
    }
    
    /**
     * Generate HMAC-SHA256 signature for PayOS API v2
     * Data format: amount=$amount&cancelUrl=$cancelUrl&description=$description&orderCode=$orderCode&returnUrl=$returnUrl
     */
    private String generateSignature(int amount, String cancelUrl, String description, long orderCode, String returnUrl) {
        try {
            String data = String.format("amount=%d&cancelUrl=%s&description=%s&orderCode=%d&returnUrl=%s",
                    amount, cancelUrl, description, orderCode, returnUrl);
            
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKey);
            
            byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            log.info("PayOS signature data: {}", data);
            log.info("PayOS signature: {}", hexString.toString());
            
            return hexString.toString();
        } catch (Exception e) {
            log.error("Error generating PayOS signature", e);
            throw new RuntimeException("Failed to generate PayOS signature", e);
        }
    }

    private String sanitizeEndpoint(String endpoint) {
        if (endpoint == null || endpoint.isBlank()) {
            return "";
        }
        return endpoint.startsWith("/") ? endpoint : "/" + endpoint;
    }
}
