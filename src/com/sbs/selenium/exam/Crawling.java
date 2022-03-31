package com.sbs.selenium.exam;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Crawling {
	//constructors
	String searchWord = "";
	String startDate = "";
	String finishDate = "";
	public Crawling(String searchWord, String startDate, String finishDate) {
		super();
		this.searchWord = searchWord;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}	
	
	
	//method that is returning ChromeDriver object 
	private ChromeDriver getChromDriverObject () throws InterruptedException  {
		// get path of the chromedriver
		String currentDir = System.getProperty("user.dir");
		Path path = Paths.get(currentDir + "\\recourses\\chromedriver.exe");
		
		// set webdriver's path
		System.setProperty("webdriver.chrome.driver", path.toString());
		
		// set webdriver's option
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized"); // run full-screen
		options.addArguments("--disable-popup-blcking"); // ignore pop-up
		options.addArguments("--disable-default-apps"); // do not use default apps
		
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}
	
	// crawling articles' information (title, press name, publishing date, link)
	public ArrayList<ArrayList<String>> getArticles () throws InterruptedException {
		ChromeDriver driver = getChromDriverObject();
		// create blank tab
		driver.executeScript("window.open('about:blank','_blank');");
		
		List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		
		//convert to first tab
		driver.switchTo().window(tabs.get(0));
		//input search word and search (with period)
		driver.get("https://search.naver.com/search.naver?where=news&query=" + this.searchWord + "&nso=so%3Ar%2Cp%3Afrom" + this.startDate + "to" + this.finishDate);
		String goNextOrNot = driver.findElement(By.className("btn_next")).getAttribute("href"); // get href attribute for next page		
		ArrayList<ArrayList<String>> infos = new ArrayList<ArrayList<String>>(); //arraylist for containing the articles' info
		//visit all pages and get articles
		while (true) {
			if (goNextOrNot != null) {
				// get element which has title, published date, link, firm
				List<WebElement> articles = driver.findElements(By.className("news_wrap"));

				// get information of the articles and put into arraylist
				for (WebElement article : articles) {
					ArrayList<String> contentsOfArticle = new ArrayList<String>();
					String title = article.findElement(By.cssSelector("a.news_tit")).getText();
					String pressName = article.findElement(By.cssSelector("a.info.press")).getText();
					String datePublished = article.findElements(By.cssSelector("span.info")).get(0).getText();
					String link = article.findElement(By.cssSelector("a.news_tit")).getAttribute("href");
					contentsOfArticle.add(title);
					contentsOfArticle.add(pressName);
					contentsOfArticle.add(datePublished);
					contentsOfArticle.add(link);
					String naverNewsLink = null;
					try {
						naverNewsLink = article.findElements(By.cssSelector("a.info")).get(1).getAttribute("href");						
					} catch (Exception e) {						
					}
					if (naverNewsLink != null && !naverNewsLink.contains("sports")) { // if there is naver news form, get content of the article (without sports news)
						contentsOfArticle.add(naverNewsLink);
					} else {
					}
					infos.add(contentsOfArticle);
				}
				
				// go to the next page
				driver.findElement(By.className("btn_next")).click();				
				try{ // sleep 1 sec
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				goNextOrNot = driver.findElement(By.className("btn_next")).getAttribute("href"); // get href attribute for next page						
			} else {
				// get element which has title, published date, link, firm
				List<WebElement> articles = driver.findElements(By.className("news_wrap"));

				// get information of the articles and put into arraylist
				for (WebElement article : articles) {
					ArrayList<String> contentsOfArticle = new ArrayList<String>();
					String title = article.findElement(By.cssSelector("a.news_tit")).getText();
					String pressName = article.findElement(By.cssSelector("a.info.press")).getText();
					String datePublished = article.findElements(By.cssSelector("span.info")).get(0).getText();
					String link = article.findElement(By.cssSelector("a.news_tit")).getAttribute("href");
					contentsOfArticle.add(title);
					contentsOfArticle.add(pressName);
					contentsOfArticle.add(datePublished);
					contentsOfArticle.add(link);
					String naverNewsLink = null;
					try {
						naverNewsLink = article.findElements(By.cssSelector("a.info")).get(1).getAttribute("href");						
					} catch (Exception e) {						
					}
					if (naverNewsLink != null && !naverNewsLink.contains("sports")) { // if there is naver news form, get content of the article (without sports news)
						contentsOfArticle.add(naverNewsLink);
					} else {
					}
					infos.add(contentsOfArticle);
				}
				driver.quit();
				System.out.println("complete crawling!");
				break;
			}
		}
		return infos;
	}

}
