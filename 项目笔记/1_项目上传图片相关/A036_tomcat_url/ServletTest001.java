import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;


public class ServletTest001 implements Servlet{

	
	/*дһ����ʵ��Servlet�ӿڣ�����ʵ��Servlet�ӿ�������г��󷽷�,
	�˳���������ɵ�class�ļ�����ŵ�WEB-INFĿ¼�µ�classes�ļ����
	ͬʱҪ��дͬĿ¼�µ�web.xml�ļ�����Tomcat������֪����ε�������Աд�ĳ���

	��������ļ��У���A002-ServletTest001Ŀ¼Ҫ�ŵ�TomcatĿ¼webapps�£����в���
	ע�⣬Ҫ��д��web.xml�ļ��������ã�ʹ��Tomcat֪���û�����·����ServletTest001.class�ļ��Ķ�Ӧ��ϵ
	*/
	public void service(ServletRequest req, ServletResponse res) throws ServletException,IOException {
	   //��service(..)������дһ�δ��룬����������Tomcat�Ŀ���̨
	   System.out.println("First servlet code.");
	}

	public void init(ServletConfig config) throws ServletException {
	
	}
	
	public ServletConfig getServletConfig(){
		return null;
	}

	public String getServletInfo(){
		return null;
	}

	public void destroy(){
		
	}

}