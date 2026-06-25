package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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

		// カテゴリIDごとの期待される質問一覧
		Map<Integer, List<String>> EXPECTED_QUESTIONS = Map.of(
				1, List.of(
						"Q.キャンセル料・途中退校について",
						"Q.研修の申し込みはどのようにすれば良いですか？"),
				2, List.of(
						"Q.セルフ・キャリアドック制度とは何か",
						"Q.事業所が変わった場合、何かしら手続きをする必要がありますか？",
						"Q.助成金書類の作成方法が分かりません"),
				3, List.of() // データなし
		);

		// 検証対象カテゴリID（1 → 2 → 3）
		List<Integer> categoryIds = List.of(1, 2, 3);

		for (Integer categoryId : categoryIds) {

			// カテゴリリンク取得
			By categorySelector = By.cssSelector("a[href*='frequentlyAskedQuestionCategoryId=" + categoryId + "']");
			WebDriverUtils.visibilityTimeout(categorySelector, 5);

			WebElement category = WebDriverUtils.webDriver.findElement(categorySelector);

			// スクロールしてクリック
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].scrollIntoView({block: 'center'});", category);
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].click();", category);

			// 検索結果 or 「データが登録されていません。」
			By questionItem = By.cssSelector("dl[id^='question-h']");
			By noDataMessage = By.xpath("//*[contains(text(),'データが登録されていません')]");

			WebDriverWait wait = new WebDriverWait(WebDriverUtils.webDriver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(questionItem),
					ExpectedConditions.visibilityOfElementLocated(noDataMessage)));

			// 実際の検索結果を取得
			List<WebElement> questionList = WebDriverUtils.webDriver.findElements(questionItem);

			List<String> expectedList = EXPECTED_QUESTIONS.get(categoryId);

			if (!expectedList.isEmpty()) {

				// ★ データがある場合：質問文が期待通りか検証
				assertEquals(expectedList.size(), questionList.size(),
						"カテゴリID=" + categoryId + " の質問数が一致しません。");

				for (int i = 0; i < questionList.size(); i++) {
					WebElement dt = questionList.get(i).findElement(By.tagName("dt"));
					String actualQuestion = dt.getText().trim();
					String expectedQuestion = expectedList.get(i);

					assertEquals(expectedQuestion, actualQuestion,
							"カテゴリID=" + categoryId + " の質問文が一致しません。");
				}

			} else {

				// データがない場合「データが登録されていません。」
				WebElement noDataElem = WebDriverUtils.webDriver.findElement(noDataMessage);
				assertTrue(noDataElem.isDisplayed(),
						"カテゴリID=" + categoryId + " で「データが登録されていません。」が表示されていません。");
			}

			WebDriverUtils.getEvidence(this);
		}
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() throws InterruptedException {
		// QA対応（質問文をキーにする）
		Map<String, String> FAQ_DB = Map.of(
				"キャンセル料・途中退校について",
				"受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。",
				"研修の申し込みはどのようにすれば良いですか？",
				"営業担当がいる場合は、営業担当までご連絡ください。 申し込み方法についてご案内させていただきます。 なお、弊社営業営業がいない場合は、東京ITスクール運営事務局までご連絡いただけると幸いです。",
				"セルフ・キャリアドック制度とは何か",
				"労働者にジョブカードを活用した、キャリアコンサルタントによるキャリアコンサルティングを定期的に提供するものです。 なお、セルフ・キャリアドック制度を就業規則または労働協約に規定し、また、「セルフ・キャリアドック実施計画書」の作成が別途必要となります。",
				"事業所が変わった場合、何かしら手続きをする必要がありますか？",
				"以前は変更申請の必要がございましたが、2020年4月～は変更届の必要がなくなりました。",
				"助成金書類の作成方法が分かりません",
				"LMSマニュアルを参考に、LMSから助成金の書類をダウンロードしてください。 手引きもご用意させていただいておりますので、必ずご一読ください。 ダウンロードした助成金の書類には、基本的な御社の情報・研修情報が予め記載されております。 ご不明な点がございましたら、営業担当または東京ITスクール運営事務局までご連絡ください。");

		// まず研修関係をクリック
		By category1 = By.cssSelector("a[href*='frequentlyAskedQuestionCategoryId=1']");
		WebDriverUtils.visibilityTimeout(category1, 5);

		WebElement category = WebDriverUtils.webDriver.findElement(category1);

		((JavascriptExecutor) WebDriverUtils.webDriver)
				.executeScript("arguments[0].scrollIntoView({block: 'center'});", category);
		((JavascriptExecutor) WebDriverUtils.webDriver)
				.executeScript("arguments[0].click();", category);

		// 質問一覧が表示されるまで待機
		By questionItem = By.cssSelector("dl[id^='question-h']");
		WebDriverWait wait = new WebDriverWait(WebDriverUtils.webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(questionItem));

		List<WebElement> questionList = WebDriverUtils.webDriver.findElements(questionItem);
		assertFalse(questionList.isEmpty(), "質問が表示されていません。");

		for (WebElement dl : questionList) {

			WebElement dt = dl.findElement(By.tagName("dt"));
			String questionText = dt.getText().trim();

			// 先頭の "Q." を除去
			questionText = questionText.replaceFirst("^Q[\\.:：]?\\s*", "").trim();

			// 質問文が正しいデータに存在するかチェック
			assertTrue(FAQ_DB.containsKey(questionText),
					"想定外の質問が表示されています: " + questionText);

			// 質問をクリック
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].scrollIntoView({block: 'center'});", dt);
			Thread.sleep(300);
			((JavascriptExecutor) WebDriverUtils.webDriver)
					.executeScript("arguments[0].click();", dt);

			// 回答取得
			WebElement dd = dl.findElement(By.tagName("dd"));
			wait.until(ExpectedConditions.visibilityOf(dd));

			assertTrue(dd.isDisplayed(), "回答が表示されていません: " + questionText);

			String actualAnswer = dd.getText().trim();

			// 回答の先頭に "A." が付いている場合は除去
			actualAnswer = actualAnswer.replaceFirst("^A[\\.:：]?\\s*", "").trim();

			String expectedAnswer = FAQ_DB.get(questionText);

			// 質問＋回答のペアを検証
			String expectedPair = questionText + " → " + expectedAnswer;
			String actualPair = questionText + " → " + actualAnswer;

			assertEquals(expectedPair, actualPair,
					"質問と回答のペアが一致しません。\n" +
							"期待: " + expectedPair + "\n" +
							"実際: " + actualPair);

			WebDriverUtils.getEvidence(this);
		}
	}
}
