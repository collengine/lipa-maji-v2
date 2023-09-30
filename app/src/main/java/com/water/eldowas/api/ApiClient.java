package com.water.eldowas.api;

import com.water.eldowas.api.interceptor.AccessTokenIterceptor;
import com.water.eldowas.api.interceptor.AuthInterceptor;
import com.water.eldowas.api.services.STKPushService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.water.eldowas.util.AppConstants.BASE_URL;
import static com.water.eldowas.util.AppConstants.CONNECT_TIMEOUT;
import static com.water.eldowas.util.AppConstants.READ_TIMEOUT;
import static com.water.eldowas.util.AppConstants.WRITE_TIMEOUT;

/**
 * API Client helper class used to configure Retrofit object.
 *
 * @author Thomas Kioko
 */

public class ApiClient {

    private Retrofit retrofit;
    private boolean isDebug;
    private boolean isGetAccessToken;
    private String mAuthToken;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    /**
     * Set the {@link Retrofit} log level. This allows one to view network traffic.
     *
     * @param isDebug If true, the log level is set to
     *                {@link HttpLoggingInterceptor.Level#BODY}. Otherwise
     *                {@link HttpLoggingInterceptor.Level#NONE}.
     */
    public ApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    /**
     * Helper method used to set the authenication Token
     *
     * @param authToken token from api
     */
    public ApiClient setAuthToken(String authToken) {
        mAuthToken = authToken;
        return this;
    }

    /**
     * Helper method used to determine if get token enpoint has been invoked. This should be called
     * only when requesting of an accessToken
     *
     * @param getAccessToken {@link Boolean}
     */
    public ApiClient setGetAccessToken(boolean getAccessToken) {
        isGetAccessToken = getAccessToken;
        return this;
    }

    /**
     * Configure OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        try {
            okHttpClient.sslSocketFactory(new TLSSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }
    private OkHttpClient okHttpCliens() {
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            okHttpClient = new OkHttpClient.Builder().sslSocketFactory(new TLSSocketFactory()).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        okHttpClient
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }
    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint and a {@link HttpLoggingInterceptor} which adds the API key as query param.
     */
    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okhttpBuilder = okHttpClient();

        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(new AccessTokenIterceptor());
        }

        if (mAuthToken != null && !mAuthToken.isEmpty()) {
            okhttpBuilder.addInterceptor(new AuthInterceptor(mAuthToken));
        }

        builder.client(okhttpBuilder.build());

        retrofit = builder.build();

        return retrofit;
    }

    /**
     * Create service instance.
     *
     * @return STKPushService Service.
     */
    public STKPushService mpesaService() {
        return getRestAdapter().create(STKPushService.class);
    }

    public class TLSSocketFactory extends
            SSLSocketFactory {
        private SSLSocketFactory delegate;
        public TLSSocketFactory () throws
                KeyManagementException,
                NoSuchAlgorithmException {
            SSLContext context =
                    SSLContext.getInstance( "TLS" );
            context.init( null , null , null );
            delegate = context.getSocketFactory();
        }
        @Override
        public String [] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites
                    ();
        }
        @Override
        public String [] getSupportedCipherSuites()
        {
            return
                    delegate.getSupportedCipherSuites();
        }
        @Override
        public Socket createSocket() throws
                IOException {
            return enableTLSOnSocket
                    (delegate.createSocket());
        }
        @Override
        public Socket createSocket( Socket s,
                                    String host, int port, boolean autoClose)
                throws IOException {
            return enableTLSOnSocket
                    (delegate.createSocket(s, host, port, autoClose));
        }
        @Override
        public Socket createSocket( String host,
                                    int port) throws IOException ,
                UnknownHostException {
            return enableTLSOnSocket
                    (delegate.createSocket(host, port));
        }
        @Override
        public Socket createSocket( String host,
                                    int port, InetAddress localHost, int localPort) throws IOException ,
                UnknownHostException {
            return enableTLSOnSocket
                    (delegate.createSocket(host, port, localHost, localPort));
        }
        @Override
        public Socket createSocket( InetAddress host, int port) throws IOException {
            return enableTLSOnSocket
                    (delegate.createSocket(host, port));
        }
        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws
                IOException {
            return enableTLSOnSocket
                    (delegate.createSocket(address, port, localAddress, localPort));
        }
        private Socket enableTLSOnSocket
                (Socket socket) {
            if(socket != null && (socket instanceof
                    SSLSocket)) {
                ((SSLSocket)socket).setEnabledProtocols( new String []
                        {"TLSv1.1" , "TLSv1.2" });
            }
            return socket;
        }
    }

}
