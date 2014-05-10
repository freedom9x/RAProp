package dataset;

import java.io.File;
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
}
