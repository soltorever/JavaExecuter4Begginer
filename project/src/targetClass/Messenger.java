package targetClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * メッセージの入力、送受信を行うクラス
 */
public class Messenger {

	/** 入力されたメッセージ */
	private String message = "";

	// 標準入力のReaderはcloseせず、フィールドに保持する。
	// (一度closeするとSystem.inもcloseされ、次に利用する際にIOExceptionが発生するため）
	/** メッセージ入力用のBufferedReader */
	private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * メッセージを入力する
	 * 
	 * @throws IOException 入出力でエラーが発生した場合
	 */
	public void input() throws IOException {
		try {
			System.out.println("メッセージを入力してください。");
			this.message = bufferedReader.readLine();

		} catch (IOException e) {
			System.out.println("入力処理でエラーが発生しました。");
			throw e;
		}

	}

	/**
	 * 入力されたメッセージを表示する
	 */
	public void showInput() {
		// TODO 入力されたメッセージを表示する処理を実装する
		System.out.println("showInputメソッドはまだ実装されていません。");
	}

	/**
	 * 入力されたメッセージをMessageBoxに保存する
	 * 
	 * @throws IOException 入出力でエラーが発生した場合
	 */
	public void send() throws IOException {
		try {
			// 確認メッセージ表示
			System.out.println("メッセージを送信しますか？");
			System.out.println("1.YEs");
			System.out.println("2.No");

			// 返答の入力
			String answer = bufferedReader.readLine();

			// 返答が「1」であれば送信を行い、入力メッセージを削除する
			if (answer.equals("1")) {
				MessageBox.messageList.add(this.message);
				this.message = "";
			}

		} catch (IOException e) {
			System.out.println("入力処理でエラーが発生しました。");
			throw e;
		}
	}

	/**
	 * MessageBoxから最初のメッセージを取得して表示する。
	 *
	 * @throws IOException 入出力でエラーが発生した場合
	 */
	public void get() throws IOException {
		try {
			// 確認メッセージ表示
			System.out.println("メッセージを受信しますか？");
			System.out.println("1.YEs");
			System.out.println("2.No");

			// 返答の入力
			String answer = bufferedReader.readLine();

			// 返答が「1」であれば送信を行い、入力メッセージを削除する
			if (answer.equals("1")) {
				// TODO MessageBoxクラス内のmessageListの最初のメッセージを取得して表示し削除する処理を実装する
				System.out.println("getメソッドのメッセージ取得処理はまだ実装されていません。");
			}

		} catch (IOException e) {
			System.out.println("入力処理でエラーが発生しました。");
			throw e;
		}
	}
}
