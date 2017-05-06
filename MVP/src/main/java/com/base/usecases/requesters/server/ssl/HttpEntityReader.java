package com.base.usecases.requesters.server.ssl;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * a class that reads {@link HttpEntity} and gets the response as a {@link String}
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
class HttpEntityReader implements Command<HttpEntity, String> {


    @Override
    public String execute(HttpEntity entity) {
        if (entity == null) {
            return null;
        }

        InputStream is;
        BufferedReader reader;
        try {
            is = entity.getContent();
        } catch (IOException e) {
            throw new RuntimeException("failed to invoke HttpEntity.getEntity()");
        }

        reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            logReadEntityLineError(line, e);
        } finally {

            try {
                is.close();
            } catch (IOException e) {
                logCloseEntityInputStreamError(e);
            }

            try {
                reader.close();
            } catch (IOException e) {
                logCloseEntityReaderStreamError(e);
            }
        }
        return sb.toString();
    }


    private void logReadEntityLineError(String line, IOException e) {
        Logger.getInstance().error(getClass(), "failed to read entity line .. " +
                "current line is : " + line);
        Logger.getInstance().exception(e);
    }

    private void logCloseEntityInputStreamError(IOException e) {
        Logger.getInstance().error(getClass(), "failed to close HttpEntity.getEntity() " +
                "InputStream");
        Logger.getInstance().exception(e);
    }

    private void logCloseEntityReaderStreamError(IOException e) {
        Logger.getInstance().error(getClass(), "failed to close HttpEntity.getEntity() " +
                "BufferedReader");
        Logger.getInstance().exception(e);
    }

}
