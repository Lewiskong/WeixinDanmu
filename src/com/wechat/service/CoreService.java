package com.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wechat.config.Constants;
import com.wechat.message.resp.TextMessage;
import com.wechat.result.GenericResult;
import com.wechat.utils.XMLParserUtil;

import redis.clients.jedis.Jedis;

public class CoreService {
	
	private static Jedis jedis;
	private static List<Integer> list;
	private static int count=0;
	private static boolean isStarted=false;
	static {
		jedis=new Jedis("localhost");
		jedis.connect();
		jedis.ping();
		list=new ArrayList<Integer>();
	}
	
	public static String getDanmu(){
		if (list.size()==0) return "";
		String result= jedis.get(list.get(0)+"");
		jedis.del(list.get(0)+"");
		list.remove(0);
		return result;
	}
	
	public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // Ĭ�Ϸ��ص��ı���Ϣ����  
            String respContent = "�������쳣�����Ժ��ԣ�";  
  
            // xml�������  
            Map<String, String> requestMap = XMLParserUtil.parseXml(request);  
  
            // ���ͷ��ʺţ�open_id��  
            String fromUserName = requestMap.get("FromUserName");  
            // �����ʺ�  
            String toUserName = requestMap.get("ToUserName");  
            // ��Ϣ����  
            String msgType = requestMap.get("MsgType");  
            
            // �ظ��ı���Ϣ  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(Constants.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
            
            // �ı���Ϣ  
            if (msgType.equals(Constants.REQ_MESSAGE_TYPE_TEXT)) {  
            	if (!isStarted) respContent="亲，弹幕墙还未开始，请耐心等待～";
            	else{
	            	boolean isBadWord=false;
	            	String content=requestMap.get("Content");
	            	for (int index=0;index<Constants.BAD_WORDS.length;index++){
	            		if (content.contains(Constants.BAD_WORDS[index])){
	            			isBadWord=true;
	            			break;
	            		}
	            	}
	            	if (!isBadWord){
		                count++;
		                jedis.set(""+count,requestMap.get("Content"));
		                list.add(count);
		                respContent="弹幕发送成功";
	            	}else{
	            		respContent="弹幕发送失败，请不要发送带有不文明词汇的弹幕，谢谢合作～";
	            	}
            	}
            }  
            // ͼƬ��Ϣ  
            else if (msgType.equals(Constants.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "亲，弹幕只支持文字和emoji哦～";  
            }  
            // ����λ����Ϣ  
            else if (msgType.equals(Constants.REQ_MESSAGE_TYPE_LOCATION)) {  
            	 respContent = "亲，弹幕只支持文字和emoji哦～";    
            }  
            // ������Ϣ  
            else if (msgType.equals(Constants.REQ_MESSAGE_TYPE_LINK)) {  
            	 respContent = "亲，弹幕只支持文字和emoji哦～";  
            }  
            // ��Ƶ��Ϣ  
            else if (msgType.equals(Constants.REQ_MESSAGE_TYPE_VOICE)) {  
            	 respContent = "亲，弹幕只支持文字和emoji哦～";  
            }  
            // �¼�����  
            else if (msgType.equals(Constants.REQ_MESSAGE_TYPE_EVENT)) {  
                // �¼�����  
                String eventType = requestMap.get("Event");  
                // ����  
                if (eventType.equals(Constants.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "лл��Ĺ�ע��";  
                }  
                // ȡ����  
                else if (eventType.equals(Constants.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO ȡ���ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ  
                }  
                // �Զ���˵�����¼�  
                else if (eventType.equals(Constants.EVENT_TYPE_CLICK)) {  
                    // TODO �Զ���˵�Ȩû�п��ţ��ݲ����������Ϣ  
                }  
            }  
  
            textMessage.setContent(respContent);  
            respMessage = XMLParserUtil.textMessageToXml(textMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  
	
	public static int getDanmuNumber(){
		return count;
	}
	
	public static GenericResult startDanmu(){
		isStarted=true;
		return new GenericResult("1", "");
	}
	
	public static GenericResult pauseDanmu(){
		isStarted=false;
		return new GenericResult("1","");
	}
}
