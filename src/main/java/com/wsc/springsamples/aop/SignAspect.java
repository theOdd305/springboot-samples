package com.wsc.springsamples.aop;

import com.wsc.springsamples.exceptions.BusinessException;
import com.wsc.springsamples.utils.SignUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class SignAspect {

    private static final String SIGN_HEAD = "X-SIGN";

    @Pointcut("execution(@com.wsc.springsamples.config.annotation.Signature * * (..))")
    private void verifySignPointCut() {

    }

    @Before("verifySignPointCut()")
    public void verify() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();

        String sign = request.getHeader(SIGN_HEAD);

        if (StringUtils.hasText(sign)) {
            throw new BusinessException("no signature in header: " + SIGN_HEAD);
        }

        try {
            String generatedSignature = generatedSignature(request);
            if (!sign.equals(generatedSignature)) {
                throw new BusinessException("invalid signature");
            }
        } catch (Throwable throwable) {
            throw new BusinessException("invalid signature");
        }
    }

    /**
     * 服务器端生成参数签名
     * @param request
     * @return
     * @throws IOException
     */
    private String generatedSignature(HttpServletRequest request) throws IOException {
        // @RequestBody
        String bodyParam = null;
        if (request instanceof ContentCachingRequestWrapper) {
            bodyParam = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(), StandardCharsets.UTF_8);
        }

        // @RequestParam
        Map<String, String[]> requestParameterMap = request.getParameterMap();

        // @PathVariable
        String[] paths = null;
        ServletWebRequest webRequest = new ServletWebRequest(request, null);
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (!CollectionUtils.isEmpty(uriTemplateVars)) {
            paths = uriTemplateVars.values().toArray(new String[0]);
        }

        return SignUtil.sign(bodyParam, requestParameterMap, paths);
    }

}
