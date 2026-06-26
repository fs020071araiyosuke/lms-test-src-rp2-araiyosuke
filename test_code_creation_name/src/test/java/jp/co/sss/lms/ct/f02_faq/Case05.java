package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		WebDriverUtils.webDriver.get("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());
		WebDriverUtils.getEvidence(this);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		By idInput = By.id("loginId");
		By pwInput = By.id("password");
		By loginButton = By.cssSelector("input[type='submit']");

		WebDriverUtils.visibilityTimeout(idInput, 5);

		WebElement idElem = WebDriverUtils.webDriver.findElement(idInput);
		WebElement pwElem = WebDriverUtils.webDriver.findElement(pwInput);
		WebElement loginBtnElem = WebDriverUtils.webDriver.findElement(loginButton);

		idElem.sendKeys("StudentAA01");
		pwElem.sendKeys("Yousuke6");
		loginBtnElem.click();

		By courseHeader = By.xpath("//li[@class='active' and contains(text(),'コース詳細')]");
		WebDriverUtils.visibilityTimeout(courseHeader, 5);
		assertEquals("コース詳細 | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// TODO ここに追加
		By functionTab = By.cssSelector("li.dropdown > a");
		By helpButton = By.cssSelector("a[href='/lms/help']");

		WebDriverUtils.visibilityTimeout(functionTab, 5);

		WebElement functionTabElem = WebDriverUtils.webDriver.findElement(functionTab);
		functionTabElem.click();

		WebElement helpButtonElem = WebDriverUtils.webDriver.findElement(helpButton);
		helpButtonElem.click();

		assertEquals("ヘルプ | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// TODO ここに追加
		By faqLink = By.cssSelector("a[href='/lms/faq']");

		WebDriverUtils.visibilityTimeout(faqLink, 5);

		String originalTab = WebDriverUtils.webDriver.getWindowHandle();

		WebElement faqLinkElem = WebDriverUtils.webDriver.findElement(faqLink);
		faqLinkElem.click();

		for (String handle : WebDriverUtils.webDriver.getWindowHandles()) {
			if (!handle.equals(originalTab)) {
				WebDriverUtils.webDriver.switchTo().window(handle);
				break;
			}
		}
		assertEquals("よくある質問 | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// FAQタブへ切り替え
		for (String handle : WebDriverUtils.webDriver.getWindowHandles()) {
			WebDriverUtils.webDriver.switchTo().window(handle);
		}

		// 検索欄と検索ボタン
		By keywordInput = By.cssSelector("input[type='text']");
		By searchButton = By.cssSelector("input[type='submit']");

		WebDriverUtils.visibilityTimeout(keywordInput, 10);

		WebElement keywordElem = WebDriverUtils.webDriver.findElement(keywordInput);
		WebElement searchBtnElem = WebDriverUtils.webDriver.findElement(searchButton);

		//文字列検索
		String keyword = "";

		keywordElem.clear();
		keywordElem.sendKeys(keyword);

		// 検索実行
		searchBtnElem.click();

		assertEquals("よくある質問 | LMS", WebDriverUtils.webDriver.getTitle());

		// 質問タイトル
		By questionTitle = By.cssSelector("dl[id^='question-h'] dt");
		List<WebElement> questionList = WebDriverUtils.webDriver.findElements(questionTitle);

		// 「データが登録されていません。」の表示
		By noDataMessage = By.xpath("//*[contains(text(),'データが登録されていません')]");

		// 検索結果が0件の場合
		if (questionList.isEmpty()) {
			WebElement message = WebDriverUtils.webDriver.findElement(noDataMessage);

			assertTrue(message.isDisplayed(), "「データが登録されていません。」が表示されていません。");
			assertEquals("データが登録されていません。", message.getText(),
					"表示メッセージが期待値と一致しません。");
		}
		// 検索結果がある場合にキーワード一致の検証
		else {
			for (WebElement q : questionList) {
				assertTrue(
						q.getText().contains(keyword),
						"キーワードを含まない質問があります: " + q.getText());
			}
		}
		WebDriverUtils.getEvidence(this);
	}
}
