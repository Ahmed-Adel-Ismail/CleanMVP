package com.base.usecases.requesters.server.ssl;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.TestException;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.requesters.server.ssl.params.Parameter;
import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.base.usecases.requesters.server.ssl.params.PathVariableGroup;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * a class that appends the Path variables and Parameters to a URL String
 * <p>
 * Created by Wafaa on 10/20/2016.
 */
@SuppressWarnings("deprecation")
public class HttpUrlGenerator implements Command<Message, String> {

    private String url;

    public HttpUrlGenerator(String url) {
        this.url = url;
    }

    @Override
    public String execute(Message message) {
        if (message instanceof RequestMessage) {
            try {
                url = addPathVariablesAndParametersToUrl(url, (RequestMessage) message);
            } catch (UnsupportedEncodingException e) {
                new TestException().execute(e);
            }
        }
        return url;
    }

    private String addPathVariablesAndParametersToUrl(String url, RequestMessage message)
            throws UnsupportedEncodingException {
        url = addPathVariablesToUrl(url, message);
        url = addParametersToUrl(url, message);
        return url;
    }


    private String addPathVariablesToUrl(String url, RequestMessage requestMessage) {
        PathVariableGroup variableGroup = requestMessage.getPathVariablesGroup();
        if (variableGroup != null && !variableGroup.isEmpty()) {
            StringBuilder finalURL = new StringBuilder();
            finalURL.append(url);
            for (Serializable variable : variableGroup) {
                finalURL.append("/");
                finalURL.append(variable);
            }
            return finalURL.toString();
        }
        return url;
    }

    private String addParametersToUrl(String url, RequestMessage requestMessage)
            throws UnsupportedEncodingException {
        ParametersGroup parameters = requestMessage.getParametersGroup();
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder finalURL = new StringBuilder();
            finalURL.append(url);
            finalURL.append("?");
            for (Parameter p : parameters) {
                finalURL.append(p.getKey());
                finalURL.append("=");
                finalURL.append(p.getValue());
                finalURL.append("&");
            }
            finalURL.deleteCharAt(finalURL.toString().length() - 1);
            return finalURL.toString();
        }
        return url;
    }
}
