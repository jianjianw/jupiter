package com.qiein.jupiter.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.wiztools.commons.StreamUtil;

@SuppressWarnings("deprecation")
public class HttpClientImpl implements HttpClient {

	private URL url = null;
	@SuppressWarnings("unused")
	private String urlHost = "";
	@SuppressWarnings("unused")
	private int urlPort = 80;
	@SuppressWarnings("unused")
	private String urlProtocol = "";
	private String urlStr = "";

	private DefaultHttpClient httpclient = null;
	private HttpParams httpParams = null;
	private List<NameValuePair> parameters = null;
	private HttpContext httpContext = null;

	private AbstractHttpMessage method = null;

	private HttpResponse http_res = null;

	private boolean interruptedShutdown = false;
	private boolean isRequestCompleted = false;

	public HttpClientImpl() {

		httpclient = new DefaultHttpClient();
		httpParams = httpclient.getParams();
		parameters = new ArrayList<NameValuePair>();
	}

	@Override
	public void setUrl(String urlStr) throws MalformedURLException {

		setUrl(new URL(urlStr));
	}

	@Override
	public void setUrl(URL url) {

		this.url = url;
		urlHost = url.getHost();
		urlPort = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
		urlProtocol = url.getProtocol();
		urlStr = url.toString();
	}

	@Override
	public void setHttpVersion(String httpVersion) {

		ProtocolVersion protocolVersion = httpVersion.equals("1.1") ? new ProtocolVersion("HTTP", 1, 1)
				: new ProtocolVersion("HTTP", 1, 0);
		httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, protocolVersion);
	}

	@Override
	public void setHttpContentCharset(String encoding) {
		httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, encoding);
	}

	@Override
	public void setRequestTimeoutInMillis(int requestTimeoutInMillis) {
		HttpConnectionParams.setConnectionTimeout(httpParams, requestTimeoutInMillis);
	}

	@Override
	public void setProxy(String proxyhost, int proxyport) {

		setProxy(true, proxyhost, proxyport, false, null, null);
	}

	@Override
	public void setProxy(String proxyhost, int proxyport, String proxyAuthUsername, String proxyAuthPassword) {

		setProxy(true, proxyhost, proxyport, true, proxyAuthUsername, proxyAuthPassword);
	}

	@Override
	public void setHttpMethod(String httpMethod) throws HttpException {

		if (httpMethod.equalsIgnoreCase("GET")) {
			method = new HttpGet(urlStr);
		} else if (httpMethod.equalsIgnoreCase("POST")) {
			method = new HttpPost(urlStr);
		} else if (httpMethod.equalsIgnoreCase("PUT")) {
			method = new HttpPut(urlStr);
		} else if (httpMethod.equalsIgnoreCase("DELETE")) {
			method = new HttpDelete(urlStr);
		} else if (httpMethod.equalsIgnoreCase("HEAD")) {
			method = new HttpHead(urlStr);
		} else if (httpMethod.equalsIgnoreCase("OPTIONS")) {
			method = new HttpOptions(urlStr);
		} else if (httpMethod.equalsIgnoreCase("TRACE")) {
			method = new HttpTrace(urlStr);
		} else {
			throw new HttpException("No httpMethod");
		}

		method.setParams(new BasicHttpParams().setParameter(urlStr, url));
	}

	@Override
	public void addHeader(String name, String value) {

		Header header = new BasicHeader(name, value);
		method.addHeader(header);
	}

	@Override
	public void addHeader(Map<String, String> header_data) {

		if (header_data != null) {
			List<String> keys = new ArrayList<String>(header_data.keySet());
			Collections.sort(keys);
			String key = "";
			String value = "";

			for (int i = 0; i < keys.size(); i++) {
				key = keys.get(i);
				value = header_data.get(key);

				Header header = new BasicHeader(key, value);
				method.addHeader(header);
			}
		}
	}

	@Override
	public void addHeaderObj(Map<String, Object> header_data) {

		if (header_data != null) {
			List<String> keys = new ArrayList<String>(header_data.keySet());
			Collections.sort(keys);
			String key = "";
			String value = "";

			for (int i = 0; i < keys.size(); i++) {
				key = keys.get(i);
				value = String.valueOf(header_data.get(key));

				Header header = new BasicHeader(key, encode(value, "UTF-8"));
				method.addHeader(header);
			}
		}
	}

	@Override
	public void addParameter(String name, String value) {

		parameters.add(new BasicNameValuePair(name, value));
	}

	@Override
	public void setParameter() throws Exception {

		setParameter(null);
	}

	@Override
	public void setParameter(String encoding) throws Exception {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			if (parameters != null && parameters.size() > 0) {

				UrlEncodedFormEntity entity = null;

				try {
					if (encoding != null && encoding.trim().length() > 0) {
						entity = new UrlEncodedFormEntity(parameters, encoding);
					} else {
						entity = new UrlEncodedFormEntity(parameters);
					}

					eeMethod.setEntity(entity);

					EntityUtils.consume(entity);
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}

			}
		}
	}

	@Override
	public void setEntity(String content, String charset) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			if (content != null && content.trim().length() > 0) {
				try {
					AbstractHttpEntity entity = new ByteArrayEntity(content.getBytes(charset));
					eeMethod.setEntity(entity);

					EntityUtils.consume(entity);
				} catch (IOException e) {
					throw new IOException(e.getMessage());
				}
			}
		}
	}

	@Override
	public void setEntity(String content, String contentType, String charset) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			if (content != null && content.trim().length() > 0) {
				try {
					AbstractHttpEntity entity = new ByteArrayEntity(content.getBytes(charset));
					entity.setContentType(Util.getFormattedContentType(contentType, charset));
					eeMethod.setEntity(entity);

					EntityUtils.consume(entity);
				} catch (IOException e) {
					throw new IOException(e.getMessage());
				}
			}
		}
	}

	@Override
	public void setEntity(String fileName) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			try {
				InputStreamEntity inputStreamEntity = new InputStreamEntity(new FileInputStream(fileName),
						new File(fileName).length());

				eeMethod.setEntity(inputStreamEntity);
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
	}

	@Override
	public void setEntity(File file) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			try {
				InputStreamEntity inputStreamEntity = new InputStreamEntity(new FileInputStream(file), file.length());

				eeMethod.setEntity(inputStreamEntity);
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
	}

	@Override
	public void setEntity(String fileName, long length) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			try {
				InputStreamEntity inputStreamEntity = new InputStreamEntity(new FileInputStream(fileName), length);

				eeMethod.setEntity(inputStreamEntity);
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
	}

	@Override
	public void setEntity(File file, long length) throws IOException {

		if (method instanceof HttpEntityEnclosingRequest) {

			HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;

			try {
				InputStreamEntity inputStreamEntity = new InputStreamEntity(new FileInputStream(file), length);

				eeMethod.setEntity(inputStreamEntity);
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
	}

	@Override
	public void execute() throws ClientProtocolException, IOException {

		http_res = httpclient.execute((HttpUriRequest) method, httpContext);
	}

	@Override
	public String getStatusLine() {

		return http_res.getStatusLine().toString();
	}

	@Override
	public int getStatusCode() {

		return http_res.getStatusLine().getStatusCode();
	}

	@Override
	public String getHeaderValue(String name) {

		return http_res.getHeaders(name)[0].getValue();
	}

	@Override
	public String getContentType() {

		if (http_res.getHeaders("content-type").length > 0)
			return http_res.getHeaders("content-type")[0].getValue();
		else
			return "";
	}

	@Override
	public Header[] getAllHeaders() {

		return http_res.getAllHeaders();
	}

	@Override
	public Charset getCharset(String contentType) {

		Charset charset = null;

		if (contentType != null && contentType.trim().length() > 0) {
			String charsetStr = Util.getCharsetFromContentType(contentType);
			try {
				charset = Charset.forName(charsetStr);
			} catch (IllegalCharsetNameException ex) {
				charset = Charset.defaultCharset();
			} catch (UnsupportedCharsetException ex) {
				charset = Charset.defaultCharset();
			} catch (IllegalArgumentException ex) {
				charset = Charset.defaultCharset();
			}
		} else {
			charset = Charset.defaultCharset();
		}

		return charset;
	}

	@Override
	public Charset getCharset() {

		return getCharset(getContentType());
	}

	@Override
	public InputStream getContentInputStream() throws IOException {

		HttpEntity entity = http_res.getEntity();

		if (entity != null) {
			return entity.getContent();
		}

		return null;
	}

	@Override
	public boolean getContentFile(String fileName) throws IOException {

		HttpEntity entity = http_res.getEntity();

		if (entity != null) {
			byte[] buffer = null;

			InputStream inputStream = null;
			OutputStream outputstream = null;
			BufferedInputStream bufferedInputStream = null;
			BufferedOutputStream bufferedOutputStream = null;

			try {
				inputStream = entity.getContent();
				outputstream = new FileOutputStream(fileName);
				bufferedInputStream = new BufferedInputStream(inputStream);
				bufferedOutputStream = new BufferedOutputStream(outputstream);

				int count = 0;
				buffer = new byte[1024 * 1000];

				while ((count = bufferedInputStream.read(buffer)) > 0) {
					bufferedOutputStream.write(buffer, 0, count);
				}

				bufferedOutputStream.flush();

				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				if (bufferedOutputStream != null) {
					try {
						bufferedOutputStream.close();
					} catch (IOException e) {
					}
					bufferedOutputStream = null;
				}
				if (outputstream != null) {
					try {
						outputstream.close();
					} catch (IOException e) {
					}
					outputstream = null;
				}
				if (bufferedInputStream != null) {
					try {
						bufferedInputStream.close();
					} catch (IOException e) {
					}
					bufferedInputStream = null;
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
					inputStream = null;
				}

				buffer = null;
			}
		}

		return false;
	}

	@Override
	public boolean getContentFile(String filePath, String fileName) throws IOException {

		File file = new File(filePath);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				return false;
			}
		}

		return getContentFile(filePath + fileName);
	}

	@Override
	public void abortExecution() {

		if (!isRequestCompleted) {
			ClientConnectionManager conMgr = httpclient.getConnectionManager();
			conMgr.shutdown();
		}

		interruptedShutdown = true;
	}

	@Override
	public void shutdown() {

		if (!interruptedShutdown) {
			ClientConnectionManager conMgr = httpclient.getConnectionManager();
			conMgr.shutdown();
		}

		isRequestCompleted = true;
	}

	private void setProxy(boolean proxyEnabled, String proxyhost, int proxyport, boolean proxyAuthEnabled,
			String proxyAuthUsername, String proxyAuthPassword) {

		if (proxyEnabled) {
			HttpHost httpproxyHost = new HttpHost(proxyhost, proxyport, "http");
			if (proxyAuthEnabled) {
				httpclient.getCredentialsProvider().setCredentials(new AuthScope(proxyhost, proxyport),
						new UsernamePasswordCredentials(proxyAuthUsername, proxyAuthPassword));
			}
			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, httpproxyHost);
		}
	}

	public static String encode(String str, String enc) {

		String strEncode = "";

		try {
			if (str != null)
				strEncode = URLEncoder.encode(str, enc);
		} catch (UnsupportedEncodingException e) {
		}

		return strEncode;
	}

	@Override
	public String getContentStr(Charset charset) throws IOException {

		HttpEntity entity = http_res.getEntity();

		if (entity != null) {
			InputStream is = entity.getContent();
			if (is != null) {
				try {
					return StreamUtil.inputStream2String(is, charset);
				} catch (IOException e) {
					throw new IOException("Response body conversion to string using " + charset.displayName()
							+ " encoding failed. Response body not set!");
				}
			}
		}

		return null;
	}

	@Override
	public String getContentStr() throws IOException {

		return getContentStr(getCharset());
	}
}
