package jp.co.sss.lms.ct.f01_login1;

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
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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
