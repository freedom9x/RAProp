package Main;

import java.io.IOException;

import rank.AG_graph;
import vali.TopK_result;
import dataset.CommonLib;
import dataset.CreateData;
import dataset.ReadFile;

public class MainAG {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CreateData.main("tweet2011/");// lay tweet, bo reteet, lu trong test folder
//		AG_graph.main("BBC World Service staff cuts", "MB01", "tweet2011/test/MB01_AG.txt");
//		TopK_result.main("tweet2011/test/MB01_AG.txt", "tweet2011/result/AG/MB01_ag.txt");
		AG_graph.main("2022 FIFA soccer", "MB02", "tweet2011/test/MB02_AG.txt");
////		TopK_result.main("tweet2011/test/MB02_AG.txt", "tweet2011/result/AG/MB02_ag.txt");
//		TopK_result.TopK("tweet2011/result/AG/MB01_result.txt");
//		TopK_result.TopK("tweet2011/result/AG/MB02_result.txt");
		
		//CommonLib.DevideFile("")
	}

}
