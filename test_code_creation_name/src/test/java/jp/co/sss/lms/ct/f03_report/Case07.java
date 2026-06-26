package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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

		WebDriverWait wait = new WebDriverWait(WebDriverUtils.webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.titleContains("コース詳細"));

		assertEquals("コース詳細 | LMS", WebDriverUtils.webDriver.getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		WebDriver driver = WebDriverUtils.webDriver;

		// 未提出の行の詳細ボタンを取得
		By detailButton = By.xpath("//td[@class='w10per']/span[text()='未提出']/ancestor::tr//input[@value='詳細']");
		WebElement detailBtnElem = WebDriverUtils.webDriver.findElement(detailButton);

		// ボタンまでスクロール
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", detailBtnElem);

		// ボタンがクリック可能になるまで待機
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(detailBtnElem));

		// 詳細ボタンクリック
		detailBtnElem.click();

		// セクション詳細画面のタイトルが表示されるまで待機
		wait.until(ExpectedConditions.titleContains("セクション詳細"));

		// タイトル検証
		assertEquals("セクション詳細 | LMS", WebDriverUtils.webDriver.getTitle());

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
		WebDriver driver = WebDriverUtils.webDriver;

		// 「日報【デモ】を提出する」ボタン
		By submitReportButton = By.xpath("//input[@type='submit' and @value='日報【デモ】を提出する']");

		// ボタンが表示されるまで待機
		WebDriverUtils.visibilityTimeout(submitReportButton, 5);

		WebElement submitBtnElem = driver.findElement(submitReportButton);

		// ボタンまでスクロール
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", submitBtnElem);

		// クリック可能になるまで待機
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitBtnElem));

		// 通常クリック
		submitBtnElem.click();

		// レポート登録画面のタイトルが表示されるまで待機
		wait.until(ExpectedConditions.titleContains("レポート登録"));

		// タイトル検証
		assertEquals("レポート登録 | LMS", driver.getTitle());

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// TODO ここに追加
		WebDriver driver = WebDriverUtils.webDriver;

		// テキストエリア
		By textarea = By.id("content_0");
		WebDriverUtils.visibilityTimeout(textarea, 5);

		WebElement textareaElem = driver.findElement(textarea);

		// 入力
		textareaElem.clear();
		textareaElem.sendKeys("テストケース完了しました。");

		// 提出ボタン
		By submitButton = By.xpath("//button[@type='submit' and text()='提出する']");
		WebDriverUtils.visibilityTimeout(submitButton, 5);

		WebElement submitBtnElem = driver.findElement(submitButton);

		// ボタンまでスクロール
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", submitBtnElem);

		// クリック可能になるまで待機
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitBtnElem));

		// 通常クリック
		submitBtnElem.click();

		// セクション詳細画面に戻るまで待機
		wait.until(ExpectedConditions.titleContains("セクション詳細"));

		// タイトル検証
		assertEquals("セクション詳細 | LMS", driver.getTitle());

		// 提出済みボタンの検証
		By submittedButton = By.xpath("//input[@type='submit' and @value='提出済み日報【デモ】を確認する']");

		// ボタンが表示されるまで待機
		WebDriverUtils.visibilityTimeout(submittedButton, 5);
		WebElement submittedBtnElem = driver.findElement(submittedButton);

		// ボタン名の変更を確認
		assertEquals("提出済み日報【デモ】を確認する", submittedBtnElem.getAttribute("value"));

		// エビデンス取得
		WebDriverUtils.getEvidence(this);
	}

}
