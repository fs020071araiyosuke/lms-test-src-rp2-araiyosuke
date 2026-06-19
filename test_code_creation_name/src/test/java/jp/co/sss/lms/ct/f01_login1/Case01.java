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
 * ケース01
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース01 ログイン画面への遷移")
public class Case01 {

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

		// ログイン画面の主要要素が表示されていることを確認
		WebElement idInput = driver.findElement(By.id("loginId"));
		WebElement pwInput = driver.findElement(By.id("password"));
		//WebElement loginButton = driver.findElement(By.id("login-button"));

		assertTrue(idInput.isDisplayed());
		assertTrue(pwInput.isDisplayed());
		//assertTrue(loginButton.isDisplayed());

		//スクリーンショット保存（evidence/.png）
		takeScreenshot();
	}

	//スクリーンショット保存メソッドを追加
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
