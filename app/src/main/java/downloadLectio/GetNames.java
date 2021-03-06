package downloadLectio;

import android.os.AsyncTask; //used to reduce pressure on the UI thread

//Jsoup is imported and the entire libary coexists in the project file
import org.jsoup.Jsoup; //used to connect to Lectio
import org.jsoup.nodes.Document; //used to specify the download
import org.jsoup.nodes.Element; //used to specify the text and attr for our callback
import org.jsoup.select.Elements; //used to select the elements we needed. (parse)

import java.io.IOException;//used to catch errors like (connection refused by server)

public class GetNames extends AsyncTask<String, Void, String> { //Gets the Names of the students on the gymn selected
    public AsyncResponse delegate = null; //a delegate is passed onto the Asyncresponse which via the supermethod returns the delegate to the function processFinish in activity whose context was passed onto it
    private String compact = null; //compact is a compact list of all the students on the gym.
    public String GymID; //the GymID specifies which gym the list of names should be created from
    public String type;

    @Override //overrides normal method and enables the .execute to launch the task
    public String doInBackground(String... Strings) { //an asynctask to minimize cpu usage on the UI Thread (main)
        // students are stored according to firstnames starting letter therefor we need to loop thorugh all the storage sites
        if (type.equals("Student")) {
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) { //loops through all letters of the alphabet
                String url = "http://www.lectio.dk/lectio/" + GymID + "/FindSkema.aspx?type=elev&forbogstav=" + alphabet; //creates the URL we need to connect to in order to download the names.
                Document doc = null;
                try { //initiates a download of the Webpage
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get(); //connects in whichever useragent is preferred by the device
                } catch (IOException e) { //cathes a denial exception from lectio
                    e.printStackTrace(); //prints error code
                    return null; //returns null to be caught by the search fundtion and get relaunced.
                }

                if (doc == null) {
                    return null;
                }

                //never reaches here if the connection to lectio failed
                Elements links = doc.select("li").select("a"); //selects all a tags under the li tags

                for (Element link : links) { //loops through all the results and writes them onto a String.
                    //link.text() is the text component of the a tag <a>this</a>
                    //link.attr(href) is the link component of the a tag <a href="this"></a>
                    //we replace everything in the link except for the ID of the student and adds a seperator before next student.
                    //this returns a list with (name==ID£name==ID£name==ID£...)
                    compact = compact + "£" + link.text() + "==" + link.attr("href").replace("/lectio/" + GymID + "/SkemaNy.aspx?", "");
                }//goes back to do it for the next element
            }//goes back to do it for the next letter
            if (compact != null) { //checks if any matchers were found
                return compact.replace("null£", ""); //returns the complete studentlist
            }
            return "non";//returns non which implies that there were no students on the gym (the gym is out of use)
        } else {
            String url = "http://www.lectio.dk/lectio/" + GymID + "/FindSkema.aspx?type=laerer"; //creates the URL we need to connect to in order to download the names.
            Document doc = null;
            try { //initiates a download of the Webpage
                doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get(); //connects in whichever useragent is preferred by the device
            } catch (IOException e) { //cathes a denial exception from lectio
                e.printStackTrace(); //prints error code
                return null; //returns null to be caught by the search fundtion and get relaunced.
            }

            if (doc == null) {
                return null;
            }

            //never reaches here if the connection to lectio failed
            Elements links = doc.select("li").select("a"); //selects all a tags under the li tags

            for (Element link : links) { //loops through all the results and writes them onto a String.
                //link.text() is the text component of the a tag <a>this</a>
                //link.attr(href) is the link component of the a tag <a href="this"></a>
                //we replace everything in the link except for the ID of the student and adds a seperator before next student.
                //this returns a list with (name==ID£name==ID£name==ID£...)
                compact = compact + "£" + link.text() + "==" + link.attr("href").replace("/lectio/" + GymID + "/SkemaNy.aspx?", "");
            }//goes back to do it for the next element
        }//goes back to do it for the next letter
        if (compact != null) { //checks if any matchers were found
            return compact.replace("null£", ""); //returns the complete studentlist
        }
        return "non";//returns non which implies that there were no students on the gym (the gym is out of use)
    }

    @Override //own super method and therefore needs an overwrite
    protected void onPostExecute(String result) { //used to pass the string back to LoginActivity
        delegate.processFinish(result);//assigns the delegate in LoginActivity
    }
}