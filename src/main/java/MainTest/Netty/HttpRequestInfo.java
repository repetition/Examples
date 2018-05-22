package MainTest.Netty;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

/**
 * 请求封装类
 */
public class HttpRequestInfo implements HttpRequest {
    private HttpMethod httpMethod;
    private String url;
    private HttpVersion httpVersion;
    private HttpHeaders httpHeaders;

    public HttpRequestInfo(HttpMethod httpMethod, String url, HttpVersion httpVersion, HttpHeaders httpHeaders) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.httpVersion = httpVersion;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public HttpMethod getMethod() {
        return method();
    }

    @Override
    public HttpMethod method() {
        return httpMethod;
    }

    @Override
    public HttpRequest setMethod(HttpMethod httpMethod) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    @Override
    public String getUri() {
        return url;
    }

    @Override
    public String uri() {
        return url;
    }

    @Override
    public HttpRequest setUri(String s) {
        this.url = s;
        return this;
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return httpVersion;
    }

    @Override
    public HttpVersion protocolVersion() {
        return httpVersion;
    }

    @Override
    public HttpRequest setProtocolVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    @Override
    public HttpHeaders headers() {
        return httpHeaders;
    }

    @Override
    public DecoderResult getDecoderResult() {
        return null;
    }

    @Override
    public DecoderResult decoderResult() {
        return null;
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {

    }

    @Override
    public String toString() {
        return "HttpRequestInfo{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", httpVersion=" + httpVersion +
                ", httpHeaders=" + httpHeaders +
                '}';
    }
}
