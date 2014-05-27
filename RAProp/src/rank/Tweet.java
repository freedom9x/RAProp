package rank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class Tweet {
	//9 features
	public long ID;
	public String user_name;
	public String content;
	public String img_url;
	public String source;
	public String queryID;
	public int retweet_count;
	public int follow_count;
	public int friend_count;
	public int label;
	public String hash_tag;
	public int feature_score;
	public int rank_score;
	public int favorite_count;
	public Date date_create;
	public double total_AG = 0.0;
	
	/*public Tweet(String text_info)//example: text_info= "id username  follow_count friend_count img_url source retweet_count content"
	{
		text_info = pre_process(text_info);// pre process text_info
		String[] part = text_info.split(" ");// 8 part
		
		for(int i = 0; i<9; i++)
		{
			switch (i) {
			case 0:
				ID = Integer.parseInt(part[i]);
			case 1:
				user_name = part[i];
			case 2:
				content= part[i];
			case 3:
				img_url = part[i];
			case 4:
				source = part[i];
			case 5:
				retweet_count = Integer.parseInt(part[i]);
			case 6:
				follow_count = Integer.parseInt(part[i]);
			case 7:
				friend_count = Integer.parseInt(part[i]);
			case 8:
				hash_tag = part[i];
				break;

			default:
				break;
			}
		}
	}*/
	public Tweet(String content, long id)
	{
		this.content = content;
		this.ID = id;
	}
	public Tweet(String queryID,long id, int label, String user_name, String content,
			 int retweet, int favourite, String date ) throws ParseException
	{
		this.queryID = queryID;
		this.ID = id;
		this.label = label;
		this.user_name = user_name;
		this.content = content;
		this.favorite_count = favourite;
		this.retweet_count = retweet;
		//9:14 AM - 31 Jan 2011
		this.date_create = new SimpleDateFormat("h:mm a - dd MMM yyyy", Locale.ENGLISH).parse(date);
	//	this.ID = id;
	}

	public Tweet(String content) {
		// TODO Auto-generated constructor stub
		this.content = content;
	}
	public void rmv_stopword() {
		// TODO Auto-generated method stub
		this.content = this.content.replace("(", " ");
		this.content = this.content.replace(")", " ");
		this.content = this.content.replace("\"", " ");
		this.content = this.content.replace("“", " ");
		this.content = this.content.replace("#", " ");
		this.content = this.content.replace(",", " ");
		this.content = this.content.replace(".", " ");
		this.content = this.content.replace("-", " ");
		this.content = this.content.replace(";", " ");
		this.content = this.content.replace(":", " ");
		this.content = this.content.replace("!", " ");
		this.content = this.content.replace("...", " ");
		this.content = this.content.trim().replaceAll("\\s+", " ");
		//remove
//		for( int i = 0; i<this.content.length(); i++)
//		{
//			int temp = (int)content.charAt(i);
//		}
		String [] stop_words = {"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"for", "if", "in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "this", "to", "was", "will", "with"};
		
		//String[] parts = this.content.split(" ");
		
		for (String string : stop_words) {
			if(content.contains(string))
			{
				String regex = "\\s*\\b"+string+"\\b\\s*";
				content=content.replaceAll(regex, " ");
			}
		}
		content= content.toLowerCase();
		this.content = this.content.replaceAll("( )+", " ");
		
	}
	public static Tweet GetTweetFromID(long id, Tweet[] tweets)
	{
		for (Tweet tweet : tweets) {
			if(tweet.ID == id) return tweet;
		}
		Tweet term = null;
		return term;
	}
	
	public void Remove1()
	{
		String result = "";
		List<String> list = Arrays.asList(this.content.split(" "));
		for (String string : list) {
			if(string.contains("http://")|string.contains("@")) continue;
			result =result+ string+" ";
		}
		this.content = result;
		
	}
	public static String Remove1(String content)
	{
		String result = "";
		List<String> list = Arrays.asList(content.split(" "));
		for (String string : list) {
			if(string.contains("#")|string.contains("@") 
					|string.contains("http://") | string.contains("www.") )
				continue;
			result =result+ string+" ";
		}
		result = result.trim().replaceAll("\\s+", " ");
		return result;
		
	}
	public static String rmv_stopword(String term) {
		// TODO Auto-generated method stub
		//String result = "";
		term = term.replace("(", " ");
		term = term.replace(")", " ");
		term = term.replace("\"", " ");
		term = term.replace("“", " ");
		term = term.replace("#", " ");
		term = term.replace(",", " ");
		term = term.replace(".", " ");
		term = term.replace("-", " ");
		term = term.replace(";", " ");
		term = term.replace(":", " ");
		term = term.replace("!", " ");
		term = term.replace("...", " ");
		term = term.replace("'s", " ");
		term = term.replace("'", " ");
		term = term.trim().replaceAll("\\s+", " ");
		//remove
//		for( int i = 0; i<this.content.length(); i++)
//		{
//			int temp = (int)content.charAt(i);
//		}
		String [] stop_words = {"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"for", "if", "in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "this", "to", "was", "will", "with"};
		
		//String[] parts = this.content.split(" ");
		
		for (String string : stop_words) {
			if(term.contains(string))
			{
				String regex = "\\s*\\b"+string+"\\b\\s*";
				term=term.replaceAll(regex, " ");
			}
		}
		//term=term.toLowerCase();
		term=term.replaceAll("( )+", " ");
		return term;
		
	}
}
