package com.base.usecases.events;

import android.support.annotation.NonNull;

import com.base.abstraction.system.ResourcesReader;
import com.base.usecases.requesters.server.websocket.SocketMode;
import com.base.usecases.requesters.server.ssl.params.Parameter;
import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.base.usecases.requesters.server.ssl.params.PathVariableGroup;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/23/2016.
 */

public class SocketRequestMessage extends RequestMessage {

    private SocketMode socketMode;

    protected SocketRequestMessage(long id, Object content, ParametersGroup parametersGroup
            , PathVariableGroup pathVariablesGroup, SocketMode mode) {
        super(id, content, parametersGroup, pathVariablesGroup);
        this.socketMode = mode;
    }

    public SocketMode getSocketMode() {
        return socketMode;
    }

    @Override
    public String toString() {
        return "[id=" +
                new ResourcesReader().execute(getId()) +
                ", socketMode=" +
                socketMode +
                "]";
    }

    @Override
    public SocketRequestMessage.Builder copyBuilder() {
        return new SocketRequestMessage.Builder(getSocketMode())
                .id(getId())
                .content(getContent())
                .parametersGroup(getParametersGroup())
                .pathVariablesGroup(getPathVariablesGroup());

    }

    public static class Builder extends RequestMessage.Builder {

        private SocketMode socketMode;

        public Builder(@NonNull SocketMode socketMode) {
            this.socketMode = socketMode;
        }

        @Override
        public Builder parameter(Parameter parameter) {
            super.parameter(parameter);
            return this;
        }

        @Override
        public Builder id(long id) {
            super.id(id);
            return this;
        }

        @Override
        public Builder content(Object content) {
            super.content(content);
            return this;
        }

        @Override
        public Builder parametersGroup(ParametersGroup parametersGroup) {
            super.parametersGroup(parametersGroup);
            return this;
        }

        @Override
        public Builder pathVariablesGroup(PathVariableGroup variableGroup) {
            super.pathVariablesGroup(variableGroup);
            return this;
        }

        @Override
        public Builder pathVariable(Serializable pathVariable) {
            super.pathVariable(pathVariable);
            return this;
        }

        @Override
        public SocketRequestMessage build() {
            return new SocketRequestMessage(id
                    , content
                    , parametersGroup
                    , pathVariablesGroup
                    , socketMode);
        }


    }
}
