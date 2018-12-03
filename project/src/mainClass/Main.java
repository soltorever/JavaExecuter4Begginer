package mainClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 設定ファイルを読み込み、指定されたクラスのメソッドを実行するクラス。
 */
public class Main {
	
	/** 設定ファイルのファイルパス */
	private static final String SETTING_FILE_PATH = "config/ExecuteSettings.txt";

	/** 設定ファイルの情報 */
	private static List<String> settings = new ArrayList<>();

	/** 各クラスのインスタンスを保持 */
	private static Map<String, Object> objects = new HashMap<>();

	/**
	 * main関数
	 */
	public static void main(String[] args) {
		System.out.println("execute");
		// 設定ファイル読み込み
		Main.readSettings();

		// 実行処理
		for (String setting : settings) {

			// コメントアウトされておらずフォーマットが正しければ、実行する
			if (!Main.commentoutCheck(setting) && Main.formatCheck(setting)) {
				try {
					// 設定を表示
					System.out.println("<" + setting + ">");

					// 設定ファイルをカンマ区切りでパース
					String[] parsedSetting = setting.split(",");

					// メソッドを実行するインスタンスを取得
					Object obj = null;
					if (Main.objects.containsKey(parsedSetting[1])) {
						// 既にインスタンスを生成している場合
						obj = Main.objects.get(parsedSetting[1]);

					} else {
						// インスタンス未生成の場合、生成してマップに格納する
						obj = Class.forName("targetClass." + parsedSetting[0]).newInstance();
						Main.objects.put(parsedSetting[1], obj);
					}

					// メソッドを実行する
					int executeNum = Integer.parseInt(parsedSetting[3]);
					Method method = obj.getClass().getDeclaredMethod(parsedSetting[2],
							Main.getArgType(parsedSetting));

					for (int i = 0; i < executeNum; i++) {
						// 実行して戻り値を表示する
						System.out.println("戻り値：" + method.invoke(obj, getArgValue(parsedSetting)));
						System.out.println("");
					}

				} catch (ClassNotFoundException e) {
					System.out.println("対象のクラスが存在しません。");
					System.out.println("設定：" + setting);
					break;

				} catch (InstantiationException e) {
					System.out.println("対象のクラスに引数なしのコンストラクタが存在しません。");
					System.out.println("設定：" + setting);
					break;
					
				} catch (NoSuchMethodException e) {
					System.out.println("対象のメソッドが存在しません。");
					System.out.println("設定：" + setting);
					break;
					
				} catch (SecurityException | IllegalAccessException e) {
					System.out.println("対象のメソッドにアクセスできません。");
					System.out.println("設定：" + setting);
					break;
					
				} catch (IllegalArgumentException e) {
					System.out.println("引数の設定が誤っています。");
					System.out.println("設定：" + setting);
					break;
					
				} catch (InvocationTargetException e) {
					// 例外発生時はスタックトレースを表示する。
					Throwable error = e.getTargetException();
					error.printStackTrace();
					break;
				}
			}
		}
	}

	/**
	 * 設定ファイルを読み込む
	 */
	private static void readSettings() {
		try (BufferedReader br = new BufferedReader(new FileReader(Main.SETTING_FILE_PATH))) {
			String s = null;
			while ((s = br.readLine()) != null) {
				Main.settings.add(s);
			}
		} catch (IOException e) {
			// 設定ファイルが読み込めなかった場合、スタックトレースを表示する。
			e.printStackTrace();
			System.out.println("設定ファイルが読み込めませんでした。");
		}
	}

	/**
	 * 設定がコメントアウトされているかチェックする
	 *
	 * @param setting チェック対象の設定
	 * @return コメントアウトされていればtrue,それ以外はfalse
	 */
	private static boolean commentoutCheck(String setting) {
		return setting.matches("^#.*");
	}

	/**
	 * 設定のフォーマットをチェックする
	 * 
	 * @param setting チェック対象の設定
	 * @return チェックOKならばtrue,それ以外はfalse
	 */
	private static boolean formatCheck(String setting) {
		// nullチェック、空文字列チェック
		if (setting == null || setting.isEmpty()) {
			return false;
		}

		// 設定ファイルをカンマ区切りでパース
		String[] parsedSetting = setting.split(",");

		// 個数チェック
		if (parsedSetting.length < 4) {
			System.out.println("設定が不足しています。");
			System.out.println("設定：" + setting);
			return false;
		}

		// 設定値存在チェック
		for (String s : parsedSetting) {
			if (s.isEmpty()) {
				System.out.println("値がない設定が存在します。");
				System.out.println("設定：" + setting);
				return false;
			}
		}

		// 実行回数チェック
		if (!parsedSetting[3].matches("-?[1-9]*[0-9]")) {
			System.out.println("実行回数の指定が不正です。");
			System.out.println("設定：" + setting);
			return false;
		}

		// 引数チェック
		if (parsedSetting.length > 4) {
			for (int i = 4; i < parsedSetting.length; i++) {
				String s = parsedSetting[i];
				if (!(s.matches("^\".*\"$") || s.matches("-?[0-9]+") || s.matches("-?[0-9]+\\.[0-9]+"))) {
					System.out.println("引数に対応していない値が設定されています。");
					System.out.println("設定：" + setting);
					return false;
				}
			}
		}

		// チェックOK
		return true;
	}

	/**
	 * 引数の型情報を取得する
	 * 
	 * @param parsedSetting パースされた設定値の配列
	 * @return 引数の型の配列
	 */
	private static Class<?>[] getArgType(String[] parsedSetting) {
		// 引数の指定がなければnullを返却する
		if (parsedSetting.length < 5) {
			return null;
		}

		// 型情報を取得する
		Class[] result = new Class[parsedSetting.length - 4];
		for (int i = 4; i < parsedSetting.length; i++) {
			String s = parsedSetting[i];
			if (s.matches("^\".*\"$")) {
				result[i - 4] = String.class;
			} else if (s.matches("-?[0-9]+")) {
				result[i - 4] = int.class;
			} else if (s.matches("-?[0-9]+\\.[0-9]+")) {
				result[i - 4] = double.class;
			}
		}
		return result;
	}

	/**
	 * 引数の情報を取得する
	 * 
	 * @param parsedSetting パースされた設定値の配列
	 * @return 引数の型の配列
	 */
	private static Object[] getArgValue(String[] parsedSetting) {
		// 引数の指定がなければnullを返却する
		if (parsedSetting.length < 5) {
			return null;
		}

		// 引数を各クラスに変換する
		Object[] result = new Object[parsedSetting.length - 4];
		for (int i = 4; i < parsedSetting.length; i++) {
			String s = parsedSetting[i];
			if (s.matches("^\".*\"$")) {
				result[i - 4] = s.substring(1, s.length() - 1);
			} else if (s.matches("-?[0-9]+")) {
				result[i - 4] = Integer.parseInt(s);
			} else if (s.matches("-?[0-9]+\\.[0-9]+")) {
				result[i - 4] = Double.parseDouble(s);
			}
		}
		return result;
	}
}
