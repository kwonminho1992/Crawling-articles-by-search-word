package com.sbs.selenium.exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class ContentExtracting {
	Scanner scanner = new Scanner(System.in);
	
	public ArrayList<ArrayList<String>> wordExtractingFromContent () throws InterruptedException {
		// get content of articles by Jsoup			
		ArrayList<ArrayList<String>> infos = getInfos();
        try {
        	for (int i = 0; i < infos.size(); i++) {
        		if (infos.get(i).size() == 5) { // extract content that articles have naver news link only
                    Document doc = Jsoup.connect(infos.get(i).get(4)).get(); // url of naver news link

                    Elements elements = doc.select("#articleBodyContents");
                    String[] texts = elements.text().split("\\.");
                    String text = "";
                    for (int j = 0; j < texts.length; j++) {
                        text = text + "\n" + texts[j];
                    }
                    infos.get(i).add(text);
        		} else {
        		}
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		// Komoran test
		Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    	for (int i = 0; i < infos.size(); i++) {
    		if (infos.get(i).size() == 6) {
    			String strToAnalyze = infos.get(i).get(5);

    	        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);

    	        String[] words = analyzeResultList.getPlainText().split(" ");
    	        
    	        // check frequency of each word
    	        HashMap map = new HashMap();
    	        for (int j = 0; j < words.length; j++) {
    	        	// filter out unnecessary parts
    	        	// * please refer this site : https://docs.komoran.kr/firststep/postypes.html
    	            if (words[j].contains("NNG") || words[j].contains("NNP") || words[j].contains("NNB") || 
    	            		words[j].contains("NP") || words[j].contains("NR") || words[j].contains("NNG") || 
    	            		words[j].contains("VV") || words[j].contains("VA") ||  words[j].contains("MAG") || 
    	            		 words[j].contains("SL") ||  words[j].contains("SH") ||  words[j].contains("NF") ||  
    	            		 words[j].contains("NV")) { 
        	        	if (map.containsKey(words[j].split("/")[0])) {
        	            	Integer value = (Integer)map.get(words[j].split("/")[0]);
        	            	map.put(words[j].split("/")[0], new Integer(value.intValue() + 1)); // +1 
        	            } else {
        	            	map.put(words[j].split("/")[0], new Integer(1));
        	            }
    	            } else {
    	            }
    	        }
    	        
    	        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(map.entrySet()); 
    	        entryList.sort(new Comparator<Map.Entry<String, Integer>>() { // sort by descending order
    	            @Override
    	            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
    	        	return o2.getValue() - o1.getValue();
    	            }
    	        });
    	        String text = "";
    	        for(Map.Entry<String, Integer> entry : entryList){
    	        	//System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
    	        	text = text + ", " +entry.getKey() + " - " + entry.getValue() + "\n";
    	        }
    	        infos.get(i).add(text);
    		} else {
    		}
    	}  
    	System.out.println("complete words extracting!");
    	return infos;
	}
	
	//a method returns arraylist has articles' title, press name, date, link, naver news link
	private ArrayList<ArrayList<String>> getInfos () throws InterruptedException {
		//get user's input
		System.out.println("What is your search word? : ");
		String searchWord = scanner.nextLine(); 
		System.out.println("input start date (*form : yyyymmdd (ex. 20200301)");
		String startDate = scanner.nextLine();
		System.out.println("input finish date (*form : yyyymmdd (ex. 20220301)");
		String finishDate = scanner.nextLine();	
		//return arraylist has articles' title, press name, date, link, naver news link
		Crawling crawling = new Crawling(searchWord, startDate, finishDate);
		ArrayList<ArrayList<String>> infos = crawling.getArticles();
		return infos;
	}
	

}
