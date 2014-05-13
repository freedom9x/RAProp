package dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import rank.Tweet;

public class CreateData {

	public static void main(String path) throws Exception {//lay tweet, loc retweet
		// TODO Auto-generated method stub
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles)
		{
			String name = file.getName().split("\\.")[0];
			PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/test/"+name+".txt"));
			Tweet[] tweets = ReadFile.GetNtweet(file.getPath());
			for (Tweet t : tweets) 
			{				
				if (t != null) {
					if (t.content.contains("http://")) // chua short url
					{
						t.content = CommonLib.expandURL(t.content);
					}
					
					pw.println(t.queryID + " " + t.ID + " " + t.label + " "
								+ t.user_name + " \"" + t.content + "\"");
					System.out.println(t.ID+ "\t" + t.content);
				}
			}
			pw.close();
		}
	}
	public static void CreateQueryTable(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		int dem = 0;
		String term;
		PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/test/QueryTable.txt"));
		while(((term = reader.readLine())!=null))
		{
			if(term.contains("<num>"))
			{
				String term2 = reader.readLine();
				int begin = term.indexOf("<num>");
				String id = term.substring(begin+14,begin+18);
				begin = term2.indexOf("title");
				int end = term2.indexOf("</title>");
				String query  = term2.substring(begin+7, end);
				pw.println(id+"\t"+query);
			}
		}
		pw.close();		
		reader.close();
	}
	
}
