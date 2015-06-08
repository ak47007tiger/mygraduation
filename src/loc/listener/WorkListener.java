package loc.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import loc.service.SearchService;

/**
 * Application Lifecycle Listener implementation class WorkListener
 *
 */
public class WorkListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public WorkListener() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	try {
			SearchService.getDefault().getLuceneSearcher().endWork();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
