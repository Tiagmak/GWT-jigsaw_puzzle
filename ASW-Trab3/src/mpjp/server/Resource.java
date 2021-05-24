package mpjp.server;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import mpjp.game.Images;
import mpjp.game.WorkspacePool;

public class Resource extends javax.servlet.http.HttpServlet {
	private static final long serialVersionUID = 1L;

	Resource(){
		
	}
	
	protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) {
		
	}
	
	String getContentType(String name) {
		return name;
	}
	
	String getPackageName() {
		return null; 
	}
	
	static void setPackageName(String packageName) {
		
	}
	public void init() throws ServletException{
		super.init();
        ServletContext context = getServletContext();
        File base = new File(context.getRealPath("/"));

        File imagesdir = new File(base,"WEB-INF/classes/mpjp/resources");
        
        File poolDir = new File(base,"WEB-INF/pool");

        if(!poolDir.exists())
            poolDir.mkdir();

        Images.setImageDirectory(imagesdir);
        WorkspacePool.setPoolDiretory(poolDir);
	}	
}
