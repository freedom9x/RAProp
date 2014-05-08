package dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Crawler {

	public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
			Crawler crawler = new Crawler();
			BufferedReader reader = new BufferedReader(new FileReader("tweet2011/tweet2Topic.txt"));
			String term;
			int dem = 0;
			
			while(( term = reader.readLine())!=null)
			{		
				PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/tweet_test_AG.txt", true));
				String[] parts = term.split(" ");
				
				String content = crawler.getContentFromId(Long.parseLong(parts[1]), parts[3]);
				if(content!=null)
				{
					if(IsRetweet(content)) continue;
					pw.println(parts[0]+" "+ parts[1]+ 
							" "+parts[2]+" "+parts[3]+" "+"\""+content+"\"");
					dem++;
					System.out.println(dem+"---\t"+parts[0]+" "+ parts[1]+ 
							" "+parts[2]+" "+parts[3]+" "+"\""+content+"\"");
					//if(dem==20) break;
					
				}
				else {
					System.out.println("tweet broken:" + parts[1]);
				}
				pw.close();
				
			}
			
			
//			if(IsRetweet("BBC News - BBC journalist arrested and http://www.bbbc beaten by Egyptian police http://www.bbc.co.uk/news/world-africa-1230824 #jan25 #egypt #wfegfer http://www.bbc.co.  "))
//				System.out.println("OK");
//			else {
//				System.out.println("NO");
//			}
		//	String content = crawler.getContentFromId(Long.parseLong("32797963470245888"), "onemanrace");
			System.out.println("-----------------DONE-----------------");
			
		}

	/**
	 * @param args
	 */
	public String getContentFromId(long tweetID, String user_name)
	{
		String[] result= new String[5];
		String url = "https://twitter.com/"+user_name+"/status/"+tweetID;
		String data = null;
		int favorites = 0;
		try {
			data = this.getURL(url,true);
			String tag = "<div class=\"stream-item-footer clearfix\">";
			while(data.contains(tag))//tim chuoi chua content
			{
				int begin = data.indexOf(tag)+tag.length()+1;
				int end = data.indexOf("<div class=\"stream-item-footer\">");
				data = data.substring(begin,end);
				
			}
			//////if(data.con)
			//tim favorite
//			if(data != null && data.contains("<li class=\"js-stat-count js-stat-favorites stat-count\">")){
//				String term = data.substring(data.indexOf("<li class=\"js-stat-count js-stat-favorites stat-count\">"));
//				int start = term.indexOf("<strong>");
//				int end = term.indexOf("</strong>");
//				term = term.substring(start+8, end);
//				term = term.replace(",","");
//				favorites = Integer.parseInt(term);
//			}
			//tim noi dung tweet
			//if(data.contains(s))
			if(data != null && data.contains("<p class=\"js-tweet-text tweet-text\">")){
				String term = data.substring(data.indexOf("<p class=\"js-tweet-text tweet-text\">"));
				//int start = term.indexOf("\">");
				int end = term.indexOf("</p>");
				term = term.substring(0, end);
				term = term.replace("&#39;","'");
				result[0] = term.replaceAll("\\<.*?>","");
				
			}
			
		} catch (UserNotFoundException e) {
			System.out.println("User Not Found "+user_name);
			// TODO: handle exception
		}
		if(result[0]!=null)
		{
			//result[0]=Remove_hastab(result[0]);
			
			
			return result[0];
		}
		else
		{
			System.out.println("tweet is broken "+tweetID+"\t"+user_name);
			return null;
		}
		//return result[0];
	}
	
	public String getURL(String url,boolean addnewLine) throws UserNotFoundException{
		URL urlObj;
		try {
			urlObj = new URL(url);
			URLConnection connection = urlObj.openConnection();
			connection.connect();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int code = httpConnection.getResponseCode();
			if(code == 200){
				InputStreamReader in = new InputStreamReader((InputStream) httpConnection.getContent());
			    BufferedReader buff = new BufferedReader(in);
			    StringBuffer returnData = new StringBuffer();
			    String line;
			    while((line = buff.readLine()) != null ) {
			    	returnData.append(line);
			    	if(addnewLine) returnData.append("\n");
			      } 
			    buff.close();
			    in.close();
			    httpConnection.disconnect();
				return returnData.toString();
			}
			else if(code == 400){
				System.out.println("Rate Limit "+url);
				Thread.sleep(1800000);
				return this.getURL(url,addnewLine);
			}
			else {
				System.out.println("Error "+code+" "+url);
				throw new UserNotFoundException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String Remove_hastab(String text)
	{
		//String result = "";
		while(text.contains("data-query-source=\"hashtag_click\""))
		{
			String hash_tag ;
			String link_of_hash_tag ;
			int begin = text.indexOf("<a href=\"/search?q=");
			text = text.substring(begin);
			int end = text.indexOf("</b></a>");
			
			//String test = text.replaceAll("\\<.*?>","");
			link_of_hash_tag = text.substring(0,end+8);
			hash_tag = link_of_hash_tag.substring(
					link_of_hash_tag.indexOf("<s>#</s><b>")+11,
					link_of_hash_tag.indexOf("</b></a>"));
			text = text.replace(link_of_hash_tag, "#"+hash_tag);
		}
		return text;
	}
	public static boolean IsRetweet(String tweet) throws IOException
	{
		tweet = RemoveTerm(tweet, "http://");
		tweet = RemoveTerm(tweet, "#");
		tweet = RemoveTerm(tweet, "@");
		tweet = tweet.replaceAll("\\s+", "");
		BufferedReader reader = new BufferedReader(new FileReader("tweet2011/tweet_test_AG.txt"));
		String term;
		while(( term = reader.readLine())!=null)
		{
			String parts[] = term.split("\"");
			parts[1] = RemoveTerm(parts[1], "http://");
			parts[1] = RemoveTerm(parts[1], "#");
			parts[1] = RemoveTerm(parts[1], "@");
			parts[1] = parts[1].replaceAll("\\s+", "");
			if(tweet.equals(parts[1]))
			{
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}
	public static String RemoveTerm(String text, String rm_key)
	{
		while(text.contains(rm_key))
		{
			String rm_term;
			String text_of_key;
			int begin = text.indexOf(rm_key);
			String sub_begin = text.substring(begin);
			if(sub_begin.contains(" "))
			{
				int end = sub_begin.indexOf(" ");
				rm_term = sub_begin.substring(0,end);
			}
			else
			{
				rm_term = sub_begin;
			}
			text = text.replace(rm_term, "");
		}
		return text;
	}
	
}
