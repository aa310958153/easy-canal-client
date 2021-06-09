package com.wine.easy.canal.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wine.easy.canal.core.CanalListenerWorker;
import com.wine.easy.canal.web.util.ResponseContentUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web
 * @ClassName HttpServerHandler
 * @Author qiang.li
 * @Date 2021/6/1 4:21 下午
 * @Description TODO
 */
public final class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger log = LoggerFactory.getLogger(HttpServerHandler.class);
    private static final Pattern URL_PATTERN = Pattern.compile("(^/canalClient/(elk))", Pattern.CASE_INSENSITIVE);
    private CanalClientELKController canalClientELKController = null;

    public HttpServerHandler(CanalClientELKController canalClientELKController) {
        this.canalClientELKController = canalClientELKController;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext context, final FullHttpRequest request) {
        try {
            String requestPath = request.uri();
            String requestBody = request.content().toString(CharsetUtil.UTF_8);
            log.info("Http request path: {}", requestPath);
            log.info("Http request body: {}", requestBody);
            HttpMethod method = request.method();
            if (!URL_PATTERN.matcher(requestPath).matches()) {
                response(JSON.toJSONString(ResponseContentUtil.handleBadRequest("Not support request!")), context, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            if ("/canalClient/elk".equalsIgnoreCase(requestPath) && method.equals(HttpMethod.POST)) {
                JSONObject jsonObject = JSON.parseObject(requestBody);
                response(JSON.toJSONString(canalClientELKController.elk(jsonObject.getString("task"), jsonObject.getString("condition"), Arrays.asList(jsonObject.getString("params").split(";")))), context, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            response(JSON.toJSONString(ResponseContentUtil.handleBadRequest("Not support request!")), context, HttpResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("服务处理异常",e);
            response(JSON.toJSONString(ResponseContentUtil.handleBadRequest("server500!")), context, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void response(final String content, final ChannelHandlerContext context, final HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        HttpUtil.setContentLength(response, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        context.writeAndFlush(response);
    }

}
