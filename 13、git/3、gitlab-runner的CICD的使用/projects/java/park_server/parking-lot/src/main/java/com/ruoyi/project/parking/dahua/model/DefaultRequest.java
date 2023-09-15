package com.ruoyi.project.parking.dahua.model;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.AbstractIccRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;

public class DefaultRequest extends AbstractIccRequest<GeneralResponse> {

    public DefaultRequest(String url, Method method) throws ClientException {
        super(url, method);
    }

    @Override
    public Class<GeneralResponse> getResponseClass() {
        return GeneralResponse.class;
    }
}
