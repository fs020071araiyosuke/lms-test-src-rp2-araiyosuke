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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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

		//By courseHeader = By.xpath("//li[@class='active' and contains(text(),'コース詳細')]");
		//WebDriverUtils.visibilityTimeout(courseHeader, 5);
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
		//TODO ここに追加
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
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		//TODO ここに追加
		//カテゴリID=1 のリンク
		By categoryLink = By.cssSelector("a[href='/lms/faq?frequentlyAskedQuestionCategoryId=1']");

		//カテゴリリンクが表示されるまで待機
		WebDriverUtils.visibilityTimeout(categoryLink, 5);

		//クリック
		WebElement categoryElem = WebDriverUtils.webDriver.findElement(categoryLink);
		categoryElem.click();

		//検索結果の質問リストが表示されるまで待機
		By firstQuestion = By.cssSelector("dl[id^='question-h']");
		WebDriverUtils.visibilityTimeout(firstQuestion, 5);

		//検索結果が表示されていることを確認
		List<WebElement> questionList = WebDriverUtils.webDriver.findElements(firstQuestion);
		assertTrue(questionList.size() > 0, "データが登録されていません。");

		WebDriverUtils.getEvidence(this);
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() throws InterruptedException {
		//TODO ここに追加
		//質問項目
		By questionItem = By.cssSelector("dl[id^='question-h']");

		//質問リストが表示されるまで待機
		WebDriverUtils.visibilityTimeout(questionItem, 5);

		//最初の質問要素を取得
		WebElement firstQuestion = WebDriverUtils.webDriver.findElements(questionItem).get(0);

		//クリック前にスクロールして要素を画面中央へ
		((JavascriptExecutor) WebDriverUtils.webDriver)
				.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstQuestion);

		//待機（スクロール後の安定化）
		Thread.sleep(500);

		//質問をクリック
		firstQuestion.click();

		//回答部分が表示されるまで待機
		By answerItem = By.cssSelector("dl[id^='question-h'] dd");
		WebDriverUtils.visibilityTimeout(answerItem, 5);

		//回答が表示されていることを確認
		WebElement answerElem = WebDriverUtils.webDriver.findElement(answerItem);
		assertTrue(answerElem.isDisplayed(), "回答が表示されていません");

		WebDriverUtils.getEvidence(this);
	}
}
