package downloadLectio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetSchedule {
	public static Map<String, String> Map(String[] args) throws MalformedURLException, IOException {
			String[] ID = (GetID.ID()).split("-");
			String url = "http://www.lectio.dk/lectio/"+ID[0]+"/SkemaNy.aspx?type=elev&elevid="+ID[1];
	        Document doc = Jsoup.connect(url).get();	        
	        Map<String, String> values = new HashMap<String, String>();
	        
	        Elements links = doc.select("a");
	        for (Element link : links) {
	        	values.put(link.attr("title"), link.text());
	        }
			return values;
	}

public static void main(String[] args) throws MalformedURLException, IOException {
	System.out.println(GetSchedule.Map(args));
	}
}