package tm.salam.TmBookmaker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;

import java.net.ConnectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${sms-service.host}")
    private String smsServiceHost;
    @Value("${sms-service.authorization}")
    private String smsServiceAuthorization;

    @Override
    public ResponseTransfer<?> sendSms(final String phoneNumber, final String message) throws HttpClientErrorException,
            ConnectException, ResourceAccessException {

        final ResponseTransfer<?>responseTransfer;
        final RestTemplate restTemplate=new RestTemplate();
        final HttpHeaders httpHeaders=new HttpHeaders();

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", smsServiceAuthorization);
        final Map<String, String> body=new HashMap<>();

        body.put("phoneNumber", phoneNumber);
        body.put("message", message);
        HttpEntity<Map<String, String>> httpRequest=new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(smsServiceHost, HttpMethod.POST, httpRequest, String.class);

        if(response.getStatusCode().is2xxSuccessful()){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error sms service don't work")
                            .build())
                    .build();
        }
        return responseTransfer;
    }

}
