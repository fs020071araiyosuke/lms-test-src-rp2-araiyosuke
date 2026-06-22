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

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース01
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース01 ログイン画面への遷移")
public class Case01 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		WebDriverUtils.createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		WebDriverUtils.closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		// ログイン画面へアクセス
		String url = "http://localhost:8080/lms/";
		WebDriverUtils.goTo(url);

		// タイトル確認（ログインページであること）
		assertEquals("ログイン | LMS", WebDriverUtils.webDriver.getTitle());

		// ログイン画面の主要要素が表示されていることを確認
		By idInput = By.id("loginId");

		// 要素が表示されるまで待機
		WebDriverUtils.visibilityTimeout(idInput, 5);

		assertTrue(WebDriverUtils.webDriver.findElement(idInput).isDisplayed());

		// エビデンス取得（メソッド名付き）
		WebDriverUtils.getEvidence(this);
	}
}
