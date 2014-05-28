package dataset;

import java.io.BufferedReader;
import java.io.File;
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

	public static void main(String[] args) throws NumberFormatException, Throwable {
			// TODO Auto-generated method stub
			Crawler crawler = new Crawler();
			//crawler.CrawlContent("tweet2011/49TrecData/MB33_AG.txt", "tweet2011/CrawlTweet/MB33_AG.txt");
			//crawler.CrawlUser("tweet2011/CrawlTweet/MB33_AG.txt","tweet2011/CrawlTweet/UserInfo.txt");
			////Crawler crawler = new Crawler();
			
//			File folder1 = new File("tweet2011/CrawlTweet");
//			File[] folder2 = folder1.listFiles();
//			for (File file : folder2) 
//			{
//				System.out.println("-------------------"+file.getName()+"-------------------------------");
//				//crawler.CrawlContent(file.getPath(), "tweet2011/CrawlTweet/"+file.getName());
//				crawler.CrawlUser("tweet2011/CrawlTweet/"+file.getName(), "tweet2011/CrawlTweet/UserInfo.txt");
//				
//			}
//			
//			String term[] = crawler.getContentFromId(Long.parseLong("29040431006220288"), "averyyyyyyy");
//			String term2[] = crawler.getContentFromId(Long.parseLong("29040434730762241"), "ChloeAButterfly");
//			String term3[] = crawler.getContentFromId(Long.parseLong("29100380419792897"), "IranPressNews");
//			String term4[] = crawler.getContentFromId(Long.parseLong("29228028110307328"), "OTBL");
//			crawler.getUserInfo("ayyo_mookiie");
//			crawler.getUserInfo("ChloeAButterfly");
//			crawler.getUserInfo("IranPressNews");
//			crawler.getUserInfo("OTBL");
//			crawler.getUserInfo("freelivem");
//			crawler.getUserInfo("TheWorldNews");
//			//String term5[] = crawler.getContentFromId(Long.parseLong(""), "");
//			
//			for (String string : term) {
//				System.out.print(string+"\t");
//			}
//			System.out.println();
//			for (String string : term2) {
//				System.out.print(string+"\t");
//			}
//			System.out.println();
//			for (String string : term3) {
//				System.out.print(string+"\t");
//			}
//			System.out.println();
//			for (String string : term4) {
//				System.out.print(string+"\t");
//			}
//			System.out.println();
////			for (String string : term5) {
////				System.out.print(string+"\t");
////			}
//			System.out.println();
			BufferedReader reader = new BufferedReader(new FileReader("tweet2011/CrawlTweet/UserInfo.txt"));
			PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/UserFeatures/UserInfo.txt", true));
			String term;
			while((term=reader.readLine())!=null)
			{
				if(!crawler.IsOvertide(term.split("\t")[0], "tweet2011/UserFeatures/UserInfo.txt"))
				{
					pw.println(term);
					System.out.println(term);
				}
				else
					System.out.println(term.split("\t")[0]+" is overide");	
			}
			reader.close();
			pw.close();
			System.out.println("-----------------DONE-----------------");
		}

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public String[] getUserInfo(String user_name)
	{
		String[] result = new String[5];
		String url = "https://twitter.com/"+user_name;
		String data = null;
		try {
			data = this.getURL(url, true);
			if(data.contains("<h1>Account suspended</h1>"))
			{
				System.out.println(user_name + "is suppended");
				return null;
			}
			//verified account
			if(data != null && data.contains("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""))
			{
				String term = data.substring(data.indexOf("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""));
				int start = term.indexOf("verified&quot;:");
				int end = term.indexOf(",&quot;statuses_count&quot;");
				term = term.substring(start+15, end);
				result[0]=term;
			}
			//create date of user
			if(data != null && data.contains("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""))
			{
				String term = data.substring(data.indexOf("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""));
				int start = term.indexOf("created_at&quot;:&quot;");
				int end = term.indexOf("&quot;,&quot;favourites_count&quot;");
				term = term.substring(start+23, end);
				result[4] =term;
			}
			
			//đếm số tweet
			if(data != null && data.contains("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""))
			{
				String term = data.substring(data.indexOf("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""));
				int start = term.indexOf("statuses_count&quot;:");
				int end = term.indexOf(",&quot;lang&quot;");
				term = term.substring(start+21, end);
				term = term.replace(",","");
				result[1] = term;
			}
			
						
			//đếm số follower
			if(data != null && data.contains("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""))
			{
				String term = data.substring(data.indexOf("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""));
				int start = term.indexOf("followers_count&quot;:");
				int end = term.indexOf(",&quot;friends_count&quot;");
				term = term.substring(start+22, end);
				term = term.replace(",","");
				result[2] = term;
			}
			
			//đếm số friend - following
			if(data != null && data.contains("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""))
			{
				String term = data.substring(data.indexOf("<input type=\"hidden\" id=\"init-data\" class=\"json-data\""));
				int start = term.indexOf("friends_count&quot;:");
				int end = term.indexOf(",&quot;listed_count&quot;");
				term = term.substring(start+20, end);
				term = term.replace(",","");
				result[3] = term;
			}
			
		} catch (UserNotFoundException e) {
			System.out.println("User Not Found "+user_name);
			// TODO: handle exception
		}
		
//		if(result[0]!=null)
//		{
//			//System.out.println(user_name+"\t"+result[0]+"\t"+"create_date "+create_date+"\t"+"tweet "+tweet+ "\t"+ "friend "+friend +"\t"+ "follower " +follower);
//			for (String string : result) {
//				System.out.print(string+"\t");
//			}
//		}
//		else
//			System.out.println(user_name+" is not available!");
//		System.out.println();
		
		return result;
	}
	public String[] getContentFromId(long tweetID, String user_name)
	{
		//0 la content, 1 la favourite, 2 la retweet, 3 la date create
		String[] result= new String[4];//0 la noi dung, 1 la favorite, 2 retweet, 3 create data
		String url = "https://twitter.com/"+user_name+"/status/"+tweetID;
		String data = null;
		//int favorites = 0;
		try {
			//Thread.sleep(5000);
			data = this.getURL(url,true);
			String tag = "<div class=\"stream-item-footer clearfix\">";
			if(data.contains(tag))
			{
				System.out.println(tweetID+"  is reply");
				return null;
			}
			//favourite
			if(data != null && data.contains("<li class=\"js-stat-count js-stat-favorites stat-count\">")){
				String term = data.substring(data.indexOf("<li class=\"js-stat-count js-stat-favorites stat-count\">"));
				int start = term.indexOf("<strong>");
				int end = term.indexOf("</strong>");
				term = term.substring(start+8, end);
				term = term.replace(",","");
				
				result[1] = term;
			}
			else result[1] = "0";
			//retweet
			if(data != null && data.contains("<li class=\"js-stat-count js-stat-retweets stat-count\">")){
				String term = data.substring(data.indexOf("<li class=\"js-stat-count js-stat-retweets stat-count\">"));
				int start = term.indexOf("<strong>");
				int end = term.indexOf("</strong>");
				term = term.substring(start+8, end);
				term = term.replace(",","");
				result[2] = term;
			}
			else result[2] = "0";
			//date create
			if(data != null && data.contains("<div class=\"client-and-actions\">")){
				String term = data.substring(data.indexOf("<div class=\"client-and-actions\">"));
				int start = term.indexOf("<span class=\"metadata\">");
				int end = term.indexOf("</span>");
				term = term.substring(start+34, end);
				result[3] = term;
				
			}
			//content
		//	String tag = "<div class=\"stream-item-footer clearfix\">";
			while(data.contains(tag))//tim chuoi chua content
			{
				int begin = data.indexOf(tag)+tag.length()+1;
				int end = data.indexOf("<div class=\"stream-item-footer\">");
				data = data.substring(begin,end);
				
			}
			if(data != null && data.contains("<p class=\"js-tweet-text tweet-text\">")){
				String term = data.substring(data.indexOf("<p class=\"js-tweet-text tweet-text\">"));
				//int start = term.indexOf("\">");
				int end = term.indexOf("</p>");
				term = term.substring(0, end);
				term = term.replace("&#39;","'");
				result[0] = term.replaceAll("\\<.*?>","");
				
			}
			//System.out.println(result[0]+"\t"+result[1]+"\t"+result[2]+"\t"+result[3]);
			
		} catch (UserNotFoundException e) {
			System.out.println("User Not Found "+user_name);
			// TODO: handle exception
		}
		if(result[0]!=null)
		{
			return result;
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
	public static boolean IsRetweet(String tweet, String path_result) throws IOException
	{
		tweet = RemoveTerm(tweet, "http://");
		tweet = RemoveTerm(tweet, "#");
		tweet = RemoveTerm(tweet, "@");
		tweet = tweet.replaceAll("\\s+", "");
		BufferedReader reader = new BufferedReader(new FileReader(path_result));
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
	public void CrawlContent(String path, String path_result) throws NumberFormatException, Throwable
	{
		Crawler crawler = new Crawler();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String term;
		int dem = 0;
		
		while(( term = reader.readLine())!=null)
		{		
			PrintWriter pw = new PrintWriter(new FileWriter(path_result, true));
			String[] parts = term.split(" ");
			
			String content[] = crawler.getContentFromId(Long.parseLong(parts[1]), parts[3]);
			if(content!=null)
			{
				//if(IsRetweet(content, path_result)) continue;
				//
				pw.println(parts[0]+"\t"+ parts[1]+ 
						"\t"+parts[2]+"\t"+parts[3]+"\t"+"\""+content[0]+"\""
						+"\t"+content[1]+"\t"+content[2]+"\t"+content[3]);
				dem++;
				System.out.println(dem+"---\t"+parts[0]+" "+ parts[1]+ 
						" "+parts[2]+" "+parts[3]+" "+"\""+content[0]+"\""
						+" "+content[1]+" "+content[2]+" "+content[3]);
				//if(dem==20) break;
				
			}
			else {
				//System.out.println("tweet broken:" + parts[1]);
			}
			pw.close();		
		}	
		reader.close();
	}
	public void CrawlUser(String path, String path_result) throws IOException 
	{
		Crawler crawler = new Crawler();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String term;
		int dem = 0;
		
		while(( term = reader.readLine())!=null)
		{		
			PrintWriter pw = new PrintWriter(new FileWriter(path_result, true));
			String[] parts = term.split("\t");
			if(IsOvertide(parts[3], path_result))
			{
				System.out.println("Override");	
				continue;
			}
			String content[] = crawler.getUserInfo(parts[3]);
			if(content!=null)
			{	
				//
				pw.println(parts[3]+"\t"+content[0]+"\t"+content[1]+"\t"+content[2]+"\t"+content[3]+"\t"+content[4]);
				dem++;
				File tem = new File(path);
				System.out.println(dem+"."+tem.getName()+"------"+"\t"+parts[3]+"\t"+content[0]+"\t"+content[1]+"\t"+content[2]+"\t"+content[3]+"\t"+content[4]);
				//if(dem==20) break;
				
			}
			else {
				//System.out.println("tweet broken:" + parts[1]);
			}
			pw.close();		
		}	
		reader.close();
	}

	public boolean IsOvertide(String string, String path_result) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader(path_result));
		String term;
		while(( term = reader.readLine())!=null)
		{
			String parts[] = term.split("\t");
			if(string.equals(parts[0]))
			{
				reader.close();
				System.out.println(string+"  overide!");
				return true;
			}
		}
		reader.close();
		return false;
	}
}
