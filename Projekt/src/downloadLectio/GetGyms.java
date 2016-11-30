package downloadLectio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetGyms {
	public static Map<String, String> Map(String[] args) throws MalformedURLException, IOException {
			String url = "http://www.enelleranden.dk/lectio/logingym.html";
	        Document doc = Jsoup.connect(url).get();	        
	        Map<String, String> values = new HashMap<String, String>();
	        
	        Elements links = doc.select("a");
	        for (Element link : links) {
	        	values.put(link.attr("href").replace("/lectio/", "").replace("/default.aspx", ""), link.text());
	        }
			return values;
	}

	public static String GymID() {
		
		return null;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println(GetGyms.Map(args));
		}
}