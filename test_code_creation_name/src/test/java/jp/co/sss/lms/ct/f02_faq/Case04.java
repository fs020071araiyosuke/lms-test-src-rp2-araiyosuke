package jp.co.sss.lms.ct.f02_faq;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	/** WebDriver*/
	private static WebDriver driver;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		// ログイン画面へアクセス
		String url = "http://localhost:8080/lms/";
		driver.get(url);
		// タイトル確認（ログインページであること）
		assertEquals("ログイン | LMS", driver.getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		//ユーザー入力
		WebElement id = driver.findElement(By.id("loginId"));
		WebElement pw = driver.findElement(By.id("password"));
		WebElement loginBtn = driver.findElement(By.cssSelector("input[type='submit']"));

		//初回ログイン済みユーザー（DB に存在するユーザー）
		id.sendKeys("StudentAA01");
		pw.sendKeys("Yousuke6");
		loginBtn.click();

		//コース詳細画面へアクセス
		String url = "http://localhost:8080/lms/course/detail";
		driver.get(url);
		//コース詳細画面
		assertEquals("コース詳細 | LMS", driver.getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//TODO ここに追加
		//機能タブをクリック
		WebElement functionTab = driver.findElement(By.cssSelector("li.dropdown > a"));
		functionTab.click();

		//ヘルプボタンをクリック
		WebElement helpButton = driver.findElement(By.cssSelector("a[href='/lms/help']"));
		helpButton.click();

		//ヘルプ画面へアクセス
		String url = "http://localhost:8080/lms/help";
		driver.get(url);
		//ヘルプ画面
		assertEquals("ヘルプ | LMS", driver.getTitle());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//TODO ここに追加
		//よくある質問リンクをクリック（id はあなたのHTMLに合わせて変更）
		WebElement faqLink = driver.findElement(By.cssSelector("a[href='/lms/faq']"));
		faqLink.click();
		//よくある質問画面へアクセス
		String url = "http://localhost:8080/lms/faq";
		driver.get(url);
		//ヘルプ画面
		assertEquals("よくある質問 | LMS", driver.getTitle());
		//画像保存
		takeScreenshot();
	}

	//画像保存メソッドを追加
	private void takeScreenshot() {
		try {
			// クラス名を取得
			String className = this.getClass().getSimpleName();
			// 日時を yyyyMMdd_HHmmss 形式で作成
			String timestamp = java.time.LocalDateTime.now()
					.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// 保存先：プロジェクト直下の /evidence/
			File dest = new File("evidence/" + className + "_" + timestamp + ".png");

			// フォルダが無ければ作成
			dest.getParentFile().mkdirs();

			Files.copy(src.toPath(), dest.toPath());
			System.out.println("スクリーンショット保存: " + dest.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// WebDriver の生成・破棄
	private static void createDriver() {
		if (driver == null) {
			ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver(options);
		}
	}

	private static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}
