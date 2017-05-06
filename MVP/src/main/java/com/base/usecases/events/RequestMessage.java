package com.base.usecases.events;

import com.base.abstraction.events.Message;
import com.base.abstraction.system.ResourcesReader;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.server.ssl.params.Parameter;
import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.base.usecases.requesters.server.ssl.params.PathVariableGroup;

import java.io.Serializable;

/**
 * a {@link Message} Object that is used for Messages that can hold requests from an
 * {@link EntityGateway} throw a
 * {@link com.base.presentation.repos.base.Repository} or any layer above
 * <p/>
 * Created by Wafaa on 10/20/2016.
 */

public class RequestMessage extends Message {

    private final ParametersGroup parametersGroup;
    private PathVariableGroup pathVariablesGroup;

    protected RequestMessage(long id, Object content, ParametersGroup parametersGroup,
                             PathVariableGroup pathVariablesGroup) {
        super(id, content);
        this.parametersGroup = parametersGroup;
        this.pathVariablesGroup = pathVariablesGroup;
    }

    public ParametersGroup getParametersGroup() {
        return parametersGroup;
    }

    public PathVariableGroup getPathVariablesGroup() {
        return pathVariablesGroup;
    }

    @Override
    public String toString() {
        return "[id=" +
                new ResourcesReader().execute(getId()) +
                ", pathVariablesGroup=" +
                pathVariablesGroup +
                ", parametersGroup=" +
                parametersGroup +
                ", content=" +
                String.valueOf(getContent()).trim() +
                "]";
    }

    @Override
    public RequestMessage.Builder copyBuilder() {
        return new RequestMessage.Builder()
                .id(getId())
                .content(getContent())
                .parametersGroup(parametersGroup)
                .pathVariablesGroup(pathVariablesGroup);
    }


    public static class Builder extends Message.Builder {

        protected ParametersGroup parametersGroup;
        protected PathVariableGroup pathVariablesGroup;

        public Builder parameter(Parameter parameter) {
            if (parametersGroup == null) {
                parametersGroup = new ParametersGroup();
            }
            parametersGroup.add(parameter);
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

        public Builder parametersGroup(ParametersGroup parametersGroup) {
            this.parametersGroup = parametersGroup;
            return this;
        }

        public Builder pathVariablesGroup(PathVariableGroup variableGroup) {
            this.pathVariablesGroup = variableGroup;
            return this;
        }

        public Builder pathVariable(Serializable pathVariable) {
            if (pathVariablesGroup == null) {
                pathVariablesGroup = new PathVariableGroup();
            }
            pathVariablesGroup.add(pathVariable);
            return this;
        }

        @Override
        public RequestMessage build() {
            return new RequestMessage(id, content, parametersGroup, pathVariablesGroup);
        }

    }

}
