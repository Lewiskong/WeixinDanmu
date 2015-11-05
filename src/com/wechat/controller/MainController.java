package com.wechat.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wechat.domain.Danmu;
import com.wechat.result.GenericResult;
import com.wechat.service.CoreService;
import com.wechat.utils.SignUtil;




@Controller
public class MainController {
	@RequestMapping(value="/message",method=RequestMethod.GET)
	@ResponseBody
	public void handleGetMessage(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// ΢�ż���ǩ��  
        String signature = request.getParameter("signature");  
        // ʱ���  
        String timestamp = request.getParameter("timestamp");  
        // �����  
        String nonce = request.getParameter("nonce");  
        // ����ַ�  
        String echostr = request.getParameter("echostr");  
  
        PrintWriter out = response.getWriter();  
        // ͨ�����signature���������У�飬��У��ɹ���ԭ���echostr����ʾ����ɹ����������ʧ��  
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {  
            out.print(echostr);  
        }  
        out.close();  
        out = null;  
	}
	
	@RequestMapping(value="/message",method=RequestMethod.POST)
	@ResponseBody
	public void handlePostMessage(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩  
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
  
        // ���ú���ҵ���������Ϣ��������Ϣ  
        String respMessage = CoreService.processRequest(request);  
        
        // ��Ӧ��Ϣ  
        PrintWriter out = response.getWriter();  
        out.print(respMessage);  
        out.close();  
	}
	
	@RequestMapping({"/test"})
	public void test(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.getWriter().println("The server is running now");
	}
	
	@RequestMapping({"/getDanmu"})
	public void getDanmu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Danmu result=new Danmu();
		result.setColor("red");
		result.setPosition("0");
		result.setSize("1");
		result.setText(CoreService.getDanmu());
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(new Gson().toJson(result));
	}
	
	@RequestMapping({"/getDanmuCount"})
	public void getDanmuCount(HttpServletRequest request,HttpServletResponse response) throws Exception{
		int number=CoreService.getDanmuNumber();
		response.getWriter().print("{\"number\":\""+number+"\"}");
	}
	
	@RequestMapping({"/startDanmu"})
	public void startDanmu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		GenericResult result=CoreService.startDanmu();
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(new Gson().toJson(result));
	}
	
	@RequestMapping({"/pauseDanmu"})
	public void pauseDanmu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		GenericResult result=CoreService.pauseDanmu();
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(new Gson().toJson(result));
	}
}
