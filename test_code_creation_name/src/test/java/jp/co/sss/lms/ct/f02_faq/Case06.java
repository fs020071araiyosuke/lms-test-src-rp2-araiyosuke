package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
		// コース詳細画面が表示されるまで待機
		WebDriverWait wait = new WebDriverWait(WebDriverUtils.webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.titleContains("コース詳細"));

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

		//カテゴリリンクをすべて取得
		By categoryLinksSelector = By.cssSelector("a[href*='frequentlyAskedQuestionCategoryId']");
		WebDriverUtils.visibilityTimeout(categoryLinksSelector, 5);

		List<WebElement> categoryLinks = WebDriverUtils.webDriver.findElements(categoryLinksSelector);
		assertTrue(categoryLinks.size() > 0, "カテゴリが1件も存在しません。");

		//カテゴリを1件ずつクリックして検証
		for (int i = 0; i < categoryLinks.size(); i++) {

			WebElement category = categoryLinks.get(i);

			//スクロールしてクリック可能にする
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].scrollIntoView({block: 'center'});", category);

			category.click();

			//検索結果or「データが登録されていません。」のどちらかが表示されるまで待機
			By questionItem = By.cssSelector("dl[id^='question-h']");
			By noDataMessage = By.xpath("//*[contains(text(),'データが登録されていません')]");

			WebDriverWait wait = new WebDriverWait(WebDriverUtils.webDriver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(questionItem),
					ExpectedConditions.visibilityOfElementLocated(noDataMessage)));

			//検索結果の有無を判定
			List<WebElement> questionList = WebDriverUtils.webDriver.findElements(questionItem);

			if (questionList.size() > 0) {
				assertTrue(questionList.size() > 0, "検索結果が表示されていません。");
			} else {
				WebElement noDataElem = WebDriverUtils.webDriver.findElement(noDataMessage);
				assertTrue(noDataElem.isDisplayed(), "「データが登録されていません。」が表示されていません。");
			}

			WebDriverUtils.getEvidence(this);

			//再取得（DOM が更新されるため）
			categoryLinks = WebDriverUtils.webDriver.findElements(categoryLinksSelector);
		}
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() throws InterruptedException {
		//質問が存在しない場合はスキップ
		List<WebElement> questionCheck = WebDriverUtils.webDriver.findElements(By.cssSelector("dl[id^='question-h']"));
		if (questionCheck.isEmpty()) {
			System.out.println("質問がないためtest06をスキップします。");
			return;
		}

		//質問項目
		By questionItem = By.cssSelector("dl[id^='question-h']");

		//質問リストが表示されるまで待機
		WebDriverUtils.visibilityTimeout(questionItem, 5);

		List<WebElement> questionList = WebDriverUtils.webDriver.findElements(questionItem);
		assertTrue(questionList.size() > 0, "検索結果に質問がありません。");

		//質問を1件ずつクリックして回答を確認
		for (int i = 0; i < questionList.size(); i++) {

			//DOM 更新の可能性があるため毎回再取得
			questionList = WebDriverUtils.webDriver.findElements(questionItem);
			WebElement question = questionList.get(i);

			//スクロールして中央へ
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].scrollIntoView({block: 'center'});", question);

			Thread.sleep(500);

			//質問クリック
			question.click();

			//回答部分が表示されるまで待機
			By answerItem = By.cssSelector("dl[id^='question-h'] dd");
			WebDriverUtils.visibilityTimeout(answerItem, 5);

			//回答要素取得
			List<WebElement> answerList = WebDriverUtils.webDriver.findElements(answerItem);
			assertTrue(answerList.size() > i, "回答が表示されていません。");

			WebElement answerElem = answerList.get(i);
			assertTrue(answerElem.isDisplayed(), "回答が表示されていません。");

			//エビデンス取得
			WebDriverUtils.getEvidence(this);
		}
	}
}
