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
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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

		// ログイン画面へアクセス
		WebDriverUtils.goTo("http://localhost:8080/lms/");

		// タイトル確認
		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		// ロケータ定義
		By idInput = By.id("loginId");
		By pwInput = By.id("password");
		By loginButton = By.cssSelector("input[type='submit']");

		// 入力欄が表示されるまで待機
		WebDriverUtils.visibilityTimeout(idInput, 5);

		// 要素取得
		WebElement idElem = WebDriverUtils.webDriver.findElement(idInput);
		WebElement pwElem = WebDriverUtils.webDriver.findElement(pwInput);
		WebElement loginBtnElem = WebDriverUtils.webDriver.findElement(loginButton);

		// 初回ログイン済みユーザーでログイン
		idElem.sendKeys("StudentAA01");
		pwElem.sendKeys("Yousuke6");
		loginBtnElem.click();

		// コース詳細画面へ遷移
		WebDriverUtils.goTo("http://localhost:8080/lms/course/detail");

		// タイトル確認
		assertEquals("コース詳細 | LMS", WebDriverUtils.webDriver.getTitle());

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}
}
