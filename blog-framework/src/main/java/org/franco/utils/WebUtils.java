package org.franco.utils;

import com.alibaba.fastjson.JSON;
import org.franco.domain.ResponseResult;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void renderString(HttpServletResponse response, ResponseResult responseResult) {
        try
        {
            response.setStatus(responseResult.getCode());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(responseResult));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void setDownLoadHeader(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fname= URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition","attachment; filename="+fname);
    }
}