package jp.co.sss.lms.ct.f02_faq;

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
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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

		WebDriverUtils.goTo("http://localhost:8080/lms/");

		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		By idInput = By.id("loginId");
		By pwInput = By.id("password");
		By loginButton = By.cssSelector("input[type='submit']");

		// 入力欄が表示されるまで待機
		WebDriverUtils.visibilityTimeout(idInput, 5);

		// 要素取得
		WebElement idElem = WebDriverUtils.webDriver.findElement(idInput);
		WebElement pwElem = WebDriverUtils.webDriver.findElement(pwInput);
		WebElement loginBtnElem = WebDriverUtils.webDriver.findElement(loginButton);

		// ログイン
		idElem.sendKeys("StudentAA01");
		pwElem.sendKeys("Yousuke6");
		loginBtnElem.click();

		// コース詳細画面へ遷移
		WebDriverUtils.goTo("http://localhost:8080/lms/course/detail");

		assertEquals("コース詳細 | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		By functionTab = By.cssSelector("li.dropdown > a");
		By helpButton = By.cssSelector("a[href='/lms/help']");

		// メニュー表示待ち
		WebDriverUtils.visibilityTimeout(functionTab, 5);

		// 要素取得
		WebElement functionTabElem = WebDriverUtils.webDriver.findElement(functionTab);
		functionTabElem.click();

		WebElement helpButtonElem = WebDriverUtils.webDriver.findElement(helpButton);
		helpButtonElem.click();

		// ヘルプ画面へ遷移
		WebDriverUtils.goTo("http://localhost:8080/lms/help");

		assertEquals("ヘルプ | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を開く")
	void test04() {

		By faqLink = By.cssSelector("a[href='/lms/faq']");

		// FAQリンク表示待ち
		WebDriverUtils.visibilityTimeout(faqLink, 5);

		WebElement faqLinkElem = WebDriverUtils.webDriver.findElement(faqLink);
		faqLinkElem.click();

		// FAQ画面へ遷移
		WebDriverUtils.goTo("http://localhost:8080/lms/faq");

		assertEquals("よくある質問 | LMS", WebDriverUtils.webDriver.getTitle());

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}
}
