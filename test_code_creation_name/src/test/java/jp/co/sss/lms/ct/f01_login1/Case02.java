package jp.co.sss.lms.ct.f01_login1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// TODO ここに追加
		// 未登録ユーザーを入力
		WebElement idInput = driver.findElement(By.id("loginId"));
		WebElement pwInput = driver.findElement(By.id("password"));
		WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit']"));

		idInput.sendKeys("unknownUser"); // DBに存在しないユーザー
		pwInput.sendKeys("wrongpass"); // 適当なパスワード
		loginButton.click();

		// エラーメッセージの表示確認
		WebElement errorMsg = driver.findElement(By.xpath("//*[contains(text(), 'ログインに失敗しました')]"));

		assertTrue(errorMsg.isDisplayed());
		assertEquals("* ログインに失敗しました。", errorMsg.getText());

		// 画面遷移していないこと（タイトルがログインページのまま）
		assertEquals("ログイン | LMS", driver.getTitle());
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
