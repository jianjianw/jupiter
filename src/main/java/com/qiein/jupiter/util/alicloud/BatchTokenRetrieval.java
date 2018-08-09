package com.qiein.jupiter.util.alicloud;

import org.apache.commons.httpclient.NameValuePair;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author edward
 * @date 2018/5/18
 */
public class BatchTokenRetrieval {

    private static final String ALIYUN_AUTHORITY_ENDPOINT = "https://signin.aliyun.com/oauth2/v1/auth";
    private static final String ALIYUN_TOKEN_ENDPOINT = "https://oauth.aliyun.com/v1/token";
    private static final String ALIYUN_SIGN_IN_ENDPOINT = "https://signin.aliyun.com/login.htm";

    private static final RandomString randomString = new RandomString(45);

    public static String retrieve(String username, String password, String clientId, String clientSecret,
                                  String registeredCallbackUrl) {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(manager);

        httpsRequest(
            String.format("%s?user_principal_name=%s&password_ims=%s", ALIYUN_SIGN_IN_ENDPOINT, username, password),
            "GET", manager, true);

        String nonce = randomString.nextString();
        String authorityUrl = getAliyunAuthorityUrl(nonce, clientId, registeredCallbackUrl);

        String response = httpsRequest(authorityUrl, "GET", manager, false);
        /**
         * Expected result:
         * https://127.0.0.1:8443/aliyun/auth/callback?state=a353f80b-c529-4867-8254-e7b9b970fcc9&code
         * =58UDi2x4qW4oZMv3QYJnJRfJMKpRrC2T
         */
        String[] items = response.split("code=");
        if (items.length == 2) {
            String token = null;
            try {
                token = getAccessToken(items[1], nonce, clientId, clientSecret, registeredCallbackUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * Expected result:
             * {"request_id":"89ea3f86-a1ad-4e87-8ccf-9f2c8723800f",
             * "access_token":"eyJBY2Nlc3NLZXlJZCI6IlNUUy5OSFFCdW1XZ1VOTkVyNjhpNHVUQm9haktaIiwiQWNjZXNzS2V5U2VjcmV0IjoiRVNoRzl5dzh5bnFnamNod2FZQkpRdXNyaDdOYjh6elZFVVZzR3dXQlF2d1oiLCJTZWN1cml0eVRva2VuIjoiQ0FJU2h3SjFxNkZ0NUIyeWZTaklyNHZrQ2MvWnVyaDArWXl1Y0JDSmpUUWdXTTFEanEvZ3VEejJJWEZOZm5Gc0Jlb2Z2dlEzbm1GUzZ2b1lscUFpRWNjY0doV2NOcElvVENiOEFkemxNZVQ3b01XUXdlRXVoZi9NUUJxYmFYUFMyTXZWZkorS0xyZjBjZXVzYkZicGp6SjZ4YUNBR3h5cFExMmlOKy9pNi9jbEZJWjFPRE8xZGoxYkh0eGJDeEovb2NzQlR4dnJPTzJxTHdUaGp4aW1iajVoc1JFdWdtaDB1K2J0bTVIRnRrR1AwZ2Vpa2I1Sit0eXFmOHJtVGJFMVlNNGpDSWJyaHJRdEovYWNpSFVOMFhVUXF2Y3ExcDQ4MkRMZnM4dUdCRjlZL3hpS0R2SFo2TlVITG5Vak4vRmdRL0lZOXFHc3hhVWk2N2FQeTk3dG9CMUVOL0dBZ0tNRTJTVW9xaHFBQVRJREpLREZqSVJaeHV5MTJqZ0EyK3laTUxFaitvS3ZTK1h2UUhIRVIrTHBRS2tXN2g1eGc1U0hOSzRDaWdONlR5QkNBWHNibFJSNjJQaG9ocitoWFh6R0ZPanR5N0phSmlEWWZHZ1AydmQvUHdpdTdpbHFUZTlTd1YyTSt3bnpNK0pVejlpWWxPaHJlRXJiMFdTdTVPd1BKN0xJN1Nnalg2K0Q0WkxsQkVzbyJ9","token_type":"AliyunSTS","refresh_token":"FHWW23m1mFpIqgmg","id_token":"eyJraWQiOiJKQzl3eHpyaHFKMGd0YUNFdDJRTFVmZXZFVUl3bHRGaHVpNE8xYmg2N3RVIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiI5MjAxNzEwMzEiLCJzdWIiOiIyOTEyMDQ0MjE4ODAyOTI0NDIiLCJ1cG4iOiJjYWJAZWR3YXJkbHUub25hbGl5dW4uY29tIiwiaXNzIjoiaHR0cHM6XC9cL29hdXRoLmFsaXl1bi5jb20iLCJuYW1lIjoiY2FiQGVkd2FyZGx1Lm9uYWxpeXVuLmNvbSIsImV4cCI6MTUyNjY0Mzg3OSwiYmlkIjoiMjY4NDIiLCJpYXQiOjE1MjY2NDAyNzksImFpZCI6IjE0MTQzOTYyMDM5NDUwMzUifQ.XdkIN4PVZoB_bQZ3n6Sq6NUOq-HCnBVwc2Vc-JgjAQ1dDwe5Rg8QapRnS4E2FWkO3c3WcAe_2bCnBf4TtaSWu1x52HhdpMV5SYTQdDDhEXjnjQqF9vfIKAieNdvBpTaGB1fzLtedPSmTkY1Anw-Gf_Gr3r7Mm-7va0jQXi8T71sM0Og7CIXgfOJ4s6QiMLMfIr02wpKsEQJb3pzl3KyMP_WMZ9n6SV5O3ko3jaQYVclExxo9FbbUn6fdtxprtS5iw6r01wH8wN8FrANF08l49OwCdAveeexeuY3fvIPkb7-5ZY4skJXDxxMjKKtiq4dCTOUBaPeC3K3PExFYqijeMg","additional_information":{"refresh_token_id":31074},"expires_in":"3599"}
             */
            return token;
        } else {
            return null;
        }
    }

    private static String httpsRequest(String requestUrl, String requestMethod, CookieManager cookies,
                                       boolean followRedirects) {
        String newUrl = null;

        try {

            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            sslContext.init(null, tm, new java.security.SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setSSLSocketFactory(ssf);
            conn.setInstanceFollowRedirects(followRedirects);
            applyCookies(conn, cookies);
            conn.connect();

            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    newUrl = conn.getHeaderField("Location");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newUrl;
    }

    private static void applyCookies(HttpsURLConnection urlConnection, CookieManager cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        if (cookies.getCookieStore() != null && cookies.getCookieStore().getCookies() != null) {
            for (HttpCookie cookie : cookies.getCookieStore().getCookies()) {
                cookieHeader.append(cookie).append(";");
            }
        }
        if (cookieHeader.length() > 0) {
            cookieHeader.deleteCharAt(cookieHeader.length() - 1);
            urlConnection.setRequestProperty("Cookie", cookieHeader.toString());
        }
    }

    private static String getAliyunAuthorityUrl(String nonce, String clientId, String registeredCallbackUrl) {
        String state = UUID.randomUUID().toString();
        try {
            return ALIYUN_AUTHORITY_ENDPOINT
                + "?response_type=code&response_mode=form_post"
                + "&redirect_uri=" + URLEncoder.encode(registeredCallbackUrl, "UTF-8")
                + "&client_id=" + clientId
                + "&state=" + state
                + "&codeChallenge=" + nonce
                + "&access_type=offline";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAccessToken(String authCode, String nonce, String clientId, String clientSecret,
                                         String registeredCallbackUrl)
        throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("client_id", clientId));
        params.add(new NameValuePair("grant_type", "authorization_code"));
        params.add(new NameValuePair("client_secret", clientSecret));
        params.add(new NameValuePair("code", authCode));
        params.add(new NameValuePair("redirect_uri", registeredCallbackUrl));
        params.add(new NameValuePair("code_verifier", nonce));
        NameValuePair[] pairs = new NameValuePair[params.size()];
        return HttpRequester.post(ALIYUN_TOKEN_ENDPOINT, params.toArray(pairs));
    }
}
