/*
 * Copyright 2016 Kukri Máté
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package openmcauth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;
import openmcauth.exception.MinecraftAuthenticationException;
import openmcauth.json.AuthPayload;
import openmcauth.json.AuthResponse;
import openmcauth.json.ErrorResponse;
import openmcauth.json.RefreshPayload;
import openmcauth.json.RefreshResponse;
import openmcauth.provider.LoggingProvider;
import openmcauth.provider.TokenStorageProvider;
import openmcauth.uuid.UUIDConverter;

public class MinecraftAuthenticationService {
    private static URL AUTHENTICATE_URL;
    private static URL REFRESH_URL;
    
    private final Gson gson;
    private final LoggingProvider loggingProvider;
    private final TokenStorageProvider storageProvider;
    private boolean doTokenLogin = true;
    private UUID clientToken = null;
    private UUID accessToken = null;
    
    public MinecraftAuthenticationService(LoggingProvider loggingProvider, TokenStorageProvider storageProvider) {
        try {
            AUTHENTICATE_URL = new URL("https://authserver.mojang.com/authenticate");
            REFRESH_URL = new URL("https://authserver.mojang.com/refresh");
        } catch (Exception e) { 
            System.err.println("Invalid static URL in openmcauth! Please recompile openmcauth with the correct URL!"); 
            System.exit(1); 
        }
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UUID.class, new UUIDConverter());
        gson = gsonBuilder.create();
        
        this.loggingProvider = loggingProvider;
        this.storageProvider = storageProvider;
        
        if ((this.clientToken = this.storageProvider.readClientToken()) == null) {
            this.clientToken = UUID.randomUUID();
            this.storageProvider.writeClientToken(this.clientToken);
            
            // Don't do token login in case of new client tokens
            doTokenLogin = false;
        }
        
        if ((this.accessToken = this.storageProvider.readAccessToken()) == null && doTokenLogin) {
            // Don't do token login if access token is null
            doTokenLogin = false;
        }    
    }
    
    public void resetService() {
        this.storageProvider.writeClientToken(this.clientToken = UUID.randomUUID());
        this.storageProvider.writeAccessToken(this.accessToken = null);
        this.doTokenLogin = false;
    }
    
    public void resetSession() {
        this.storageProvider.writeAccessToken(this.accessToken = null);
        this.doTokenLogin = false;
    }
    
    public MinecraftAuthenticationData doPasswordLogin(String username, String password) throws MinecraftAuthenticationException {
        this.loggingProvider.info("Logging in with username and password");
        AuthPayload payload = new AuthPayload(username, password, this.clientToken);
        AuthResponse response = sendJsonData(AUTHENTICATE_URL, payload, AuthResponse.class);
        this.storageProvider.writeAccessToken(this.accessToken = response.getAccessToken());
        this.doTokenLogin = true;
        return new MinecraftAuthenticationData(response.getSelectedProfile(), response.getAccessToken());
    }
    
    public MinecraftAuthenticationData doTokenLogin() throws MinecraftAuthenticationException {
        if (!doTokenLogin)
            return null;
        
        this.loggingProvider.info("Logging in with token");
        RefreshPayload payload = new RefreshPayload(this.accessToken, this.clientToken);
        RefreshResponse response = sendJsonData(REFRESH_URL, payload, RefreshResponse.class);
        return new MinecraftAuthenticationData(response.getSelectedProfile(), response.getAccessToken());
    }
    
    private <T> T sendJsonData(URL url, Object payload, Class<T> classOfT) throws MinecraftAuthenticationException {
        return this.gson.fromJson(sendPost(url, "application/json", gson.toJson(payload)), classOfT);
    }
    
    private String sendPost(URL url, String contentType, String data) throws MinecraftAuthenticationException {
        byte[] rawData = data.getBytes(Charset.forName("UTF-8"));
        
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        } catch (Exception e) {
            throw new MinecraftAuthenticationException("Can't connect to the server: " + e.getMessage());
        }
        
        conn.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        conn.setRequestProperty("Content-Lenght", "" + rawData.length);
        conn.setDoOutput(true);
        
        OutputStream os = null;
        
        try {
            os = conn.getOutputStream();
            os.write(rawData);
            os.flush();
        } catch (Exception e) {
            throw new MinecraftAuthenticationException("Error opening output stream: " + e.getMessage());
        } finally {
            closeOutputStreamWithoutCare(os);
        }
        
        InputStream is = null;
        
        try  {
            is = conn.getInputStream();
            return inputStreamToString(is);
        } catch(Exception e) {
            is = conn.getErrorStream();
            ErrorResponse error;
            try {
                error = this.gson.fromJson(inputStreamToString(is), ErrorResponse.class);
            } catch (JsonSyntaxException|IOException e2) {
                throw new MinecraftAuthenticationException("Error reading error stream: " + e2.getMessage());
            }
            throw new MinecraftAuthenticationException(error.getErrorMessage());
        } finally {
            closeInputStreamWithoutCare(is);
        }
    }
    
    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        char[] buffer = new char[1024];
        
        try {
            inputStreamReader = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Your system doesn't support UTF-8! openmcauth needs UTF-8 support! Exiting!");
            System.exit(1);
        }
        
        for (;;) {
            int numberOfBytes = inputStreamReader.read(buffer, 0, buffer.length);
            if (numberOfBytes == -1)
                break;
            stringBuilder.append(buffer, 0, numberOfBytes);
        }
        
        return stringBuilder.toString();
    }
    
    private void closeOutputStreamWithoutCare(OutputStream os) {
        try {
            os.close();
        } catch (Exception e) {}
    }
    
    private void closeInputStreamWithoutCare(InputStream is) {
        try {
            is.close();
        } catch (Exception e) {}
    }
}
