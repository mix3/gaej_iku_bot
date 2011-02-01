package org.mix3.bot.page;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.MultiLineLabel;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@AuthorizeInstantiation({Roles.ADMIN})
public class PostCheckPage extends AbstractWebPage{
	public PostCheckPage() {
		@SuppressWarnings("static-access")
		URLFetchService ufs = new URLFetchServiceFactory().getURLFetchService();
		try {
			HTTPResponse res = ufs.fetch(new URL("http://tenki.jp/warn/"));
			String html = new String(res.getContent(), "UTF8");
			Source source = new Source(html);
			String date = source.getAllElementsByClass("dateRight").get(0).getContent().toString();
			Element wrap = source.getElementById("wrap_announcingTable");
			int flag = 0;
			List<String> result = new ArrayList<String>();
			for(Element element : wrap.getAllElements()){
				if(flag == 1){
					for(Element a : element.getAllElements("a")){
						result.add(a.getContent().toString());
					}
					flag = 0;
				}
				if(element.getAttributeValue("abbr") != null && element.getAttributeValue("abbr").equals("雷")){
					flag = 1;
				}
			}
			add(new MultiLineLabel("result", date + " " + result.toString()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
