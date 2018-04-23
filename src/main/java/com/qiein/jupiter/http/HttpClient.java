package com.qiein.jupiter.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

public interface HttpClient {

	public void setUrl(String urlStr) throws MalformedURLException;

	public void setUrl(URL url);

	public void setHttpVersion(String httpVersion);

	public void setHttpContentCharset(String encoding);

	public void setRequestTimeoutInMillis(int requestTimeoutInMillis);

	public void setProxy(String proxyhost, int proxyport);

	public void setProxy(String proxyhost, int proxyport, String proxyAuthUsername, String proxyAuthPassword);

	public void setHttpMethod(String httpMethod) throws HttpException;

	public void addHeader(String name, String value);

	public void addHeader(Map<String, String> header_data);

	public void addHeaderObj(Map<String, Object> header_data);

	public void addParameter(String name, String value);

	public void setParameter() throws Exception;

	public void setParameter(String encoding) throws Exception;

	public void setEntity(String content, String charset) throws IOException;

	public void setEntity(String content, String contentType, String charset) throws IOException;

	public void setEntity(String fileName) throws IOException;

	public void setEntity(File file) throws IOException;

	public void setEntity(String fileName, long length) throws IOException;

	public void setEntity(File file, long length) throws IOException;

	public void execute() throws ClientProtocolException, IOException;

	public String getStatusLine();

	public int getStatusCode();

	public String getHeaderValue(String name);

	public String getContentType();

	public Header[] getAllHeaders();

	public Charset getCharset(String contentType);

	public Charset getCharset();

	public InputStream getContentInputStream() throws IOException;

	public boolean getContentFile(String fileName) throws IOException;

	public boolean getContentFile(String filePath, String fileName) throws IOException;

	public void abortExecution();

	public void shutdown();

	public String getContentStr(Charset charset) throws IOException;

	public String getContentStr() throws IOException;
}