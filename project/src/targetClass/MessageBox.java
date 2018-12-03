package targetClass;

import java.util.ArrayList;
import java.util.List;

/**
 * メッセージを保持するクラス
 */
public class MessageBox {

	// メッセージリストにfinal修飾子をつけている理由は、他のリストに変更させないため
	/** メッセージリスト */
	public static final List<String> messageList = new ArrayList<>();

	/**
	 * メッセージリストの中身を表示する。
	 */
	public static void showMessages() {
		// TODO メッセージリストの中身を表示する処理を実装する
		System.out.println("showMessagesメソッドはまだ実装されていません。");
	}
}
