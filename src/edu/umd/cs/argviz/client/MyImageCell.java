package edu.umd.cs.argviz.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/*
 * 
 * This code comes from a Google Group:
 * http://groups.google.com/group/google-web-toolkit/browse_thread/thread/1995e2bb604088c6/9c919c2ca7ce55f2?lnk=gst&q=gwt+ImageCell#9c919c2ca7ce55f2
 */

public class MyImageCell extends AbstractCell<String> {
	
	  interface Template extends SafeHtmlTemplates { 
	    @Template("<img src=\"{0}\" style=\"width:20px;height:20px;\"/>")  // 20*20 size 
	    SafeHtml img(String url); 
	  } 
	  private static Template template; 
	  /** 
	   * Construct a new MyImageCell. 
	   */ 
	  public MyImageCell() { 
	    if (template == null) { 
	      template = GWT.create(Template.class); 
	    } 
	  } 
	  @Override 
	  public void render(Context context, String value, SafeHtmlBuilder sb) { 
	    if (value != null) { 
	      // The template will sanitize the URI. 
	      sb.append(template.img(value)); 
	    } 
	  } 
	} 