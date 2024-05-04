package tm.salam.TmBookmaker.services;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;

import java.net.ConnectException;

public interface SmsService {

    ResponseTransfer<?> sendSms(String phoneNumber, String message) throws HttpClientErrorException,
            ConnectException, ResourceAccessException;

}
