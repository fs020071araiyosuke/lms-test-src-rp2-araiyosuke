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
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	@BeforeAll
	static void before() {
		WebDriverUtils.createDriver();
	}

	@AfterAll
	static void after() {
		WebDriverUtils.closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		WebDriverUtils.webDriver.get("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());
		WebDriverUtils.getEvidence(this);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {

		// ロケータ定義
		By idInput = By.id("loginId");
		By pwInput = By.id("password");
		By loginButton = By.cssSelector("input[type='submit']");
		By errorMsg = By.xpath("//*[contains(text(), 'ログインに失敗しました')]");

		// 入力欄が表示されるまで待機
		WebDriverUtils.visibilityTimeout(idInput, 5);

		// 要素取得
		WebElement idElem = WebDriverUtils.webDriver.findElement(idInput);
		WebElement pwElem = WebDriverUtils.webDriver.findElement(pwInput);
		WebElement loginBtnElem = WebDriverUtils.webDriver.findElement(loginButton);

		// 未登録ユーザーでログイン
		idElem.sendKeys("unknownUser");
		pwElem.sendKeys("wrongpass");
		loginBtnElem.click();

		// エラーメッセージが表示されるまで待機
		WebDriverUtils.visibilityTimeout(errorMsg, 5);

		// エラーメッセージ確認
		WebElement errorElem = WebDriverUtils.webDriver.findElement(errorMsg);
		assertTrue(errorElem.isDisplayed());
		assertEquals("* ログインに失敗しました。", errorElem.getText());

		// タイトルがログインページのまま
		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}
}
